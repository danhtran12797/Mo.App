package com.thd.danhtran12797.moapp.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.ProductAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityCategoryBinding;
import com.thd.danhtran12797.moapp.databinding.EditCategoryCustomBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_GROUP_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_PRODUCT_ID;

public class CategoryActivity extends AppCompatActivity implements ProductAdapter.ProductInterface {

    public static final String PRODUCT_URL = "https://firebasestorage.googleapis.com/v0/b/appmusicfrist.appspot.com/o/image_app%2Fimg_vn%2Fimage_product.jpg?alt=media&token=352d0ad5-ae56-4a2d-b568-afb1bc30ee7a";

    private ActivityCategoryBinding categoryBinding;
    private ProductAdapter productAdapter;
    private CategoryViewModel categoryViewModel;
    private List<Product> lstProduct;
    private Category category;
    private String groupId;
    private Uri imageUri;
    private EditCategoryCustomBinding editCategoryCustomBinding;
    private boolean isDataChange = false;
    private Dialog editCateDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryBinding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(categoryBinding.getRoot());

        setSupportActionBar(categoryBinding.categoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            category = (Category) getIntent().getSerializableExtra(KEY_CATEGORY);
            groupId = getIntent().getStringExtra(KEY_GROUP_ID);
            getSupportActionBar().setTitle(category.getName());
        }

        categoryBinding.categoryToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        categoryBinding.addProductFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddProduct();
            }
        });

//        dummyData();

        productAdapter = new ProductAdapter(this);
        categoryBinding.categoryRecyclerView.setAdapter(productAdapter);
        categoryBinding.categoryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        categoryBinding.categoryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                categoryBinding.setIsLoading(true);
                categoryViewModel.getProducts(category.getId()).observe(CategoryActivity.this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        categoryBinding.setIsLoading(false);
                        productAdapter.submitList(products);
                    }
                });
            }
        });

    }

    public void openActivityAddProduct(){
        Intent intent = new Intent(CategoryActivity.this, DetailProductActivity.class);
        intent.putExtra(KEY_CATEGORY_ID, category.getId());
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(DATA_CHANGE, isDataChange);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDeleteCategoryDialog(){
        MaterialAlertDialogBuilder builder=new MaterialAlertDialogBuilder(this);
        builder.setTitle("Xóa danh mục");
        builder.setMessage("Bạn có đồng ý xóa danh mục?");
        builder.setIcon(R.drawable.ic_baseline_warning);
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                categoryViewModel.deleteCategory(category.getId()).observe(CategoryActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            if (s.equals("success")) {
                                Toast.makeText(CategoryActivity.this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                                isDataChange=true;
                                onBack();
                            }
                        }else{
                            Toast.makeText(CategoryActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public void showEditCategoryDialog() {
        imageUri = null;
        if (editCategoryCustomBinding != null)
            editCategoryCustomBinding = null;

        editCateDialogs = new Dialog(this);
        editCategoryCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_category_custom, null, false);
        editCateDialogs.setContentView(editCategoryCustomBinding.getRoot());
        editCateDialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        editCateDialogs.setCancelable(false);
        editCateDialogs.show();

        editCategoryCustomBinding.title.setText("Cập nhật danh mục");
        editCategoryCustomBinding.setCategory(category);

        editCategoryCustomBinding.imageEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(CategoryActivity.this);
            }
        });

        editCategoryCustomBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editCateDialogs.dismiss();
            }
        });

        editCategoryCustomBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameCate = editCategoryCustomBinding.nameCateTextView.getText().toString();
                if (!nameCate.matches("")) {
                    editCategoryCustomBinding.setIsLoading(true);
                    editCategoryCustomBinding.negativeButton.setEnabled(false);
                    editCategoryCustomBinding.positiveButton.setEnabled(false);
                    if (imageUri != null) {
                        File file = new File(imageUri.getPath());
                        categoryViewModel.uploadImage(file, "cate").observe(CategoryActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s != null) {
                                    if (!s.equals("failed")) {
                                        updateCategory(nameCate, s);
                                    }
                                } else {
                                    editCateDialogs.dismiss();
                                    Toast.makeText(CategoryActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        updateCategory(nameCate, category.getImage());
                    }

                } else {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateCategory(String nameCate, String imageCate) {
        categoryViewModel.updateCategory(category.getId(), nameCate, imageCate).observe(CategoryActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                editCategoryCustomBinding.setIsLoading(false);
//                editCategoryCustomBinding.negativeButton.setEnabled(true);
//                editCategoryCustomBinding.positiveButton.setEnabled(true);
                editCateDialogs.dismiss();
                if (s != null) {
                    if (s.equals("success")) {
                        Toast.makeText(CategoryActivity.this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
                        isDataChange = true;
                        getSupportActionBar().setTitle(nameCate);
                    }
                }
            }
        });
    }

    private void dummyData() {
        lstProduct = new ArrayList<>();

        String spec = "Tên SP: Băng keo trong - 50 mic\n" +
                "Qui cách: 4F8* 100Y (90 mét)\n" +
                "Đơn vị tính: Cuộn\n" +
                "Chất liệu: OPP\n" +
                "Chiều rộng: 10- 288mm\n" +
                "Chiều dài:  35- 1000m\n" +
                "Độ dày: 35- 65mic\n" +
                "Màu sắc: Trong sutts, nâu, màu\n" +
                "Chịu lực: >=24N/10mm\n" +
                "Lực bám dính:>=4N/100mm\n" +
                "Độ  giãn: 162%\n" +
                "Đặc điểm: Không thấm nức\n" +
                "Giá: 6000 đ\n" +
                "Hạn sử dụng:12/2025";
        Product product = new Product("Băng keo trong - 50 mic", "4F8* 100Y (90 mét)", PRODUCT_URL);

        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
        lstProduct.add(product);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                editCategoryCustomBinding.imageCategory.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        } else if (requestCode == ACTIVITY_REQUEST && resultCode == RESULT_OK && data != null) {
            boolean isChane = data.getBooleanExtra(DATA_CHANGE, false);
            if (isChane)
                categoryViewModel.setIsChangeData();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                Toast.makeText(this, "Hello Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_add_product:
                openActivityAddProduct();
                break;
            case R.id.menu_delete_category:
                showDeleteCategoryDialog();
                break;
            case R.id.menu_edit_category:
                showEditCategoryDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(this, DetailProductActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, product.getId());
        intent.putExtra(KEY_CATEGORY_ID, category.getId());
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

}
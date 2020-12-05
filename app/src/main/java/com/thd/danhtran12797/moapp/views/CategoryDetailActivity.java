package com.thd.danhtran12797.moapp.views;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.ProductAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityCategoryDetailBinding;
import com.thd.danhtran12797.moapp.databinding.EditCategoryCustomBinding;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.viewmodels.CategoryDetailViewModel;

import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_NAME;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_PRODUCT_ID;

public class CategoryDetailActivity extends BaseActivity implements ProductAdapter.ProductInterface {

    private static final String TAG = "CategoryDetailActivity";

    private ProductAdapter productAdapter;
    private ActivityCategoryDetailBinding categoryDetailBinding;
    private CategoryDetailViewModel categoryDetailViewModel;
    private String cate_id, cate_name;

    private EditCategoryCustomBinding editCategoryCustomBinding;

    private boolean isDataChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryDetailBinding = ActivityCategoryDetailBinding.inflate(getLayoutInflater());
        setContentView(categoryDetailBinding.getRoot());

        setSupportActionBar(categoryDetailBinding.categoryDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            cate_id = getIntent().getStringExtra(KEY_CATEGORY_ID);
            cate_name = getIntent().getStringExtra(KEY_CATEGORY_NAME);
            getSupportActionBar().setTitle(cate_name);
        }

        categoryDetailBinding.categoryDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        categoryDetailBinding.addProductFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivityAddProduct(cate_id);
            }
        });

        productAdapter = new ProductAdapter(this);
        categoryDetailBinding.categoryDetailRecyclerView.setAdapter(productAdapter);

        categoryDetailViewModel = new ViewModelProvider(this).get(CategoryDetailViewModel.class);
        categoryDetailViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                categoryDetailBinding.setIsLoading(true);
                categoryDetailViewModel.getProducts(cate_id).observe(CategoryDetailActivity.this, new Observer<List<Product>>() {
                    @Override
                    public void onChanged(List<Product> products) {
                        categoryDetailBinding.setIsLoading(false);
                        productAdapter.submitList(products);
                        if (products.size() != 0)
                            categoryDetailBinding.emptyProductLayout.setVisibility(View.GONE);
                        else
                            categoryDetailBinding.emptyProductLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    public void onBack() {
        Intent intent = new Intent();
        intent.putExtra(DATA_CHANGE, isDataChange);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void showDeleteCateDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Xóa danh mục");
        builder.setMessage("Bạn có đồng ý xóa danh mục?");
        builder.setIcon(R.drawable.ic_baseline_warning);
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                categoryDetailViewModel.deleteCategory(cate_id).observe(CategoryDetailActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            if (s.equals("success")) {
                                Toast.makeText(CategoryDetailActivity.this, "Xóa danh mục thành công", Toast.LENGTH_SHORT).show();
                                isDataChange = true;
                                onBack();
                            }
                        } else {
                            Toast.makeText(CategoryDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
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

    public void showEditCateDialog() {
        if (editCategoryCustomBinding != null)
            editCategoryCustomBinding = null;

        Dialog dialogs = new Dialog(this);
        editCategoryCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_category_custom, null, false);
        dialogs.setContentView(editCategoryCustomBinding.getRoot());
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setCancelable(false);
        dialogs.show();

        editCategoryCustomBinding.title.setText("Cập nhật danh mục");
        editCategoryCustomBinding.nameCategoryTextView.setText(cate_name);

        editCategoryCustomBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        editCategoryCustomBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameCategory = editCategoryCustomBinding.nameCategoryTextView.getText().toString();
                if (!nameCategory.matches("")) {
                    editCategoryCustomBinding.negativeButton.setEnabled(false);
                    editCategoryCustomBinding.positiveButton.setEnabled(false);
                    editCategoryCustomBinding.setIsLoading(true);
                    categoryDetailViewModel.updateCategory(cate_id, nameCategory).observe(CategoryDetailActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            editCategoryCustomBinding.negativeButton.setEnabled(true);
                            editCategoryCustomBinding.positiveButton.setEnabled(true);
                            editCategoryCustomBinding.setIsLoading(false);
                            if (s != null) {
                                if (s.equals("success")) {
                                    Toast.makeText(CategoryDetailActivity.this, "Cập nhật danh mục thành công", Toast.LENGTH_SHORT).show();
                                    dialogs.dismiss();
                                    getSupportActionBar().setTitle(nameCategory);
                                    isDataChange = true;
                                }
                            } else {
                                dialogs.dismiss();
                                Toast.makeText(CategoryDetailActivity.this, "Vui lòng kiểm tra Internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CategoryDetailActivity.this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openActivityAddProduct(String id_cate) {
        Intent intent = new Intent(CategoryDetailActivity.this, ProductDetailActivity.class);
        intent.putExtra(KEY_CATEGORY_ID, id_cate);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: " + isDataChange);
            boolean check = data.getBooleanExtra(DATA_CHANGE, false);
            if (check) {
                isDataChange = check;
                categoryDetailViewModel.setIsChangeData();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                startActivity(new Intent(this, SearchProductActivity.class));
                break;
            case R.id.menu_add_category:
                openActivityAddProduct(cate_id);
                break;
            case R.id.menu_delete_group:
                showDeleteCateDialog();
                break;
            case R.id.menu_edit_group:
                showEditCateDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(String id_pro) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, id_pro);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onAddItemClick(String id_cate) {

    }
}
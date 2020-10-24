package com.thd.danhtran12797.moapp.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.databinding.ActivityDetailProductBinding;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;
import com.thd.danhtran12797.moapp.viewmodels.ProductViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_PRODUCT_ID;

public class ProductDetailActivity extends BaseActivity {

    private static final String TAG = "DetailProductActivity";

    private ActivityDetailProductBinding detailProductBinding;
    private String productId;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private boolean hideMenu = false;
    private boolean isDataChange = false;
    private Uri imageUri = null;
    private String categoryId;
    private String name_pro, spec, color, material, thickness, price, width, length, adh_force, elas, charac, unit, bearing, exp_date;
    private Product productMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailProductBinding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        setContentView(detailProductBinding.getRoot());

        setSupportActionBar(detailProductBinding.productDetailToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detailProductBinding.productDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        detailProductBinding.imageEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(ProductDetailActivity.this);
            }
        });

        detailProductBinding.saveProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name_pro = detailProductBinding.nameEditText.getText().toString().trim();
                price = detailProductBinding.priceEditText.getText().toString().trim();
                spec = detailProductBinding.specEditText.getText().toString().trim();
                material = detailProductBinding.materialEditText.getText().toString().trim();
                thickness = detailProductBinding.thicknessEditText.getText().toString().trim();
                width = detailProductBinding.widthEditText.getText().toString().trim();
                length = detailProductBinding.lengthEditText.getText().toString().trim();
                color = detailProductBinding.colorEditText.getText().toString().trim();
                adh_force = detailProductBinding.adhForceEditText.getText().toString().trim();
                elas = detailProductBinding.elasEditText.getText().toString().trim();
                charac = detailProductBinding.characEditText.getText().toString().trim();
                unit = detailProductBinding.unitEditText.getText().toString().trim();
                bearing = detailProductBinding.powerEditText.getText().toString().trim();
                exp_date = detailProductBinding.expDateEditText.getText().toString().trim();

                if (checkValidate(name_pro, price, spec, material, thickness, width, length,
                        color, adh_force, elas, charac, unit, bearing, exp_date)) {
                    detailProductBinding.setIsLoading(true);
                    if (getIntent().hasExtra(KEY_PRODUCT_ID)) {
                        if (imageUri != null) {
                            uploadImage();
                        } else {
                            updateProduct(productMain.getImage());
                        }
                    } else {
                        if (imageUri != null) {
                            uploadImage();
                        } else {
                            detailProductBinding.setIsLoading(false);
                            Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng nhập dữ liệu!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        if (getIntent().hasExtra(KEY_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(KEY_PRODUCT_ID);
            productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
            detailProductBinding.setIsLoading(true);
            productViewModel.getProduct(productId).observe(this, new Observer<Product>() {
                @Override
                public void onChanged(Product product) {
                    productMain = product;
                    detailProductBinding.setIsLoading(false);
                    detailProductBinding.setProduct(productMain);
                }
            });
            setFocusableView(false);
        } else {
            categoryId = getIntent().getStringExtra(KEY_CATEGORY_ID);
            setFocusableView(true);
            invalidateOptionsMenu();
            hideMenu = true;
        }
    }

    public void showDeleteProductDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Xóa sản phẩm");
        builder.setMessage("Bạn có đồng ý xóa sản phẩm?");
        builder.setIcon(R.drawable.ic_baseline_warning);
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                productViewModel.deleteProduct(productId).observe(ProductDetailActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            if (s.equals("success")) {
                                Toast.makeText(ProductDetailActivity.this, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                                isDataChange = true;
                                onBack();
                            }
                        } else {
                            Toast.makeText(ProductDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
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

    public void uploadImage() {
//        File file = new File(imageUri.getPath());
        categoryViewModel.uploadImage(imageUri, "pro").observe(ProductDetailActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (!s.equals("failed")) {
                        if (getIntent().hasExtra(KEY_PRODUCT_ID))
                            updateProduct(s);
                        else
                            insertProduct(s);
                    }
                } else {
                    detailProductBinding.setIsLoading(false);
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void insertProduct(String image_pro) {
        categoryViewModel.insertProduct(categoryId, name_pro, image_pro, price, "1",
                spec, material, thickness, width, length,
                color, adh_force, elas, charac, unit, bearing, exp_date).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")) {
                    Toast.makeText(ProductDetailActivity.this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    isDataChange = true;
                    onBack();
                }
            }
        });
    }

    public void updateProduct(String image_pro) {
        productViewModel.updateProduct(productMain.getId(), name_pro, image_pro, price, "1", spec,
                material, thickness, width, length, color, adh_force, elas,
                charac, unit, bearing, exp_date).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")) {
                    detailProductBinding.setIsLoading(false);
                    Toast.makeText(ProductDetailActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    setFocusableView(false);
                    isDataChange = true;
                }
            }
        });
    }

    public boolean checkValidate(String name_pro, String price,
                                 String spec, String material, String thickness,
                                 String width, String length, String color, String adh_force, String elas,
                                 String charac, String unit, String bearing, String exp_date) {

        if (name_pro.equals("")) {
            detailProductBinding.nameEditText.requestFocus();
            return false;
        } else if (spec.equals("")) {
            detailProductBinding.specEditText.requestFocus();
            return false;
        } else if (material.equals("")) {
            detailProductBinding.materialEditText.requestFocus();
            return false;
        } else if (thickness.equals("")) {
            detailProductBinding.thicknessEditText.requestFocus();
            return false;
        } else if (width.equals("")) {
            detailProductBinding.widthEditText.requestFocus();
            return false;
        } else if (length.equals("")) {
            detailProductBinding.lengthEditText.requestFocus();
            return false;
        } else if (color.equals("")) {
            detailProductBinding.colorEditText.requestFocus();
            return false;
        } else if (adh_force.equals("")) {
            detailProductBinding.adhForceEditText.requestFocus();
            return false;
        } else if (elas.equals("")) {
            detailProductBinding.elasEditText.requestFocus();
            return false;
        } else if (charac.equals("")) {
            detailProductBinding.specEditText.requestFocus();
            return false;
        } else if (unit.equals("")) {
            detailProductBinding.specEditText.requestFocus();
            return false;
        } else if (bearing.equals("")) {
            detailProductBinding.powerEditText.requestFocus();
            return false;
        } else if (exp_date.equals("")) {
            detailProductBinding.powerEditText.requestFocus();
            return false;
        } else if (price.equals("")) {
            detailProductBinding.priceEditText.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        onBack();
    }

    private void onBack() {
        Intent intent = new Intent();
        intent.putExtra(DATA_CHANGE, isDataChange);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void setFocusableView(boolean state) {
        detailProductBinding.nameEditText.setFocusable(state);
        detailProductBinding.specEditText.setFocusable(state);
        detailProductBinding.unitEditText.setFocusable(state);
        detailProductBinding.materialEditText.setFocusable(state);
        detailProductBinding.widthEditText.setFocusable(state);
        detailProductBinding.lengthEditText.setFocusable(state);
        detailProductBinding.colorEditText.setFocusable(state);
        detailProductBinding.powerEditText.setFocusable(state);
        detailProductBinding.adhForceEditText.setFocusable(state);
        detailProductBinding.elasEditText.setFocusable(state);
        detailProductBinding.characEditText.setFocusable(state);
        detailProductBinding.priceEditText.setFocusable(state);
        detailProductBinding.expDateEditText.setFocusable(state);
        detailProductBinding.thicknessEditText.setFocusable(state);

        detailProductBinding.saveProductButton.setVisibility(state == true ? View.VISIBLE : View.GONE);
        detailProductBinding.imageEditLayout.setEnabled(state);

        if (state) {
            detailProductBinding.nameEditText.setFocusableInTouchMode(state);
            detailProductBinding.specEditText.setFocusableInTouchMode(state);
            detailProductBinding.unitEditText.setFocusableInTouchMode(state);
            detailProductBinding.materialEditText.setFocusableInTouchMode(state);
            detailProductBinding.widthEditText.setFocusableInTouchMode(state);
            detailProductBinding.lengthEditText.setFocusableInTouchMode(state);
            detailProductBinding.colorEditText.setFocusableInTouchMode(state);
            detailProductBinding.powerEditText.setFocusableInTouchMode(state);
            detailProductBinding.adhForceEditText.setFocusableInTouchMode(state);
            detailProductBinding.elasEditText.setFocusableInTouchMode(state);
            detailProductBinding.characEditText.setFocusableInTouchMode(state);
            detailProductBinding.priceEditText.setFocusableInTouchMode(state);
            detailProductBinding.expDateEditText.setFocusableInTouchMode(state);
            detailProductBinding.thicknessEditText.setFocusableInTouchMode(state);
        }

        if (state)
            detailProductBinding.nameEditText.requestFocus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: GOOD JOB");
                imageUri = result.getUri();
                detailProductBinding.imageProduct.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: " + error.getMessage());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_detail_menu, menu);
        if (hideMenu) {
            for (int i = 0; i < menu.size(); i++)
                menu.getItem(i).setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_edit_product:
                setFocusableView(true);
                break;
            case R.id.menu_delete_product:
                showDeleteProductDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
package com.thd.danhtran12797.moapp.views;

import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.ImageDetail2Adapter;
import com.thd.danhtran12797.moapp.adapters.ImageDetailAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityDetailProductBinding;
import com.thd.danhtran12797.moapp.databinding.DialogCropImageBinding;
import com.thd.danhtran12797.moapp.databinding.DialogImageDetailBinding;
import com.thd.danhtran12797.moapp.models.ImageDetail;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.models.UploadMultiResponse;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;
import com.thd.danhtran12797.moapp.viewmodels.ProductViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_PRODUCT_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.PICK_IMAGE_MULTIPLE;

public class ProductDetailActivity extends BaseActivity implements ImageDetailAdapter.ImageDetailInterface, PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "DetailProductActivity";

    private ActivityDetailProductBinding detailProductBinding;
    private DialogImageDetailBinding imageDetailBinding;
    private DialogCropImageBinding cropImageBinding;
    private String productId;
    private ProductViewModel productViewModel;
    private CategoryViewModel categoryViewModel;
    private boolean hideMenu = false;
    private boolean isDataChange = false;
    private String categoryId;
    private String name_pro, spec, color, material, thickness, price, width, length, adh_force, elas, charac, unit, bearing, exp_date;
    private Dialog imageDetailDialog;
    private Dialog cropImageDialog;
    private ImageDetailAdapter imageDetailAdapter;
    private ImageDetail2Adapter imageDetail2Adapter;
    private ImageDetail2Adapter imageDetail2Adapter1;
    private List<Uri> mArrayUri;
    private List<Bitmap> lstBitmapCrop;
    private boolean isEdit = false;
    private int currentCropImage = 1;

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

//        detailProductBinding.takePhotoImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                checkPermission(ProductDetailActivity.this);
//            }
//        });

        detailProductBinding.imageProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getIntent().hasExtra(KEY_PRODUCT_ID)) {
                    showProductDetailDialog(isEdit);
                } else {
                    checkPermission(ProductDetailActivity.this, true);
                }
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

//                if (checkValidate(name_pro, price, spec, material, thickness, width, length,
//                        color, adh_force, elas, charac, unit, bearing, exp_date)) {
                detailProductBinding.setIsLoading(true);
                if (getIntent().hasExtra(KEY_PRODUCT_ID)) {
                    if (lstBitmapCrop != null && lstBitmapCrop.size() > 0) {
                        uploadFileMultilPart(false);
                    } else {
                        updateProduct();
                    }
                } else {
                    if (lstBitmapCrop != null && lstBitmapCrop.size() > 0) {
                        uploadFileMultilPart(true);
                    } else {
                        detailProductBinding.setIsLoading(false);
                        Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
                    }
                }
//                } else {
//                    Toast.makeText(ProductDetailActivity.this, "Vui lòng nhập dữ liệu!", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        if (getIntent().hasExtra(KEY_PRODUCT_ID)) {
            productId = getIntent().getStringExtra(KEY_PRODUCT_ID);
            detailProductBinding.setIsLoading(true);
            productViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean aBoolean) {
                    productViewModel.getProduct(productId).observe(ProductDetailActivity.this, new Observer<Product>() {
                        @Override
                        public void onChanged(Product product) {
                            detailProductBinding.setIsLoading(false);

                            detailProductBinding.setProduct(product);

                            detailProductBinding.countImageTextView.setVisibility(View.GONE);
                            if (product.getImageDetail().size() > 1)
                                detailProductBinding.imageDetailRecyclerView.setVisibility(View.VISIBLE);
                            else
                                detailProductBinding.imageDetailRecyclerView.setVisibility(View.GONE);

                            imageDetailAdapter.setLstImageDetail(product.getImageDetail());
                            imageDetailAdapter.notifyDataSetChanged();
                            imageDetail2Adapter.setLstImageDetail(product.getImageDetail());
                            imageDetail2Adapter.notifyDataSetChanged();

                            imageDetail2Adapter1 = new ImageDetail2Adapter(true);
                            imageDetail2Adapter1.setLstImageDetail(product.getImageDetail());
                            detailProductBinding.imageDetailRecyclerView.setAdapter(imageDetail2Adapter1);
                            detailProductBinding.imageDetailRecyclerView.addItemDecoration(new DividerItemDecoration(ProductDetailActivity.this, DividerItemDecoration.VERTICAL));
                        }
                    });
                }
            });

            detailProductBinding.setEnableView(false);
        } else {
            categoryId = getIntent().getStringExtra(KEY_CATEGORY_ID);
            detailProductBinding.setEnableView(true);
            detailProductBinding.imageDetailRecyclerView.setVisibility(View.GONE);
            invalidateOptionsMenu();
            hideMenu = true;
            isEdit = true;
        }

        initImageDetail();
    }

    private void initCropImage() {
        lstBitmapCrop = new ArrayList<>();
        currentCropImage = 1;

        if (cropImageBinding != null)
            cropImageBinding = null;
        cropImageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_crop_image, null, false);

        cropImageDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        cropImageDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cropImageDialog.setContentView(cropImageBinding.getRoot());

        cropImageBinding.cropImageView.setImageUriAsync(mArrayUri.get(0));
        cropImageBinding.currentCropTextView.setText(currentCropImage + "");
        cropImageBinding.sizeCropTextView.setText(String.valueOf(mArrayUri.size()));

        cropImageBinding.cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstBitmapCrop.add(cropImageBinding.cropImageView.getCroppedImage());
                currentCropImage++;
                if (currentCropImage > mArrayUri.size()) {
                    cropImageDialog.dismiss();
                    detailProductBinding.countImageTextView.setVisibility(View.VISIBLE);
                    if (getIntent().hasExtra(KEY_PRODUCT_ID)) {
                        detailProductBinding.countImageTextView.setText("+" + lstBitmapCrop.size());
                    } else {
                        detailProductBinding.imageProduct.setImageBitmap(lstBitmapCrop.get(0));
                        if (lstBitmapCrop.size() > 1)
                            detailProductBinding.countImageTextView.setText("+" + (lstBitmapCrop.size() - 1));
                    }
                } else {
                    cropImageBinding.currentCropTextView.setText(currentCropImage + "");
                    cropImageBinding.cropImageView.setImageUriAsync(mArrayUri.get(currentCropImage - 1));
                }
            }
        });

        cropImageDialog.show();
    }

    private String json_array_images(List<String> images) {
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < images.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("image", images.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            jsonArray.put(jsonObject);
        }
        return jsonArray.toString();
    }

    private void insertImage(String json) {
        productViewModel.insertImage(productId, json).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    updateProduct();
                } else {
                    detailProductBinding.setIsLoading(false);
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateImage(String id, String image) {
        productViewModel.updateImage(id, image).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                detailProductBinding.setIsLoading(false);
                if (s != null) {
                    productViewModel.setIsChangeData();
                    isDataChange = true;
                    if (image.equals("")) {
                        imageDetailAdapter.setPosPress1(0);
                        Toast.makeText(ProductDetailActivity.this, "Xóa chi tiết ảnh thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProductDetailActivity.this, "Cập nhật chi tiết ảnh thành công", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void uploadFileMultilPart(boolean insertProduct) {
        detailProductBinding.setIsLoading(true);
        productViewModel.uploadFileMultilPart(lstBitmapCrop).observe(this, new Observer<UploadMultiResponse>() {
            @Override
            public void onChanged(UploadMultiResponse response) {
                if (response != null) {
                    String json = json_array_images(response.getName_images());
                    Log.d(TAG, "onChanged: " + json);
                    if (insertProduct)
                        // insert product
                        insertProduct(json);
                    else {
                        // insert images for product
                        insertImage(json);
                    }

                } else {
                    detailProductBinding.setIsLoading(false);
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initImageDetail() {
        imageDetailBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_image_detail, null, false);

        imageDetailDialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
        imageDetailDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageDetailDialog.setContentView(imageDetailBinding.getRoot());

        imageDetailBinding.closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageDetailDialog.dismiss();
            }
        });

        imageDetailAdapter = new ImageDetailAdapter(this);
        imageDetailBinding.imageDetailRecyclerView.setAdapter(imageDetailAdapter);

        imageDetail2Adapter = new ImageDetail2Adapter(false);
        imageDetailBinding.viewPager2.setAdapter(imageDetail2Adapter);
        imageDetailBinding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.d(TAG, "onPageSelected: " + position);
                imageDetailAdapter.setPosPress(position);
            }
        });

        imageDetailBinding.editImageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });
    }

    public void showProductDetailDialog(boolean isEdit) {
        if (isEdit)
            imageDetailBinding.editImageMenu.setVisibility(View.VISIBLE);
        else
            imageDetailBinding.editImageMenu.setVisibility(View.GONE);

        imageDetailDialog.show();
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

    public void uploadImage(Uri imageUri) {
        detailProductBinding.setIsLoading(true);
        categoryViewModel.uploadImage(imageUri, "pro").observe(ProductDetailActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    updateImage(imageDetailAdapter.getIdImageDetail(), s);
                } else {
                    detailProductBinding.setIsLoading(false);
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void insertProduct(String json_images) {
        categoryViewModel.insertProduct(categoryId, name_pro, price, "1",
                spec, material, thickness, width, length,
                color, adh_force, elas, charac, unit, bearing, exp_date, json_images).observe(this, new Observer<String>() {
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

    public void updateProduct() {
        productViewModel.updateProduct(productId, name_pro, price, "1", spec,
                material, thickness, width, length, color, adh_force, elas,
                charac, unit, bearing, exp_date).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s.equals("success")) {
                    detailProductBinding.setIsLoading(false);
                    Toast.makeText(ProductDetailActivity.this, "Cập nhật sản phẩm thành công", Toast.LENGTH_SHORT).show();
                    detailProductBinding.setEnableView(false);
                    isEdit = false;
                    isDataChange = true;
                    productViewModel.setIsChangeData();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: GOOD JOB");
                uploadImage(result.getUri());
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: " + error.getMessage());
            }
        } else if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {
            mArrayUri = new ArrayList<>();
            if (data.getData() != null) {
                Log.d(TAG, "PICK MULTI IMAGE: ONE");
                Uri mImageUri = data.getData();
                mArrayUri.add(mImageUri);
                initCropImage();
            } else {
                ClipData mClipData = data.getClipData();
                for (int i = 0; i < mClipData.getItemCount(); i++) {
                    ClipData.Item item = mClipData.getItemAt(i);
                    Uri uri = item.getUri();
                    mArrayUri.add(uri);
                }
                Log.d(TAG, "PICK MULTI IMAGE: " + mClipData.getItemCount());
                initCropImage();
            }
        }
    }

    public void showPopup(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_image_menu);
        popup.show();
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
                isEdit = true;
                detailProductBinding.setEnableView(true);
                break;
            case R.id.menu_delete_product:
                showDeleteProductDialog();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(ImageDetail imageDetail, int pos) {
        imageDetailBinding.viewPager2.setCurrentItem(pos);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add_image:
                imageDetailDialog.dismiss();
                checkPermission(ProductDetailActivity.this, true);
                break;
            case R.id.menu_edit_image:
                imageDetailDialog.dismiss();
                checkPermission(ProductDetailActivity.this, false);
                break;
            case R.id.menu_delete_image:
                imageDetailDialog.dismiss();
                detailProductBinding.setIsLoading(true);
                updateImage(imageDetailAdapter.getIdImageDetail(), "");
                break;
        }
        return false;
    }
}
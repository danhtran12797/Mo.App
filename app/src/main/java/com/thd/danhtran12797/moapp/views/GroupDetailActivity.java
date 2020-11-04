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
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.CategoryAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityGroupDetailBinding;
import com.thd.danhtran12797.moapp.databinding.EditCategoryCustomBinding;
import com.thd.danhtran12797.moapp.databinding.EditGroupCustomBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;
import com.thd.danhtran12797.moapp.viewmodels.GroupDetailViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_GROUP_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_GROUP_NAME;

public class GroupDetailActivity extends BaseActivity implements CategoryAdapter.CategoryInterface {

    private static final String TAG = "GroupDetailActivity";

    private CategoryAdapter categoryAdapter;
    private ActivityGroupDetailBinding groupDetailBinding;
    private GroupDetailViewModel groupDetailViewModel;
    private String group_id, group_name;

    private Uri imageUri = null;
    private EditCategoryCustomBinding editCategoryCustomBinding;
    private EditGroupCustomBinding editGroupCustomBinding;
    private CategoryViewModel categoryViewModel;

    private boolean isDataChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        groupDetailBinding = ActivityGroupDetailBinding.inflate(getLayoutInflater());
        setContentView(groupDetailBinding.getRoot());

        setSupportActionBar(groupDetailBinding.groupDetailToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            group_id = getIntent().getStringExtra(KEY_GROUP_ID);
            group_name = getIntent().getStringExtra(KEY_GROUP_NAME);
            getSupportActionBar().setTitle(group_name);
        }

        groupDetailBinding.groupDetailToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBack();
            }
        });

        groupDetailBinding.addCategoryFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog(group_id);
            }
        });

        categoryAdapter = new CategoryAdapter(this);
        groupDetailBinding.groupDetailRecyclerView.setAdapter(categoryAdapter);

        groupDetailViewModel = new ViewModelProvider(this).get(GroupDetailViewModel.class);
        groupDetailViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                groupDetailBinding.setIsLoading(true);
                groupDetailViewModel.getCategories(group_id).observe(GroupDetailActivity.this, new Observer<List<Category>>() {
                    @Override
                    public void onChanged(List<Category> categories) {
                        groupDetailBinding.setIsLoading(false);
                        categoryAdapter.submitList(categories);
                        if (categories.size() != 0)
                            groupDetailBinding.emptyCateLayout.setVisibility(View.GONE);
                        else
                            groupDetailBinding.emptyCateLayout.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
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

    public void showDeleteGroupDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("Xóa nhóm");
        builder.setMessage("Bạn có đồng ý xóa nhóm?");
        builder.setIcon(R.drawable.ic_baseline_warning);
        builder.setCancelable(false);
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                groupDetailViewModel.deleteGroup(group_id).observe(GroupDetailActivity.this, new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        if (s != null) {
                            if (s.equals("success")) {
                                Toast.makeText(GroupDetailActivity.this, "Xóa nhóm thành công", Toast.LENGTH_SHORT).show();
                                isDataChange = true;
                                onBack();
                            }
                        } else {
                            Toast.makeText(GroupDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
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

    public void showEditGroupDialog() {
        if (editGroupCustomBinding != null)
            editGroupCustomBinding = null;

        Dialog dialogs = new Dialog(this);
        editGroupCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_group_custom, null, false);
        dialogs.setContentView(editGroupCustomBinding.getRoot());
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setCancelable(false);
        dialogs.show();

        editGroupCustomBinding.title.setText("Cập nhật nhóm");
        editGroupCustomBinding.nameGroupTextView.setText(group_name);

        editGroupCustomBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        editGroupCustomBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameGroup = editGroupCustomBinding.nameGroupTextView.getText().toString();
                if (!nameGroup.matches("")) {
                    editGroupCustomBinding.negativeButton.setEnabled(false);
                    editGroupCustomBinding.positiveButton.setEnabled(false);
                    editGroupCustomBinding.setIsLoading(true);
                    groupDetailViewModel.updateGroup(group_id, nameGroup).observe(GroupDetailActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            editGroupCustomBinding.negativeButton.setEnabled(true);
                            editGroupCustomBinding.positiveButton.setEnabled(true);
                            editGroupCustomBinding.setIsLoading(false);
                            if (s != null) {
                                if (s.equals("success")) {
                                    Toast.makeText(GroupDetailActivity.this, "Cập nhật nhóm thành công", Toast.LENGTH_SHORT).show();
                                    dialogs.dismiss();
                                    getSupportActionBar().setTitle(nameGroup);
                                    isDataChange = true;
                                }
                            } else {
                                dialogs.dismiss();
                                Toast.makeText(GroupDetailActivity.this, "Vui lòng kiểm tra Internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(GroupDetailActivity.this, "Vui lòng nhập tên nhóm!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showAddCategoryDialog(String groupId) {
        imageUri = null;
        if (editCategoryCustomBinding != null)
            editCategoryCustomBinding = null;

        Dialog dialogs = new Dialog(this);
        editCategoryCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_category_custom, null, false);
        dialogs.setContentView(editCategoryCustomBinding.getRoot());
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setCancelable(false);
        dialogs.show();

        editCategoryCustomBinding.imageEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission(GroupDetailActivity.this, false);
            }
        });

        editCategoryCustomBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        editCategoryCustomBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    String nameCate = editCategoryCustomBinding.nameCateTextView.getText().toString();
                    if (!nameCate.matches("")) {
                        editCategoryCustomBinding.setIsLoading(true);
                        editCategoryCustomBinding.negativeButton.setEnabled(false);
                        editCategoryCustomBinding.positiveButton.setEnabled(false);
//                        File file = new File(imageUri.getPath());
                        categoryViewModel.uploadImage(imageUri, "cate").observe(GroupDetailActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if (s != null) {
                                    if (!s.equals("failed")) {
                                        categoryViewModel.insertCategory(groupId, nameCate, s).observe(GroupDetailActivity.this, new Observer<String>() {
                                            @Override
                                            public void onChanged(String s) {
                                                editCategoryCustomBinding.setIsLoading(false);
                                                editCategoryCustomBinding.negativeButton.setEnabled(true);
                                                editCategoryCustomBinding.positiveButton.setEnabled(true);
                                                dialogs.dismiss();
                                                if (s != null) {
                                                    if (s.equals("success")) {
                                                        Toast.makeText(GroupDetailActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                                                        isDataChange = true;
                                                        groupDetailViewModel.setIsChangeData();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(GroupDetailActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                                    dialogs.dismiss();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(GroupDetailActivity.this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(GroupDetailActivity.this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
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
                Log.d(TAG, "onActivityResult: " + error.getMessage());
            }
        } else if (requestCode == ACTIVITY_REQUEST && resultCode == RESULT_OK && data != null) {
            Log.d(TAG, "onActivityResult: " + isDataChange);
            boolean check = data.getBooleanExtra(DATA_CHANGE, false);
            if (check) {
                isDataChange = check;
                groupDetailViewModel.setIsChangeData();
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
                showAddCategoryDialog(group_id);
                break;
            case R.id.menu_delete_group:
                showDeleteGroupDialog();
                break;
            case R.id.menu_edit_group:
                showEditGroupDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Category category, String groupId) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(KEY_CATEGORY, category);
        intent.putExtra(KEY_GROUP_ID, group_id);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onAddItemClick(String groupId) {

    }
}
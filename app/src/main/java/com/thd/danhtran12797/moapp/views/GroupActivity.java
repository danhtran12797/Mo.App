package com.thd.danhtran12797.moapp.views;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.CategoryAdapter;
import com.thd.danhtran12797.moapp.adapters.GroupAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityGroupBinding;
import com.thd.danhtran12797.moapp.databinding.EditCategoryCustomBinding;
import com.thd.danhtran12797.moapp.databinding.EditGroupCustomBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.models.Group;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;
import com.thd.danhtran12797.moapp.viewmodels.GroupViewModel;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_NAME;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_GROUP_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_GROUP_NAME;

public class GroupActivity extends BaseActivity implements GroupAdapter.GroupInterface, CategoryAdapter.CategoryInterface {

    private static final String TAG = "GroupActivity";
    public static final String IMAGE_URL = "https://firebasestorage.googleapis.com/v0/b/appmusicfrist.appspot.com/o/image_app%2Fimg_vn%2Fnhom_bangkeo1.png?alt=media&token=551baa54-7535-4910-917e-5577eb283775";
    private List<Group> lstGroup;
    private GroupAdapter groupAdapter;
    private ActivityGroupBinding binding;
    private EditGroupCustomBinding editGroupCustomBinding;
    private EditCategoryCustomBinding editCategoryCustomBinding;

    private GroupViewModel groupViewModel;
    private CategoryViewModel categoryViewModel;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        setSupportActionBar(binding.groupToolbar);

//        dummyData();

        groupAdapter = new GroupAdapter(GroupActivity.this, GroupActivity.this);
        binding.groupRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.groupRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        binding.groupRecyclerView.setAdapter(groupAdapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        groupViewModel = new ViewModelProvider(this).get(GroupViewModel.class);
        groupViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                binding.setIsLoading(true);
                groupViewModel.getGroups().observe(GroupActivity.this, new Observer<List<Group>>() {
                    @Override
                    public void onChanged(List<Group> groups) {
                        binding.setIsLoading(false);
                        if (groups != null) {
                            groupAdapter.submitList(groups);
                        }else{
                            Toast.makeText(GroupActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        binding.addGroupFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddGroupDialog();
            }
        });
    }

    public void showAddGroupDialog() {
        if(editGroupCustomBinding!=null)
            editGroupCustomBinding=null;

        Dialog dialogs = new Dialog(this);
        editGroupCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_group_custom, null, false);
        dialogs.setContentView(editGroupCustomBinding.getRoot());
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setCancelable(false);
        dialogs.show();

        editGroupCustomBinding.negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogs.dismiss();
            }
        });

        editGroupCustomBinding.positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameGroup=editGroupCustomBinding.nameGroupTextView.getText().toString();
                if (!nameGroup.matches("")) {
                    editGroupCustomBinding.negativeButton.setEnabled(false);
                    editGroupCustomBinding.positiveButton.setEnabled(false);
                    editGroupCustomBinding.setIsLoading(true);
                    groupViewModel.insertGroup(nameGroup).observe(GroupActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            editGroupCustomBinding.negativeButton.setEnabled(true);
                            editGroupCustomBinding.positiveButton.setEnabled(true);
                            editGroupCustomBinding.setIsLoading(false);
                            if(s!=null){
                                if(s.equals("success")){
                                    Toast.makeText(GroupActivity.this, "Thêm nhóm thành công", Toast.LENGTH_SHORT).show();
                                    dialogs.dismiss();
                                    groupViewModel.setIsChangeData();
                                }
                            }else{
                                dialogs.dismiss();
                                Toast.makeText(GroupActivity.this, "Vui lòng kiểm tra Internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(GroupActivity.this, "Vui lòng nhập tên nhóm!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void showAddCategoryDialog(String groupId){
        imageUri = null;
        if(editCategoryCustomBinding!=null)
            editCategoryCustomBinding=null;

        Dialog dialogs = new Dialog(this);
        editCategoryCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_category_custom, null, false);
        dialogs.setContentView(editCategoryCustomBinding.getRoot());
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setCancelable(false);
        dialogs.show();

        editCategoryCustomBinding.imageEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(GroupActivity.this);
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
                if(imageUri!=null){
                    String nameCate = editCategoryCustomBinding.nameCateTextView.getText().toString();
                    if(!nameCate.matches("")){
                        editCategoryCustomBinding.setIsLoading(true);
                        editCategoryCustomBinding.negativeButton.setEnabled(false);
                        editCategoryCustomBinding.positiveButton.setEnabled(false);
                        File file = new File(imageUri.getPath());
                        categoryViewModel.uploadImage(file, "cate").observe(GroupActivity.this, new Observer<String>() {
                            @Override
                            public void onChanged(String s) {
                                if(s!=null){
                                    if(!s.equals("failed")){
                                        categoryViewModel.insertCategory(groupId, nameCate, s).observe(GroupActivity.this, new Observer<String>() {
                                            @Override
                                            public void onChanged(String s) {
                                                editCategoryCustomBinding.setIsLoading(false);
                                                editCategoryCustomBinding.negativeButton.setEnabled(true);
                                                editCategoryCustomBinding.positiveButton.setEnabled(true);
                                                dialogs.dismiss();
                                                if (s != null) {
                                                    if(s.equals("success")){
                                                        Toast.makeText(GroupActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                                                        groupViewModel.setIsChangeData();
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        });
                    }else{
                        Toast.makeText(GroupActivity.this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(GroupActivity.this, "Vui lòng chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void dummyData() {
        lstGroup = new ArrayList<>();

        Category category = new Category("1", "BK trong", IMAGE_URL);
        Category category1 = new Category("1", "BK điện nano", IMAGE_URL);
        Category category2 = new Category("1", "BK giấy nhăn", IMAGE_URL);
        Category category3 = new Category("1", "BK điện nano không màu", IMAGE_URL);
        Category category4 = new Category("1", "BK đục", IMAGE_URL);
        Category category5 = new Category("1", "BK trong", IMAGE_URL);
        Category category6 = new Category("1", "BK trong", IMAGE_URL);
        Category category7 = new Category("1", "BK không màu", IMAGE_URL);
        Category category8 = new Category("1", "BK điện nano không màu", IMAGE_URL);

        List<Category> lstCate1 = new ArrayList<>();
        lstCate1.add(category1);
        lstCate1.add(category);
        lstCate1.add(category2);
        lstCate1.add(category3);
        lstCate1.add(category4);
        lstCate1.add(category5);
        lstCate1.add(category6);
        lstCate1.add(category7);
        lstCate1.add(category8);

        List<Category> lstCate2 = new ArrayList<>();
        lstCate2.add(category5);
        lstCate2.add(category6);
        lstCate2.add(category7);
        lstCate2.add(category8);

        List<Category> lstCate3 = new ArrayList<>();
        lstCate3.add(category2);
        lstCate3.add(category3);
        lstCate3.add(category4);
        lstCate3.add(category5);
        lstCate3.add(category6);
        lstCate3.add(category7);
        lstCate3.add(category8);

        lstGroup.add(new Group("1", "Nhóm băng keo dân dụng", lstCate1));
        lstGroup.add(new Group("1", "Nhóm băng keo văn phòng, gia đình, dán thùng", lstCate2));
        lstGroup.add(new Group("1", "Nhóm băng keo công nghiệp", lstCate3));
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
                Log.d(TAG, "onActivityResult: "+error.getMessage());
            }
        }else if(requestCode==ACTIVITY_REQUEST&&resultCode==RESULT_OK&&data!=null){
            boolean dataChane=data.getBooleanExtra(DATA_CHANGE, false);
            Log.d(TAG, "onActivityResult: "+dataChane);
            if(dataChane)
                groupViewModel.setIsChangeData();
        }
    }

    @Override
    public void onItemClick(Group group) {
        Intent intent = new Intent(this, GroupDetailActivity.class);
        intent.putExtra(KEY_GROUP_ID, group.getId());
        intent.putExtra(KEY_GROUP_NAME, group.getName());
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onItemClick(Category category, String groupId) {
        Intent intent = new Intent(this, CategoryActivity.class);
        intent.putExtra(KEY_CATEGORY, category);
        intent.putExtra(KEY_GROUP_ID, groupId);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onAddItemClick(String groupId) {
        showAddCategoryDialog(groupId);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.group_menu, menu);
//        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(GroupActivity.this, "Hello Search View", Toast.LENGTH_SHORT).show();
//            }
//        });
//        return true;
//    }
}
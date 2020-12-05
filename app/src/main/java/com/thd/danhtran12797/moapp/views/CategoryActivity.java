package com.thd.danhtran12797.moapp.views;

import android.app.Dialog;
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
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.google.android.material.navigation.NavigationView;
import com.thd.danhtran12797.moapp.R;
import com.thd.danhtran12797.moapp.adapters.CategoryAdapter;
import com.thd.danhtran12797.moapp.adapters.ProductAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityCategoryBinding;
import com.thd.danhtran12797.moapp.databinding.CustomActionItemLayoutBinding;
import com.thd.danhtran12797.moapp.databinding.EditCategoryCustomBinding;
import com.thd.danhtran12797.moapp.databinding.NavHeaderBinding;
import com.thd.danhtran12797.moapp.models.Category;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;

import java.util.List;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CATEGORY_NAME;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_PRODUCT_ID;

public class CategoryActivity extends BaseActivity implements CategoryAdapter.CategoryInterface, ProductAdapter.ProductInterface, NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "CategoryActivity";
    private CategoryAdapter categoryAdapter;
    private ActivityCategoryBinding categoryBinding;
    private EditCategoryCustomBinding editCategoryCustomBinding;

    private CategoryViewModel categoryViewModel;
    private NavHeaderBinding navHeaderBinding;
    private CustomActionItemLayoutBinding actionItemLayoutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryBinding = ActivityCategoryBinding.inflate(getLayoutInflater());
        setContentView(categoryBinding.getRoot());

        categoryAdapter = new CategoryAdapter(CategoryActivity.this, CategoryActivity.this);
        categoryBinding.categoryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        categoryBinding.categoryRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        categoryBinding.categoryRecyclerView.setAdapter(categoryAdapter);

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                categoryBinding.setIsLoading(true);
                categoryViewModel.getCategories().observe(CategoryActivity.this, new Observer<List<Category>>() {
                    @Override
                    public void onChanged(List<Category> categories) {
                        categoryBinding.setIsLoading(false);
                        if (categories != null) {
                            categoryAdapter.submitList(categories);
                        } else {
                            Toast.makeText(CategoryActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        categoryBinding.addCategoryFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddCategoryDialog();
            }
        });

        initToolBar();
        initDrawer();
    }

    private void initToolBar() {
        setSupportActionBar(categoryBinding.categoryToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        categoryBinding.categoryToolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }


    private void initDrawer() {
        categoryBinding.navView.setNavigationItemSelectedListener(this);

        View headerView = categoryBinding.navView.getHeaderView(0);
        navHeaderBinding = NavHeaderBinding.bind(headerView);
//        initHeaderView();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, categoryBinding.drawerLayout, categoryBinding.categoryToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        categoryBinding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void showAddCategoryDialog() {
        if (editCategoryCustomBinding != null)
            editCategoryCustomBinding = null;

        Dialog dialogs = new Dialog(this);
        editCategoryCustomBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.edit_category_custom, null, false);
        dialogs.setContentView(editCategoryCustomBinding.getRoot());
        dialogs.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogs.setCancelable(false);
        dialogs.show();

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
                    categoryViewModel.insertCategory(nameCategory).observe(CategoryActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            editCategoryCustomBinding.negativeButton.setEnabled(true);
                            editCategoryCustomBinding.positiveButton.setEnabled(true);
                            editCategoryCustomBinding.setIsLoading(false);
                            if (s != null) {
                                if (s.equals("success")) {
                                    Toast.makeText(CategoryActivity.this, "Thêm danh mục thành công", Toast.LENGTH_SHORT).show();
                                    dialogs.dismiss();
                                    categoryViewModel.setIsChangeData();
                                }
                            } else {
                                dialogs.dismiss();
                                Toast.makeText(CategoryActivity.this, "Vui lòng kiểm tra Internet", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên danh mục!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void openActivityAddProduct(String id_cate) {
        Intent intent = new Intent(CategoryActivity.this, ProductDetailActivity.class);
        intent.putExtra(KEY_CATEGORY_ID, id_cate);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITY_REQUEST && resultCode == RESULT_OK && data != null) {
            boolean dataChane = data.getBooleanExtra(DATA_CHANGE, false);
            Log.d(TAG, "onActivityResult: " + dataChane);
            if (dataChane)
                categoryViewModel.setIsChangeData();
        }
    }

    @Override
    public void onItemClick(Category category) {
        Intent intent = new Intent(this, CategoryDetailActivity.class);
        intent.putExtra(KEY_CATEGORY_ID, category.getId());
        intent.putExtra(KEY_CATEGORY_NAME, category.getName());
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onItemClick(String id_pro) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, id_pro);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }

    @Override
    public void onAddItemClick(String id_cate) {
        openActivityAddProduct(id_cate);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_category:
                Toast.makeText(this, "CATEGORY", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_my_fav:
                Toast.makeText(this, "FAV", Toast.LENGTH_SHORT).show();
                break;
        }
        categoryBinding.drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_menu, menu);
        final MenuItem menuItem = menu.findItem(R.id.menu_cart_category);

        View view_badge_cart = menuItem.getActionView();
        actionItemLayoutBinding=CustomActionItemLayoutBinding.bind(view_badge_cart);
        actionItemLayoutBinding.cartBadge.setText(String.valueOf(123));

//        setupBadge();

        view_badge_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(menuItem);
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_cart_category:

                break;
            case R.id.menu_search_category:
                startActivity(new Intent(CategoryActivity.this, SearchProductActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
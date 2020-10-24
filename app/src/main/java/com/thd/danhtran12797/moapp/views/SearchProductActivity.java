package com.thd.danhtran12797.moapp.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jaredrummler.materialspinner.MaterialSpinner;
import com.thd.danhtran12797.moapp.adapters.ProductAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivitySearchProductBinding;
import com.thd.danhtran12797.moapp.models.Product;
import com.thd.danhtran12797.moapp.viewmodels.CategoryViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_PRODUCT_ID;
import static com.thd.danhtran12797.moapp.utils.Constants.REQUEST_CODE_SEARCH_VOICE;

public class SearchProductActivity extends BaseActivity implements ProductAdapter.ProductInterface {

    private static final String TAG = "SearchProductActivity";

    private ActivitySearchProductBinding searchProductBinding;
    private CategoryViewModel categoryViewModel;
    private int total_page = 0;
    private int page = 0;
    private boolean isLoading = false;

    private ProductAdapter productAdapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchProductBinding = ActivitySearchProductBinding.inflate(getLayoutInflater());
        setContentView(searchProductBinding.getRoot());

        setSupportActionBar(searchProductBinding.searchProductToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchProductBinding.searchProductToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        productAdapter = new ProductAdapter(this);
        searchProductBinding.searchRecyclerView.setAdapter(productAdapter);
        searchProductBinding.searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        searchProductBinding.searchRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        gridLayoutManager = (GridLayoutManager) searchProductBinding.searchRecyclerView.getLayoutManager();

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        initAutocompleteSearch();
        eventScrollRecyclerView();

        searchProductBinding.spinner.setItems("Giá từ thấp tới cao", "Giá từ cao xuống thấp");

        searchProductBinding.spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                String query = searchProductBinding.actv.getText().toString();
                if (query.length() != 0) {
                    startSearchProduct();
                } else {
                    searchProductBinding.actv.requestFocus();
                    Toast.makeText(SearchProductActivity.this, "Vui lòng nhập từ khóa tìm kiếm!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        searchProductBinding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchProductBinding.actv.setText("");
            }
        });

        searchProductBinding.imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });
    }

    private void eventScrollRecyclerView() {
        searchProductBinding.searchRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int frist = 0;
                int visible = 0;
                int total_count = 0;

                frist = gridLayoutManager.findFirstVisibleItemPosition();
                visible = gridLayoutManager.getChildCount();
                total_count = gridLayoutManager.getItemCount();

                if (frist + visible >= total_count && isLoading == false && page <= total_page && total_page != 0) {
                    isLoading = true;
                    loadData(searchProductBinding.actv.getText().toString(), searchProductBinding.spinner.getSelectedIndex() + 1, ++page);
                }
            }
        });
    }

    private void getTotalPage(String search_name, int view_type, int page) {
        categoryViewModel.getTotalPage(search_name).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    total_page = Integer.parseInt(s);
                    if (total_page == 0) {
                        showLayoutSearch(true);
                        Toast.makeText(SearchProductActivity.this, "Không tìm thấy sản phẩm!", Toast.LENGTH_SHORT).show();
                    } else
                        loadData(search_name, view_type, page);
                } else
                    Toast.makeText(SearchProductActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData(String search_name, int view_type, int page) {
        // &&total_page!=0
        if (page > total_page) {
            Toast.makeText(this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        searchProductBinding.setIsLoading(true);
        categoryViewModel.getProductFromSearch(search_name, view_type, page).observe(this, new Observer<List<Product>>() {
            @Override
            public void onChanged(List<Product> products) {
                searchProductBinding.setIsLoading(false);
                if (products != null) {
                    showLayoutSearch(false);
                    isLoading = false;
                    productAdapter.addAll(products);
                } else
                    Toast.makeText(SearchProductActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSearchProduct() {
        closeKeyboard();
        page = 0;
        productAdapter.resetListProduct();
        getTotalPage(searchProductBinding.actv.getText().toString(), searchProductBinding.spinner.getSelectedIndex() + 1, ++page);
    }

    private void initAutocompleteSearch() {
//        txtSearch.setThreshold(1); //will start working from first character
//        txtSearch.setDropDownWidth(800);
//        txtSearch.setAdapter(new SearchAdapter(this, android.R.layout.simple_list_item_1));
        searchProductBinding.actv.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        searchProductBinding.actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (v.getText().length() != 0) {
                        startSearchProduct();
                    }
                    return true;
                }
                return false;
            }
        });

        searchProductBinding.actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    searchProductBinding.imgClose.setVisibility(View.VISIBLE);
                    searchProductBinding.imgVoice.setVisibility(View.GONE);
                } else {
                    showLayoutSearch(true);
                    searchProductBinding.imgClose.setVisibility(View.GONE);
                    searchProductBinding.imgVoice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void showLayoutSearch(boolean state) {
        searchProductBinding.layoutSearchProduct.setVisibility(state ? View.VISIBLE : View.GONE);
        searchProductBinding.searchRecyclerView.setVisibility(state ? View.GONE : View.VISIBLE);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hãy nói gì đó");
        try {
            startActivityForResult(intent, REQUEST_CODE_SEARCH_VOICE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(this,
                    "Xin lỗi!! Thiết bị của bạn không hỗ trợ chức năng này",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SEARCH_VOICE && resultCode == RESULT_OK && null != data) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchProductBinding.actv.setText(searchWrd);
                    startSearchProduct();
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(Product product) {
        Intent intent = new Intent(this, ProductDetailActivity.class);
        intent.putExtra(KEY_PRODUCT_ID, product.getId());
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }
}
package com.thd.danhtran12797.moapp.views;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thd.danhtran12797.moapp.adapters.CustomerAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityListCustomerBinding;
import com.thd.danhtran12797.moapp.models.Customer;
import com.thd.danhtran12797.moapp.viewmodels.CustomerViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.thd.danhtran12797.moapp.utils.Constants.ACTIVITY_REQUEST;
import static com.thd.danhtran12797.moapp.utils.Constants.DATA_CHANGE;
import static com.thd.danhtran12797.moapp.utils.Constants.KEY_CUSTOMER;
import static com.thd.danhtran12797.moapp.utils.Constants.REQUEST_CODE_SEARCH_VOICE;

public class CustomerListActivity extends BaseActivity implements CustomerAdapter.CustomerInterface {

    private static final String TAG = "ListCustomerActivity";

    private CustomerViewModel customerViewModel;
    private ActivityListCustomerBinding listCustomerBinding;

    private CustomerAdapter customerAdapter;
    private LinearLayoutManager linearLayoutManager;

    private int totalPage = 0;
    private int page = 1;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listCustomerBinding = ActivityListCustomerBinding.inflate(getLayoutInflater());
        setContentView(listCustomerBinding.getRoot());

        customerAdapter = new CustomerAdapter(this);
        listCustomerBinding.customerRecyclerView.setAdapter(customerAdapter);
        listCustomerBinding.customerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        linearLayoutManager = (LinearLayoutManager) listCustomerBinding.customerRecyclerView.getLayoutManager();

        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);
        customerViewModel.getIsChangeData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d(TAG, "onChanged: " + aBoolean);
                listCustomerBinding.setIsLoading(true);
                page = 1;
                getTotalPage("", page);
            }
        });

        listCustomerBinding.imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listCustomerBinding.actv.setText("");
            }
        });

        listCustomerBinding.imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        listCustomerBinding.addCustomerFloating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(CustomerListActivity.this, CustomerDetailActivity.class), ACTIVITY_REQUEST);
            }
        });

        initAutocompleteSearch();
        eventScrollRecyclerView();
    }

    private void eventScrollRecyclerView() {
        listCustomerBinding.customerRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int frist = 0;
                int visible = 0;
                int total_count = 0;

                frist = linearLayoutManager.findFirstVisibleItemPosition();
                visible = linearLayoutManager.getChildCount();
                total_count = linearLayoutManager.getItemCount();

                if (frist != 0 && frist + visible >= total_count && isLoading == false && page <= totalPage) {
                    isLoading = true;
                    loadData("", ++page);
                }
            }
        });
    }

    private void getTotalPage(String search_name, int page) {
        customerViewModel.getTotalPageCus(search_name).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    totalPage = Integer.parseInt(s);
                    Log.d(TAG, "TOTAL PAGE: " + totalPage);
                    loadData(search_name, page);
                } else {
                    listCustomerBinding.setIsLoading(false);
                    Toast.makeText(CustomerListActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData(String search_name, int page) {
        Log.d(TAG, "PAGE CURRENT: " + page);
        if (page > totalPage) {
//            Toast.makeText(this, "Đã hết dữ liệu", Toast.LENGTH_SHORT).show();
            return;
        }

        listCustomerBinding.setIsLoading(true);
        customerViewModel.getCustomers(search_name, page).observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                listCustomerBinding.setIsLoading(false);
                if (customers != null) {
                    Log.d(TAG, "CUSTOMER SIZE: " + customers.size());
                    isLoading = false;
                    customerAdapter.addAll(customers);
                } else
                    Toast.makeText(CustomerListActivity.this, "Vui lòng kiểm tra Internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startSearchProduct() {
        String nameSearch = listCustomerBinding.actv.getText().toString();
    }

    private void initAutocompleteSearch() {
        listCustomerBinding.actv.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        listCustomerBinding.actv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        listCustomerBinding.actv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    listCustomerBinding.imgClose.setVisibility(View.VISIBLE);
                    listCustomerBinding.imgVoice.setVisibility(View.GONE);
                } else {
                    listCustomerBinding.imgClose.setVisibility(View.GONE);
                    listCustomerBinding.imgVoice.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
                    listCustomerBinding.actv.setText(searchWrd);
                    startSearchProduct();
                }
            }
            return;
        } else if (requestCode == ACTIVITY_REQUEST && resultCode == RESULT_OK && null != data) {
            boolean dataChange = data.getBooleanExtra(DATA_CHANGE, false);
            Log.d(TAG, "onActivityResult: DATA CHANGE" + dataChange);
            if (dataChange) {
                customerAdapter.resetListCustomer();
                customerViewModel.setIsChangeData();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(Customer customer) {
        Intent intent = new Intent(this, CustomerDetailActivity.class);
        intent.putExtra(KEY_CUSTOMER, customer);
        startActivityForResult(intent, ACTIVITY_REQUEST);
    }
}
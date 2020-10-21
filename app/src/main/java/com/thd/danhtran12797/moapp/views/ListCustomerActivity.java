package com.thd.danhtran12797.moapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;

import com.thd.danhtran12797.moapp.adapters.CustomerAdapter;
import com.thd.danhtran12797.moapp.databinding.ActivityListCustomerBinding;
import com.thd.danhtran12797.moapp.models.Customer;
import com.thd.danhtran12797.moapp.viewmodels.CustomerViewModel;

import java.util.List;

public class ListCustomerActivity extends BaseActivity implements CustomerAdapter.CustomerAllInterface {

    private CustomerAdapter customerAdapter;
    private CustomerViewModel customerViewModel;
    private ActivityListCustomerBinding listCustomerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listCustomerBinding = ActivityListCustomerBinding.inflate(getLayoutInflater());
        setContentView(listCustomerBinding.getRoot());

        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        customerAdapter = new CustomerAdapter(customerViewModel.getCustomerList().getValue(), this);
        listCustomerBinding.customerRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        listCustomerBinding.customerRecyclerView.setAdapter(customerAdapter);

        customerViewModel.getCustomerList().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                customerAdapter.submitList(customers);
            }
        });

        customerAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Customer customer) {
                startActivity(new Intent(ListCustomerActivity.this, InforCustomerActivity.class));
            }
        });


        listCustomerBinding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customerAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    listCustomerBinding.addImageView.setVisibility(View.GONE);
                    listCustomerBinding.closeImageView.setVisibility(View.VISIBLE);
                } else {
                    listCustomerBinding.addImageView.setVisibility(View.VISIBLE);
                    listCustomerBinding.closeImageView.setVisibility(View.GONE);
                }
            }
        });

        listCustomerBinding.searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = v.getText().toString();
                    if (text.length() > 0)
                        customerAdapter.getFilter().filter(text);
                    closeKeyboard();
                    return true;
                }
                return false;
            }
        });

        listCustomerBinding.addImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListCustomerActivity.this, InforCustomerActivity.class));
            }
        });

        listCustomerBinding.closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listCustomerBinding.searchEditText.setText("");
                customerAdapter.getFilter().filter("");
                listCustomerBinding.addImageView.setVisibility(View.VISIBLE);
                listCustomerBinding.closeImageView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void update(List<Customer> customers) {
        customerViewModel.update(customers);
    }
}
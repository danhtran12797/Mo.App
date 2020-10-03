package com.thd.danhtran12797.moapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListCustomerActivity extends BaseActivity implements CustomerAdapter.CustomerInterface {

    private EditText editTextSearch;
    private ImageView imageviewAdd;
    private ImageView imageviewClose;
    private RecyclerView recyclerView;
    private CustomerAdapter customerAdapter;
    private CustomerViewModel customerViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        editTextSearch = findViewById(R.id.search_edit_text);
        imageviewAdd = findViewById(R.id.add_image_view);
        imageviewClose = findViewById(R.id.close_image_view);
        recyclerView = findViewById(R.id.customer_recycler_view);

        customerViewModel = new ViewModelProvider(this).get(CustomerViewModel.class);

        customerAdapter = new CustomerAdapter(customerViewModel.getCustomerList().getValue(), this);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(customerAdapter);


        customerViewModel.getCustomerList().observe(this, new Observer<List<Customer>>() {
            @Override
            public void onChanged(List<Customer> customers) {
                customerAdapter.submitList(customers);
            }
        });


        editTextSearch.addTextChangedListener(new TextWatcher() {
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
                    imageviewAdd.setVisibility(View.GONE);
                    imageviewClose.setVisibility(View.VISIBLE);
                } else {
                    imageviewAdd.setVisibility(View.VISIBLE);
                    imageviewClose.setVisibility(View.GONE);
                }
            }
        });

        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        imageviewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListCustomerActivity.this, InforCustomerActivity.class));
            }
        });

        imageviewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editTextSearch.setText("");
                customerAdapter.getFilter().filter("");
                imageviewAdd.setVisibility(View.VISIBLE);
                imageviewClose.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void update(List<Customer> customers) {
        customerViewModel.update(customers);
    }
}
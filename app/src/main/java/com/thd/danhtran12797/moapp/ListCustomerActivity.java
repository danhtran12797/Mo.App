package com.thd.danhtran12797.moapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListCustomerActivity extends AppCompatActivity {

    EditText editTextSearch;
    ImageView imageviewAdd;
    RecyclerView recyclerView;
    ArrayList<Customer> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        editTextSearch = findViewById(R.id.search_edit_text);
        imageviewAdd = findViewById(R.id.add_image_view);
        recyclerView=findViewById(R.id.customer_recycler_view);

        loadData();

        CustomerAdapter adapter=new CustomerAdapter(arrayList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()>0){
                    imageviewAdd.setVisibility(View.GONE);
                }else{
                    imageviewAdd.setVisibility(View.VISIBLE);
                }
            }
        });

        imageviewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ListCustomerActivity.this, InforCustomerActivity.class));
            }
        });
    }

    public void loadData(){
        arrayList=new ArrayList<>();
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
        arrayList.add(new Customer("Danh Trần Hùng", "0958357395", "123 đường 32 phường Bà Trưng", "Viet Nam", "20"));
//        arrayList.add(new Customer("Đặng Văn B", "096355398", "56/1 đường Số 6 phường Hiệp Bình", "Campuchia", "40"));
//        arrayList.add(new Customer("Bùi Thị P", "0325907305", "12/7 đường 32 phường Lê Duẩn", "Lao", "199"));
//        arrayList.add(new Customer("Nguyễn Văn D", "0328367312", "99/100 đường 32 phường Không Tên", "Indonesia", "54"));
//        arrayList.add(new Customer("Đặng Công L", "0328367312", "324/11 đường 32 phường Bình Chánh", "Đông Timor", "96"));
//        arrayList.add(new Customer("Trần Hùng Danh", "0328363212", "44/09 đường 32 phường 32", "Đông Timor", "26"));
//        arrayList.add(new Customer("Trần Thị N", "0334367362", "345 đường 32 phường 21", "Singapore", "100"));
//        arrayList.add(new Customer("Nguyễn thị O", "0368367392", "231 đường 32 phường Hiệp Bình", "Philippines", "56"));
    }
}
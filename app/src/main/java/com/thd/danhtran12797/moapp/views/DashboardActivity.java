package com.thd.danhtran12797.moapp.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.thd.danhtran12797.moapp.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    private ActivityDashboardBinding dashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboardBinding.getRoot());
    }

    public void clickCustomer(View view) {
        startActivity(new Intent(this, CustomerListActivity.class));
    }

    public void clickProduct(View view) {
        startActivity(new Intent(this, CategoryActivity.class));
    }
}
package com.thd.danhtran12797.moapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.thd.danhtran12797.moapp.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity {

    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        activityMainBinding.usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 5 && activityMainBinding.passwordEditText.getText().toString().length() > 5) {
                    activityMainBinding.loginButton.setEnabled(true);
                } else {
                    activityMainBinding.loginButton.setEnabled(false);
                }
            }
        });

        activityMainBinding.passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 5 && activityMainBinding.usernameEditText.getText().toString().length() > 5) {
                    activityMainBinding.loginButton.setEnabled(true);
                } else {
                    activityMainBinding.loginButton.setEnabled(false);
                }
            }
        });
    }

    public void ListCustomer(View view) {
        String username = activityMainBinding.usernameEditText.getText().toString();
        String password = activityMainBinding.passwordEditText.getText().toString();
        if (username.equals("danh123") && password.equals("123456")) {
            startActivity(new Intent(this, CustomerListActivity.class));
            finish();
        } else {
            closeKeyboard();
            activityMainBinding.usernameEditText.setText("");
            activityMainBinding.passwordEditText.setText("");
            activityMainBinding.usernameEditText.requestFocus();
            Toast.makeText(this, "Đăng nhập sai, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }
}
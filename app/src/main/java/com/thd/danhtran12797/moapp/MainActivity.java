package com.thd.danhtran12797.moapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends BaseActivity {
    
    TextInputEditText usernameInputEditText;
    TextInputEditText passwordInputEditText;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameInputEditText = findViewById(R.id.username_edit_text);
        passwordInputEditText = findViewById(R.id.password_edit_text);
        loginButton = findViewById(R.id.login_button);

        usernameInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>5&&passwordInputEditText.getText().toString().length()>5) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        });

        passwordInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>5&&usernameInputEditText.getText().toString().length()>5) {
                    loginButton.setEnabled(true);
                } else {
                    loginButton.setEnabled(false);
                }
            }
        });
    }

    public void ListCustomer(View view) {
        String username=usernameInputEditText.getText().toString();
        String password=passwordInputEditText.getText().toString();
        if (username.equals("danh123") && password.equals("123456")) {
            startActivity(new Intent(this, ListCustomerActivity.class));
            finish();
        }
        else{
            closeKeyboard();
            usernameInputEditText.setText("");
            passwordInputEditText.setText("");
            usernameInputEditText.requestFocus();
            Toast.makeText(this, "Đăng nhập sai, vui lòng thử lại", Toast.LENGTH_SHORT).show();
        }
    }
    
}
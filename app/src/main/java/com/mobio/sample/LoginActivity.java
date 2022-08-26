package com.mobio.sample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.mobio.analytics.client.utility.GpsTracker;
import com.mobio.analytics.client.utility.LogMobio;
import com.mobio.analytics.client.utility.SharedPreferencesUtils;
import com.mobio.analytics.client.utility.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_LOCATION = 1;
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private GpsTracker gpsTracker;

    private TextView tvLong;
    private TextView tvLat;
    private TextView tvAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showCustomUI();
        setContentView(R.layout.activity_login);
        initView();
        addListener();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LogMobio.logD("QuanLA", "orientation "+newConfig.orientation);
    }

    private void initView() {
        btnLogin = findViewById(R.id.btn_login);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        tvLong = findViewById(R.id.tv_long);
        tvLat = findViewById(R.id.tv_lat);
        tvAddress = findViewById(R.id.tv_address);
    }

    private void showCustomUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void addListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (!TextUtils.isEmpty(userName) &&
                        !TextUtils.isEmpty(password) &&
                        isEmailValid(userName)) {
                    SharedPreferencesUtils.editString(LoginActivity.this, SharedPreferencesUtils.M_KEY_USER_NAME, userName);
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    finish();
                } else {
                    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password)) {
                        Toast.makeText(LoginActivity.this, "Mail or password is null", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid mail", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
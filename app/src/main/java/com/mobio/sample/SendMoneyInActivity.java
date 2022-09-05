package com.mobio.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobio.analytics.client.receiver.NotificationDismissedReceiver;

import java.util.StringTokenizer;

public class SendMoneyInActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String ACCOUNT_TO = "account_to";
    public static final String ACCOUNT_NAME_TO = "account_name_to";
    public static final String MONEY_TO = "money_to";
    public static final String CONTENT_TO = "content_to";

    private Button btnContinue;
    private ImageView imvBack;
    private EditText edtAccount;
    private EditText edtMoney;
    private EditText edtContent;
    private TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.WHITE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_send_money_in);
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void init(){
        btnContinue = findViewById(R.id.btn_continue);
        imvBack = findViewById(R.id.imv_back);
        edtAccount = findViewById(R.id.et_account);
        edtMoney = findViewById(R.id.et_amount_money);
        edtContent = findViewById(R.id.et_content);
        tvName = findViewById(R.id.tv_name);

        imvBack.setOnClickListener(this);
        btnContinue.setOnClickListener(this);

        edtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() > 0){
                    tvName.setVisibility(View.VISIBLE);
                }
                else {
                    tvName.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try
                {
                    edtMoney.removeTextChangedListener(this);
                    String value = edtMoney.getText().toString();

                    if (!value.equals(""))
                    {

                        if(value.startsWith(".")){
                            edtMoney.setText("0.");
                        }
                        if(value.startsWith("0") && !value.startsWith("0.")){
                            edtMoney.setText("");

                        }


                        String str = edtMoney.getText().toString().replaceAll(",", "");
                        edtMoney.setText(getDecimalFormattedString(str));
                        edtMoney.setSelection(edtMoney.getText().toString().length());
                    }
                    edtMoney.addTextChangedListener(this);
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                    edtMoney.addTextChangedListener(this);
                }
            }
        });
    }

    public static String getDecimalFormattedString(String value)
    {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1)
        {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt( -1 + str1.length()) == '.')
        {
            j--;
            str3 = ".";
        }
        for (int k = j;; k--)
        {
            if (k < 0)
            {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3)
            {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.imv_back) {
            finish();
        } else if (id == R.id.btn_continue) {
            processContinue();
        }
    }

    public void processContinue(){
        String accountTo = edtAccount.getText().toString();
        String accountName = tvName.getText().toString();
        String money = edtMoney.getText().toString();
        String contentTo = edtContent.getText().toString();

        if (tvName.getVisibility() == View.VISIBLE && !TextUtils.isEmpty(accountTo) && !TextUtils.isEmpty(accountName)
                && !TextUtils.isEmpty(money) && !TextUtils.isEmpty(contentTo)) {
            Intent intent = new Intent(SendMoneyInActivity.this, ConfirmTransferActivity.class);
            intent.putExtra(ACCOUNT_TO, accountTo);
            intent.putExtra(ACCOUNT_NAME_TO, accountName);
            intent.putExtra(MONEY_TO, money);
            intent.putExtra(CONTENT_TO, contentTo);

            startActivity(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
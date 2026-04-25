package com.haoyinrui.intenttestapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etUrl;
    private EditText etSendText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUrl = findViewById(R.id.et_url);
        etSendText = findViewById(R.id.et_send_text);
        Button btnOpenWeb = findViewById(R.id.btn_open_web);
        Button btnSendData = findViewById(R.id.btn_send_data);

        btnOpenWeb.setOnClickListener(v -> openWebPage());
        btnSendData.setOnClickListener(v -> sendTextData());
    }

    private void openWebPage() {
        String url = etUrl.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            showToast(R.string.toast_url_required);
            etUrl.requestFocus();
            return;
        }

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) == null) {
            showToast(R.string.toast_browser_not_found);
            return;
        }

        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            showToast(R.string.toast_browser_not_found);
        }
    }

    private void sendTextData() {
        String sendText = etSendText.getText().toString().trim();
        if (TextUtils.isEmpty(sendText)) {
            showToast(R.string.toast_send_required);
            etSendText.requestFocus();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, sendText);

        if (intent.resolveActivity(getPackageManager()) == null) {
            showToast(R.string.toast_share_not_found);
            return;
        }

        try {
            startActivity(Intent.createChooser(intent, getString(R.string.chooser_send_title)));
        } catch (ActivityNotFoundException e) {
            showToast(R.string.toast_share_not_found);
        }
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}

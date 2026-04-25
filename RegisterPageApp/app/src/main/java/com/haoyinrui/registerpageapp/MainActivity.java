package com.haoyinrui.registerpageapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private EditText etName;
    private EditText etEmail;
    private EditText etPwd;
    private RadioGroup rgSex;
    private RadioButton rbBoy;
    private RadioButton rbGirl;
    private CheckBox cbSing;
    private CheckBox cbDance;
    private CheckBox cbRead;
    private Button btnSubmit;
    private String selectedSex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews() {
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPwd = findViewById(R.id.et_pwd);
        rgSex = findViewById(R.id.rg_sex);
        rbBoy = findViewById(R.id.rb_boy);
        rbGirl = findViewById(R.id.rb_girl);
        cbSing = findViewById(R.id.cb_sing);
        cbDance = findViewById(R.id.cb_dance);
        cbRead = findViewById(R.id.cb_read);
        btnSubmit = findViewById(R.id.btn_submit);
    }

    private void initListeners() {
        btnSubmit.setOnClickListener(this);
        rbBoy.setOnCheckedChangeListener(this);
        rbGirl.setOnCheckedChangeListener(this);
        cbSing.setOnCheckedChangeListener(this);
        cbDance.setOnCheckedChangeListener(this);
        cbRead.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_submit) {
            submitRegister();
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int viewId = buttonView.getId();
        if (viewId == R.id.rb_boy && isChecked) {
            selectedSex = getString(R.string.sex_boy);
        } else if (viewId == R.id.rb_girl && isChecked) {
            selectedSex = getString(R.string.sex_girl);
        } else if (!rbBoy.isChecked() && !rbGirl.isChecked()) {
            selectedSex = "";
        }
    }

    private void submitRegister() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPwd.getText().toString().trim();
        String hobbies = getSelectedHobbies();

        if (TextUtils.isEmpty(name)) {
            showToast(R.string.toast_name_empty);
            return;
        }

        if (TextUtils.isEmpty(email)) {
            showToast(R.string.toast_email_empty);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast(R.string.toast_password_empty);
            return;
        }

        if (rgSex.getCheckedRadioButtonId() == -1 || TextUtils.isEmpty(selectedSex)) {
            showToast(R.string.toast_sex_empty);
            return;
        }

        if (TextUtils.isEmpty(hobbies)) {
            showToast(R.string.toast_hobby_empty);
            return;
        }

        String result = getString(R.string.toast_register_success, name, email, selectedSex, hobbies);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    private String getSelectedHobbies() {
        List<String> hobbyList = new ArrayList<>();

        if (cbSing.isChecked()) {
            hobbyList.add(getString(R.string.hobby_sing));
        }
        if (cbDance.isChecked()) {
            hobbyList.add(getString(R.string.hobby_dance));
        }
        if (cbRead.isChecked()) {
            hobbyList.add(getString(R.string.hobby_read));
        }

        return TextUtils.join("、", hobbyList);
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }
}

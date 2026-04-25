package cn.itcast.directory;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText etName;
    private EditText etPhone;
    private TextView tvResult;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        initView();
        refreshAllContacts();
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        tvResult = findViewById(R.id.tv_result);

        Button btnAdd = findViewById(R.id.btn_add);
        Button btnQuery = findViewById(R.id.btn_query);
        Button btnUpdate = findViewById(R.id.btn_update);
        Button btnDelete = findViewById(R.id.btn_delete);

        btnAdd.setOnClickListener(view -> addContact());
        btnQuery.setOnClickListener(view -> queryContact());
        btnUpdate.setOnClickListener(view -> updateContact());
        btnDelete.setOnClickListener(view -> deleteContact());
    }

    private void addContact() {
        String name = getInputText(etName);
        String phone = getInputText(etPhone);

        if (name.isEmpty() || phone.isEmpty()) {
            showToast(R.string.toast_name_phone_empty);
            return;
        }

        try {
            if (dbHelper.contactExists(name)) {
                showToast(R.string.toast_contact_exists);
                return;
            }

            long rowId = dbHelper.addContact(name, phone);
            if (rowId > 0) {
                showToast(R.string.toast_add_success);
                clearInput();
                refreshAllContacts();
            } else {
                showToast(R.string.toast_add_failed);
            }
        } catch (Exception exception) {
            showToast(R.string.toast_add_failed);
        }
    }

    private void queryContact() {
        String name = getInputText(etName);
        try {
            boolean hasResult = showContacts(name);
            if (!hasResult) {
                if (name.isEmpty()) {
                    showToast(R.string.toast_no_contacts);
                } else {
                    showToast(R.string.toast_contact_not_found);
                }
            }
        } catch (Exception exception) {
            showToast(R.string.toast_query_failed);
        }
    }

    private void updateContact() {
        String name = getInputText(etName);
        String phone = getInputText(etPhone);

        if (name.isEmpty() || phone.isEmpty()) {
            showToast(R.string.toast_update_empty);
            return;
        }

        try {
            int rows = dbHelper.updateContact(name, phone);
            if (rows > 0) {
                showToast(R.string.toast_update_success);
                clearInput();
                refreshAllContacts();
            } else {
                showToast(R.string.toast_update_failed);
            }
        } catch (Exception exception) {
            showToast(R.string.toast_update_failed);
        }
    }

    private void deleteContact() {
        String name = getInputText(etName);
        if (name.isEmpty()) {
            showToast(R.string.toast_name_empty);
            return;
        }

        try {
            int rows = dbHelper.deleteContact(name);
            if (rows > 0) {
                showToast(R.string.toast_delete_success);
                clearInput();
                refreshAllContacts();
            } else {
                showToast(R.string.toast_delete_failed);
            }
        } catch (Exception exception) {
            showToast(R.string.toast_delete_failed);
        }
    }

    private boolean showContacts(String name) {
        StringBuilder builder = new StringBuilder();

        try (Cursor cursor = dbHelper.queryContacts(name)) {
            while (cursor.moveToNext()) {
                int nameIndex = cursor.getColumnIndexOrThrow(DBHelper.COLUMN_NAME);
                int phoneIndex = cursor.getColumnIndexOrThrow(DBHelper.COLUMN_PHONE);
                String contactName = cursor.getString(nameIndex);
                String contactPhone = cursor.getString(phoneIndex);
                if (builder.length() > 0) {
                    builder.append("\n");
                }
                builder.append(getString(R.string.result_name_prefix))
                        .append(contactName)
                        .append("    ")
                        .append(getString(R.string.result_phone_prefix))
                        .append(contactPhone);
            }
        }

        if (builder.length() == 0) {
            tvResult.setText(getString(R.string.result_empty));
            return false;
        }

        tvResult.setText(builder.toString().trim());
        return true;
    }

    private String getInputText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void clearInput() {
        etName.setText("");
        etPhone.setText("");
    }

    private void refreshAllContacts() {
        try {
            showContacts("");
        } catch (Exception exception) {
            tvResult.setText(getString(R.string.result_empty));
        }
    }

    private void showToast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) {
            dbHelper.close();
        }
    }
}

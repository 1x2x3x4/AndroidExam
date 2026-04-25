package cn.itcast.broadcastreceiverdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    public static final String ACTION_MY_BROADCAST =
            "cn.itcast.broadcastreceiverdemo.MY_BROADCAST";

    private String broadcastContent = "";
    private MyBroadcastReceiver receiver;
    private EditText etMessage;
    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnSend = findViewById(R.id.btn_send);
        Button btnShow = findViewById(R.id.btn_show);
        etMessage = findViewById(R.id.et_message);
        tvContent = findViewById(R.id.tv_content);

        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter(ACTION_MY_BROADCAST);
        ContextCompat.registerReceiver(
                this,
                receiver,
                filter,
                ContextCompat.RECEIVER_NOT_EXPORTED
        );

        btnSend.setOnClickListener(v -> sendMyBroadcast());
        btnShow.setOnClickListener(v -> showBroadcastContent());
    }

    private void sendMyBroadcast() {
        String message = etMessage.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            Toast.makeText(this, R.string.input_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(ACTION_MY_BROADCAST);
        intent.setPackage(getPackageName());
        intent.putExtra("message", message);
        sendBroadcast(intent);
        Toast.makeText(this, R.string.broadcast_sent, Toast.LENGTH_SHORT).show();
    }

    private void showBroadcastContent() {
        if (TextUtils.isEmpty(broadcastContent)) {
            tvContent.setText(R.string.content_empty);
        } else {
            tvContent.setText(broadcastContent);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ACTION_MY_BROADCAST.equals(intent.getAction())) {
                String message = intent.getStringExtra("message");
                broadcastContent = message == null ? "" : message;
            }
        }
    }
}

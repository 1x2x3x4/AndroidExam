package cn.itcast.canteenbroadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public static final String ACTION_FOOD_BROADCAST = "cn.itcast.canteenbroadcast.FOOD_BROADCAST";

    private TextView tvMessage;
    private MyBroadcastReceiver receiver;
    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMessage = findViewById(R.id.tv_message);
        ImageView ivHorn = findViewById(R.id.iv_horn);

        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_FOOD_BROADCAST);
        registerFoodReceiver(filter);

        ivHorn.setOnClickListener(v -> {
            Intent intent = new Intent(ACTION_FOOD_BROADCAST);
            intent.setPackage(getPackageName());
            intent.putExtra("message", getString(R.string.message_food_ready));
            sendBroadcast(intent);
            Toast.makeText(MainActivity.this, R.string.broadcast_sent, Toast.LENGTH_SHORT).show();
        });
    }

    private void registerFoodReceiver(IntentFilter filter) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(receiver, filter);
        }
        isReceiverRegistered = true;
    }

    @Override
    protected void onDestroy() {
        if (isReceiverRegistered) {
            unregisterReceiver(receiver);
            isReceiverRegistered = false;
        }
        super.onDestroy();
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !ACTION_FOOD_BROADCAST.equals(intent.getAction())) {
                return;
            }

            String message = intent.getStringExtra("message");
            if (message != null && !message.trim().isEmpty()) {
                tvMessage.setText(message);
            }
        }
    }
}

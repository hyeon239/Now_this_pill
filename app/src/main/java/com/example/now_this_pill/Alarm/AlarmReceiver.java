package com.example.now_this_pill.Alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String pillName = intent.getStringExtra("pillName");

        Intent serviceIntent = new Intent(context, NotificationService.class);
        serviceIntent.putExtra("title", "복용할 시간이에요!");
        serviceIntent.putExtra("message", pillName+ " 복용하세요");
        NotificationService.enqueueWork(context, serviceIntent);
    }
}

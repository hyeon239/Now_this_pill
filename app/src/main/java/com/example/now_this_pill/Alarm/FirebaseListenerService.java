package com.example.now_this_pill.Alarm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseListenerService extends Service {
    private DatabaseReference databaseRef;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String userId = intent.getStringExtra("userId");
        if (userId != null) {
            databaseRef = FirebaseDatabase.getInstance().getReference()
                    .child("FirebaseEmailAccount")
                    .child("userAccount")
                    .child(userId);

            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChild("M1")) {
                        sendNotification("복용하지 않았어요!", "약을 복용해 주세요", 1);
                    }
                    if (dataSnapshot.hasChild("M2")) {
                        sendNotification("외출하기 버튼이 눌렸어요", "다음 스케줄 복용약이 패스됩니다", 2);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle possible errors.
                }
            });
        }
        return START_STICKY;
    }

    private void sendNotification(String title, String message, int notificationId) {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        serviceIntent.putExtra("title", title);
        serviceIntent.putExtra("message", message);
        serviceIntent.putExtra("notificationId", notificationId);
        NotificationService.enqueueWork(this, serviceIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseRef != null) {
            databaseRef.removeEventListener((ValueEventListener) this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

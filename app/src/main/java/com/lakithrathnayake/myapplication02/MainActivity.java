package com.lakithrathnayake.myapplication02;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    String msg;
    private RelativeLayout.LayoutParams layoutParams;
    Button b1;

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        img = findViewById(R.id.imageView);
//
//        img.setOnLongClickListener(v -> {
//            ClipData.Item item = new ClipData.Item((CharSequence) v.getTag());
//            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
//
//            ClipData dragData = new ClipData(v.getTag().toString(), mimeTypes, item);
//            View.DragShadowBuilder myShadow = new View.DragShadowBuilder(img);
//
//            v.startDrag(dragData, myShadow, null, 0);
//            return true;
//        });
//
//        img.setOnDragListener(new View.OnDragListener() {
//            @Override
//            public boolean onDrag(View v, DragEvent event) {
//                switch(event.getAction()) {
//                    case DragEvent.ACTION_DRAG_STARTED:
//                        layoutParams = (RelativeLayout.LayoutParams)v.getLayoutParams();
//                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_STARTED");
//
//                        // Do nothing
//                        break;
//
//                    case DragEvent.ACTION_DRAG_ENTERED:
//                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENTERED");
//                        int x_cord;
//                        int y_cord;
//                        break;
//
//                    case DragEvent.ACTION_DRAG_EXITED :
//                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_EXITED");
//                        x_cord = (int) event.getX();
//                        y_cord = (int) event.getY();
//                        layoutParams.leftMargin = x_cord;
//                        layoutParams.topMargin = y_cord;
//                        v.setLayoutParams(layoutParams);
//                        break;
//
//                    case DragEvent.ACTION_DRAG_LOCATION  :
//                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_LOCATION");
//                        x_cord = (int) event.getX();
//                        y_cord = (int) event.getY();
//                        break;
//
//                    case DragEvent.ACTION_DRAG_ENDED   :
//                        Log.d(msg, "Action is DragEvent.ACTION_DRAG_ENDED");
//
//                        // Do nothing
//                        break;
//
//                    case DragEvent.ACTION_DROP:
//                        Log.d(msg, "ACTION_DROP event");
//
//                        // Do nothing
//                        break;
//                    default: break;
//                }
//                return true;
//            }
//        });
//
//        img.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    ClipData data = ClipData.newPlainText("", "");
//                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(img);
//
//                    img.startDrag(data, shadowBuilder, img, 0);
//                    img.setVisibility(View.INVISIBLE);
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });

//        b1 = findViewById(R.id.button);
//        b1.setOnClickListener(v -> {
//            addNotification();
//        });

        try {
            if(ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        btnShowLocation = findViewById(R.id.button);

        btnShowLocation.setOnClickListener(v -> {
            gps = new GPSTracker(MainActivity.this);

            if(gps.canGetLocation()) {
                double latitude = gps.getLatitude();
                double longitude = gps.getLongitude();

                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
                        + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else gps.showSettingsAlert();
        });

    }

//    private void addNotification() {
//        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder;
//
//        // Create a notification channel for Android 8.0+
//        String channelId = "example_channel_id";
//        CharSequence channelName = "Example Channel";
//        String channelDescription = "Channel for test notifications";
//        int importance = NotificationManager.IMPORTANCE_DEFAULT;
//        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
//        channel.setDescription(channelDescription);
//        manager.createNotificationChannel(channel);
//
//        // Associate the channel with the builder
//        builder = new NotificationCompat.Builder(this, channelId);
//
//        builder.setSmallIcon(R.drawable.abc)
//                .setContentTitle("Notification Example")
//                .setContentText("This is a test notification");
//
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//        builder.setContentIntent(contentIntent);
//
//        manager.notify(0, builder.build());
//    }
}
package com.lakithrathnayake.myapplication02;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ImageView img;
    String msg;
    private RelativeLayout.LayoutParams layoutParams;
    Button b1,b2,b3,b4;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;

    Button btnShowLocation;
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    GPSTracker gps;
    double latitude = 0;
    double longitude = 0;

    private static final int REQUEST_IMAGE_CAPTURE = 1;


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

//        try {
//            if(ActivityCompat.checkSelfPermission(this, mPermission) != PackageManager.PERMISSION_GRANTED) {
//               ActivityCompat.requestPermissions(this, new String[]{mPermission}, REQUEST_CODE_PERMISSION);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//        btnShowLocation = findViewById(R.id.button);
//
//        btnShowLocation.setOnClickListener(v -> {
//            gps = new GPSTracker(MainActivity.this);
//
//            if(gps.canGetLocation()) {
//                latitude = gps.getLatitude();
//                longitude = gps.getLongitude();
//
//                Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                        + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
//                addNotification();
//            } else gps.showSettingsAlert();
//        });

//        b1 = findViewById(R.id.button);
//        b2 = findViewById(R.id.button2);
//        b3 = findViewById(R.id.button3);
//        b4 = findViewById(R.id.button4);
//
//        BA = BluetoothAdapter.getDefaultAdapter();
//        lv = findViewById(R.id.listView);
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
//        }
//
//        Button buttonCamera = findViewById(R.id.buttonOpenCamera);
//        buttonCamera.setOnClickListener(v -> {
//            openCamera();
//        });

    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                // Get the captured image
                Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                // You can now use the imageBitmap, e.g., display it in an ImageView
            }
        }
    }

    public void on(View v) {
        if(!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Already on",Toast.LENGTH_LONG).show();
        }
    }

    public void off (View v) {
        BA.disable();
        Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
    }

    public  void visible(View v){
        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
    }

    public void list(View v) {
        pairedDevices = BA.getBondedDevices();

        ArrayList list = new ArrayList();
        for (BluetoothDevice bt :
                pairedDevices) {
            list.add(bt.getName());
        }
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_item, list);
        lv.setAdapter(adapter);
    }

    private void addNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder;

        // Create a notification channel for Android 8.0+
        String channelId = "example_channel_id";
        CharSequence channelName = "Example Channel";
        String channelDescription = "Channel for test notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription(channelDescription);
        manager.createNotificationChannel(channel);

        // Associate the channel with the builder
        builder = new NotificationCompat.Builder(this, channelId);

        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Your Location")
                .setContentText("Your Location is - Lat: "
                        + latitude + ", Long: " + longitude);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(contentIntent);

        manager.notify(0, builder.build());
    }
}
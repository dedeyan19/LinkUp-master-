package com.jamburger.linkup.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jamburger.linkup.R;
import com.jamburger.linkup.fragments.HomeFragment;
import com.jamburger.linkup.fragments.ProfileFragment;
import com.jamburger.linkup.fragments.SearchFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    FirebaseUser user;
    String currentPage;
    Button button;

    @Override
    public void onBackPressed() {
        if (currentPage.equals("HOME")) {
            super.onBackPressed();
        } else {
            currentPage = "HOME";
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    void startFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        user = FirebaseAuth.getInstance().getCurrentUser();

        String page = getIntent().getStringExtra("page");
        if (page != null) {
            if (page.equals("PROFILE")) {
                currentPage = "PROFILE";
                bottomNavigationView.setSelectedItemId(R.id.nav_profile);
            }
        } else {
            currentPage = "HOME";
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    currentPage = "HOME";
                    break;
                case R.id.nav_search:
                    currentPage = "SEARCH";
                    break;
                case R.id.nav_profile:
                    currentPage = "PROFILE";
                    break;
            }
            startFragment(currentPage);
            return true;
        });
        startFragment(currentPage);

        /notification section
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotification();
            }
        });
    }
    private void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.messageicon) //set icon for notification
                        .setContentTitle("Notifications Example") // set icon for notification
                        .setContentText("This is a notification message")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
        Intent notificationIntent = new Intent(this, NotificationView.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void startFragment(String fragment) {
        Fragment frag = null;
        switch (fragment) {
            case "PROFILE":
                frag = new ProfileFragment();
                currentPage = "PROFILE";
                break;
            case "SEARCH":
                frag = new SearchFragment();
                currentPage = "SEARCH";
                break;
            case "HOME":
                frag = new HomeFragment();
                currentPage = "HOME";
                break;
        }
        if (frag != null) startFragment(frag);
    }
}
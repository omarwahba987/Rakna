package com.example.omar.rakna;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.AdminHome;
import com.example.omar.rakna.HomePages.SuperVisiorHome;
import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.RegisterPages.UserRegister;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

public class SplashScreen extends AppCompatActivity {
    private static int splash_time = 3000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        final DatabaseReference[] database = new DatabaseReference[1];


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isMapsEnabled() && isServicesOK()) {
                    String login = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("login", "no");

                    if (login.equals("yes")) {
                        String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        database[0] = FirebaseDatabase.getInstance().getReference().child("Accounts").child(id);
                        database[0].addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String type = dataSnapshot.child("type").getValue().toString();
                                if (type.equals("user")) {
                                    Intent intent = new Intent(SplashScreen.this, UserHome.class);
                                    startActivity(intent);
                                    finish();
                                } else if (type.equals("admin")) {
                                    Intent intent = new Intent(SplashScreen.this, AdminHome.class);
                                    intent.putExtra("email", FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                    intent.putExtra("password", PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("pass", " "));


                                    startActivity(intent);
                                    finish();
                                } else if (type.equals("supervisor")) {
                                    Intent intent = new Intent(SplashScreen.this, SuperVisiorHome.class);
                                    startActivity(intent);
                                    finish();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }


                    if (login.equals("no")) {
                        Intent intent = new Intent(SplashScreen.this, Root.class);
                        startActivity(intent);
                        finish();
                    }
                }
                if (!isMapsEnabled() || !isServicesOK()) {

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.exit(0);

                        }
                    }, 2000);

                }


            }
        }, splash_time);


    }

    public boolean isServicesOK() {

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashScreen.this);

        if (available == ConnectionResult.SUCCESS) {
            //everything is fine and the user can make map requests
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            //an error occured but we can resolve it
            Toast.makeText(this, "please instal google services and open app agin", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "please enable location  and open app agin", Toast.LENGTH_SHORT).show();
            return false;


        }
        return true;
    }


}

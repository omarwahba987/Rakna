package com.example.omar.rakna.HomePages;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.omar.rakna.GarageList;
import com.example.omar.rakna.Login;
import com.example.omar.rakna.PreGarageList;
import com.example.omar.rakna.R;
import com.example.omar.rakna.RegisterPages.UserRegister;
import com.example.omar.rakna.ReservationStatues;
import com.example.omar.rakna.Root;
import com.example.omar.rakna.Users.Garage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserHome extends AppCompatActivity {
Button signout,goGarage,myReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        signout=findViewById(R.id.signoutuser);
        myReservation=findViewById(R.id.gotoreservationBtn);
        goGarage=findViewById(R.id.gotoGarageListBtn);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent= new Intent(UserHome.this,Root.class);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("login", "no").apply();
                startActivity(intent);
                finish();
            }
        });

        goGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserHome.this, PreGarageList.class);
                startActivity(intent);


            }
        });
        myReservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(UserHome.this, ReservationStatues.class);
                startActivity(intent);
                finish();


            }
        });



    }
}

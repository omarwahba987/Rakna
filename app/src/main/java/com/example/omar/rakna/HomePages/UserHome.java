package com.example.omar.rakna.HomePages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.omar.rakna.GarageList;
import com.example.omar.rakna.Login;
import com.example.omar.rakna.R;
import com.example.omar.rakna.RegisterPages.UserRegister;
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
Button signout,goGarage;
    DatabaseReference data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);
        signout=findViewById(R.id.signoutuser);
        goGarage=findViewById(R.id.gotoGarageListBtn);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent= new Intent(UserHome.this,Root.class);
                startActivity(intent);
                finish();
            }
        });

        goGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data = FirebaseDatabase.getInstance().getReference().child("Garages");
                final ArrayList<Garage> garageList=new ArrayList<>();


                data.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot Garages : dataSnapshot.getChildren()) {
                            Garage g = Garages.getValue(Garage.class);
                            garageList.add(g);

                        }
                        Intent intent= new Intent(UserHome.this, GarageList.class);
                        startActivity(intent);


                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
}

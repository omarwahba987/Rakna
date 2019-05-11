package com.example.omar.rakna.HomePages;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.omar.rakna.CarComeInList;
import com.example.omar.rakna.CarInList;
import com.example.omar.rakna.CarOutList;
import com.example.omar.rakna.PreGarageList;
import com.example.omar.rakna.R;
import com.example.omar.rakna.Reservation;
import com.example.omar.rakna.ReservationStatues;
import com.example.omar.rakna.Root;
import com.example.omar.rakna.Users.Garage;
import com.example.omar.rakna.Users.Supervisor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SuperVisiorHome extends AppCompatActivity {
    Button signout ,carsing,carcheckout,carcomein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visior_home);
        signout=findViewById(R.id.signoutSupervisroBtn);
        carsing=findViewById(R.id.carinGarageBtn);
        carcheckout=findViewById(R.id.carOutBtn);
        carcomein=findViewById(R.id.carInBtn);
        final String[] gId = new String[1];


        //**********************
        final DatabaseReference[] database = new DatabaseReference[1];
        database[0] = FirebaseDatabase.getInstance().getReference("Accounts").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        database[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Supervisor g=  dataSnapshot.getValue(Supervisor.class);
                gId[0] =g.getGarageId();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //**********************


        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent= new Intent(SuperVisiorHome.this, Root.class);
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().putString("login", "no").apply();
                startActivity(intent);
                finish();
            }
        });

        // to approve car come into the garage
        carcomein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference database;
                database = FirebaseDatabase.getInstance().getReference("Reservation");
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot Re : dataSnapshot.getChildren()) {
                            Reservation g = Re.getValue(Reservation.class);
                            if (g.isExspired()) {
                                g.remove();

                            }
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent= new Intent(SuperVisiorHome.this, CarComeInList.class);
                intent.putExtra("gid",gId[0]);
                startActivity(intent);

            }
        });

        // to see list of users want to check out
        carcheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(SuperVisiorHome.this, CarOutList.class);
                intent.putExtra("gid",gId[0]);
                startActivity(intent);
            }
        });

        // to see list of cars in the garage
        carsing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SuperVisiorHome.this, CarInList.class);
                intent.putExtra("gid",gId[0]);
                startActivity(intent);
            }
        });
    }
}

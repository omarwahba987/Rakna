package com.example.omar.rakna;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.UserHome;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;

public class PreGarageList extends AppCompatActivity {
    EditText Snum,address;
    Button myLocationBtn,addressLocationBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_garage_list);
        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("Reservation");
        final boolean[] flagReserved = {false};
        final boolean[] found = {false};

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Re : dataSnapshot.getChildren()) {
                    Reservation g = Re.getValue(Reservation.class);
                    if (g.isExspired()) {
                        g.remove();

                    }
                }
                for (DataSnapshot Re : dataSnapshot.getChildren()) {
                    Reservation g = Re.getValue(Reservation.class);
                    if (g.getuId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        found[0] = true;
                        if(!g.isExspired())
                        {
                            flagReserved[0] =true;
                        }

                        break;

                    }
                }
                if (found[0]&&flagReserved[0]) {
                    Toast.makeText(PreGarageList.this, "you have a resevation ", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(PreGarageList.this, ReservationStatues.class);
                    startActivity(in);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Snum=findViewById(R.id.RNumberSpaces);
        address=findViewById(R.id.addressNgarages);
        myLocationBtn=findViewById(R.id.nGmyLocationBtn);
        addressLocationBtn=findViewById(R.id.addressNLocationBtn);

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Snum.getText().toString().trim().isEmpty()) {
                    Snum.setError("enter spaces number you want");
                    Snum.requestFocus();
                    return;
                }
                final int sNum = Integer.valueOf(Snum.getText().toString().trim()) ;
                Intent intent= new Intent(PreGarageList.this,GarageList.class);

                intent.putExtra("sNum",sNum);
                intent.putExtra("type","myLocation");
                startActivity(intent);
                finish();



            }
        });
        addressLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Snum.getText().toString().trim().isEmpty()) {
                    Snum.setError("enter spaces number you want");
                    Snum.requestFocus();
                    return;
                }
                if (address.getText().toString().trim().isEmpty()) {
                    address.setError("enter address");
                    address.requestFocus();
                    return;
                }
                final int sNum = Integer.valueOf(Snum.getText().toString().trim()) ;
                final String addres=address.getText().toString().trim();
                Intent intent= new Intent(PreGarageList.this,GarageList.class);

                intent.putExtra("sNum",sNum);
                intent.putExtra("address",addres);
                intent.putExtra("type","address");
                startActivity(intent);
                finish();



            }


        });

    }
}

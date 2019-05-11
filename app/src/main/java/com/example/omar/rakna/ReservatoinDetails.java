package com.example.omar.rakna;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.omar.rakna.Users.Garage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ReservatoinDetails extends AppCompatActivity {
    String garageName,garageId,garageAddress,pinCode;
    int garageHourPrice,sNum;
    DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservatoin_details);
        Intent intent=getIntent();
        garageName=intent.getStringExtra("garagename");
        garageId=intent.getStringExtra("garageid");
        garageAddress=intent.getStringExtra("garageaddress");
        garageHourPrice=  intent.getIntExtra("garagehourprice",0);
        sNum=  intent.getIntExtra("sNum",0);
        pinCode=generatePin();
        while (!checkPin(pinCode))
        {
            pinCode=generatePin();
        }
        Date intime= Calendar.getInstance().getTime();
        Date outtime= Calendar.getInstance().getTime();
        database = FirebaseDatabase.getInstance().getReference("Reservation");
        String Rid = database.push().getKey();
        Reservation reservation=new Reservation(Rid,FirebaseAuth.getInstance().getCurrentUser().getUid(),garageId,
                "onResponse",garageHourPrice,pinCode,intime,outtime,sNum,garageName,garageAddress,intime);
        database.child(Rid).setValue(reservation);

        database = FirebaseDatabase.getInstance().getReference("Garages").child(garageId);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Garage g=  dataSnapshot.getValue(Garage.class);
                g.reserved(sNum);
                database = FirebaseDatabase.getInstance().getReference("Garages");
                database.child(garageId).setValue(g);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Intent in= new Intent(ReservatoinDetails.this,ReservationStatues.class);
        startActivity(in);
        finish();




        







    }


    public String generatePin()
    {
        int max = (int) Math.pow(10,4) - 1;
        int min = (int) Math.pow(10, 3);
        int range = max-min;
        Random r = new Random();
        int x = r.nextInt(range);
        int pin = x+min;
        return String.valueOf(pin) ;
    }

    public boolean checkPin(final String pin)
    {
        DatabaseReference data;
        data = FirebaseDatabase.getInstance().getReference().child("Reservation");
        final ArrayList<String> pinList=new ArrayList<>();
        final boolean[] result = {true};

        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot Reservation : dataSnapshot.getChildren()) {
                    Reservation g = Reservation.getValue(Reservation.class);
                    pinList.add(g.getPin());

                }
                if(pinList.size()>0)
                {
                    if(pinList.contains(pin))
                    {
                        result[0] =false;
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservatoinDetails.this, "failed to resieve pin list", Toast.LENGTH_SHORT).show();

            }
        });


        return result[0];
    }
}

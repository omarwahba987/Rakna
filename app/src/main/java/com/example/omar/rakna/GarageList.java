package com.example.omar.rakna;

import android.Manifest;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.omar.rakna.RegisterPages.AddGarage;
import com.example.omar.rakna.RegisterPages.SuperVisorRegister;
import com.example.omar.rakna.Users.Garage;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Semaphore;

public class GarageList extends AppCompatActivity {
    FusedLocationProviderClient mfusedLocationProviderClient;
    DatabaseReference data;
    GarageListAdapter adapter ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_list);
        final ListView garageListView=findViewById(R.id.garageList);
        final Double[] latitude = new Double[1];
        final Double[] longitude = new Double[1];
        final GarageListAdapter[] adapter = new GarageListAdapter[1];


        //****************************************************************************
        Intent in=getIntent();
        String type=in.getStringExtra("type");
        final int sNum=  in.getIntExtra("sNum",0);
        //****************************************************************************
        if (type.equals("myLocation"))
        {
            mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GarageList.this);
            if (ActivityCompat.checkSelfPermission(GarageList.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GarageList.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(GarageList.this, "missing permissions", Toast.LENGTH_SHORT).show();
                return;
            }
            mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        latitude[0] = location.getLatitude();
                        longitude[0] = location.getLongitude();
                        Toast.makeText(GarageList.this, "device location dedected", Toast.LENGTH_SHORT).show();

                    }

                }
            });

        }

        //*****************************************************************************
        if (type.equals("address"))
        {
            String address=in.getStringExtra("address");
            Geocoder coder = new Geocoder(this);
            List<Address> addresses;
            try {
                addresses = coder.getFromLocationName(address,5);
                Address location=addresses.get(0);
                latitude[0] = location.getLatitude();
                longitude[0] = location.getLongitude();
                Toast.makeText(GarageList.this, "address dedected", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }




        //******************************************************************


        //******************************************************************

        data = FirebaseDatabase.getInstance().getReference().child("Garages");
        final ArrayList<Garage> garageList=new ArrayList<>();
        final ArrayList<Garage> garageSortedList=new ArrayList<>();



        data.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                garageList.clear();
                garageSortedList.clear();

                for (DataSnapshot Garages : dataSnapshot.getChildren()) {
                     Garage g = Garages.getValue(Garage.class);
                    garageList.add(g);

                }

                for (int i=0;i<garageList.size();i++)
                {
                    double x=0.0;
                     x= getDistancBetweenTwoPoints(garageList.get(i).getLatitude(), garageList.get(i).getLongitude(), latitude[0], longitude[0]);
                    Integer y= (int) x;

                    garageList.get(i).setDistance(y);
                }

                Collections.sort(garageList);
                if(garageList.size()<=10)
                {
                    for (int i=0;i<garageList.size();i++)
                    {
                        garageSortedList.add(garageList.get(i));
                    }
                }
                else
                {
                    for (int i=0;i<10;i++)
                    {
                        garageSortedList.add(garageList.get(i));
                    }
                }
                Collections.sort(garageSortedList);
                adapter[0] = new GarageListAdapter(getApplicationContext(),R.layout.garage_cell,garageSortedList);
                garageListView.setAdapter(adapter[0]);







            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        garageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(GarageList.this, garageSortedList.get(position).getName(), Toast.LENGTH_SHORT).show();
                if(garageSortedList.get(position).getNumOfFreeSpaces()>=sNum)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GarageList.this);

                    builder.setTitle("confirm");
                    builder.setMessage("reserve in this garage");

                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {


                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent= new Intent(GarageList.this,ReservatoinDetails.class);
                            intent.putExtra("garagename",garageSortedList.get(position).getName());
                            intent.putExtra("garageid",garageSortedList.get(position).getId());
                            intent.putExtra("garageaddress",garageSortedList.get(position).getAddress());
                            intent.putExtra("garagehourprice",garageSortedList.get(position).getHourPrice());
                            intent.putExtra("sNum",sNum);
                            startActivity(intent);
                            finish();






                        }
                    });

                    builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else
                {
                    Toast.makeText(GarageList.this,"this garage has not spaces suit you", Toast.LENGTH_SHORT).show();

                }


            }
        });







    }
    private double getDistancBetweenTwoPoints(double lat1,double lon1,double lat2,double lon2) {

        float[] distance = new float[2];

        Location.distanceBetween( lat1, lon1,
                lat2, lon2, distance);
        double x =distance[0];
        if(x<5.0)
        {
            return 5.0;
        }

        return x;
    }
}

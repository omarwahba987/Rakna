package com.example.omar.rakna.RegisterPages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.omar.rakna.HomePages.AdminHome;
import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.R;
import com.example.omar.rakna.Users.Garage;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;

public class AddGarage extends AppCompatActivity {
    EditText editTextgname, editTextgAddress, editTextNumSpaces, editTextHourPrice;
    Button mylocation, locationMap, add;
    FusedLocationProviderClient mfusedLocationProviderClient;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_garage);

        editTextgname = findViewById(R.id.garageName);
        editTextgAddress = findViewById(R.id.garageAddress);
        editTextNumSpaces = findViewById(R.id.numberSpaces);
        editTextHourPrice = findViewById(R.id.hourPrice);
        mylocation = findViewById(R.id.garageMylocation);
        locationMap = findViewById(R.id.garageLocationMap);
        add = findViewById(R.id.addGarageDataBtn);

        final Double[] latitude = new Double[1];
        final Double[] longitude = new Double[1];

        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationMap.setEnabled(false);
                mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(AddGarage.this);
                if (ActivityCompat.checkSelfPermission(AddGarage.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(AddGarage.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(AddGarage.this, "missing permissions", Toast.LENGTH_SHORT).show();
                    return;
                }
                mfusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location location = task.getResult();
                            latitude[0] = location.getLatitude();
                            longitude[0] = location.getLongitude();
                            Toast.makeText(AddGarage.this, "location set", Toast.LENGTH_SHORT).show();


                        }

                    }
                });
            }
        });

        locationMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mylocation.setEnabled(false);


                final String address = editTextgAddress.getText().toString().trim();
                if (address.isEmpty()) {
                    editTextgAddress.setError("enter garage Address");
                    editTextgAddress.requestFocus();
                    return;
                }
                Geocoder geocoder = new Geocoder(AddGarage.this);
                try {
                    List<Address> list = geocoder.getFromLocationName(address, 1);
                    Address location = list.get(0);
                    latitude[0] = location.getLatitude();
                    longitude[0] = location.getLongitude();
                    Toast.makeText(AddGarage.this, "location set", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (editTextNumSpaces.getText().toString().isEmpty()) {
                    editTextNumSpaces.setError("enter garage spaces number");
                    editTextNumSpaces.requestFocus();
                    return;
                }
                if (editTextHourPrice.getText().toString().isEmpty()) {
                    editTextHourPrice.setError("enter garage hour price");
                    editTextHourPrice.requestFocus();
                    return;
                }
                final String name = editTextgname.getText().toString().trim();
                final String address = editTextgAddress.getText().toString().trim();
                final String x = editTextNumSpaces.getText().toString();
                final int numOfSpaces = Integer.parseInt(x);
                final int numOfFreeSpaces = numOfSpaces;
                final int hourPrice = Integer.parseInt(editTextHourPrice.getText().toString());
                if (name.isEmpty()) {
                    editTextgname.setError("enter garage name");
                    editTextgname.requestFocus();
                    return;
                }
                if (address.isEmpty()) {
                    editTextgAddress.setError("enter garage Address");
                    editTextgAddress.requestFocus();
                    return;
                }

                if (latitude[0] == null || longitude[0] == null) {
                    Toast.makeText(AddGarage.this, "choose location button", Toast.LENGTH_SHORT).show();
                    return;
                }
                database = FirebaseDatabase.getInstance().getReference("Garages");
                String id = database.push().getKey();

                Garage garage = new Garage(id, name, address, numOfSpaces, numOfFreeSpaces, hourPrice, latitude[0], longitude[0], 1000);
                database.child(id).setValue(garage);

                Toast.makeText(AddGarage.this, "Garage added", Toast.LENGTH_SHORT).show();
                Intent z = getIntent();
                final String a = z.getStringExtra("email");
                final String b = z.getStringExtra("password");
                Intent intent = new Intent(AddGarage.this, AdminHome.class);
                intent.putExtra("email", a);
                intent.putExtra("password", b);
                startActivity(intent);
                finish();

            }
        });


    }
}

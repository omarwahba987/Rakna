package com.example.omar.rakna;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.SuperVisiorHome;
import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.Users.Garage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class CarComeInList extends AppCompatActivity {
ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_come_in_list);
        Intent in=getIntent();
        final String gId=in.getStringExtra("gid");
        listView=findViewById(R.id.carcomeinList);
        final ArrayList<String>listID = new ArrayList<>();
        final ArrayList<String>PinList = new ArrayList<>();

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("Reservation");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               // listID.clear();
                //PinList.clear();
                for (DataSnapshot Re : dataSnapshot.getChildren()) {
                    Reservation g = Re.getValue(Reservation.class);
                    if (g.getType().equals("onResponse")&&g.getgId().equals(gId)) {
                        listID.add(g.id);
                        PinList.add(g.getPin());

                    }
                }
                final ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,PinList){

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view =super.getView(position, convertView, parent);

                        TextView textView=(TextView) view.findViewById(android.R.id.text1);

                        /*YOUR CHOICE OF COLOR*/
                        textView.setTextColor(Color.BLACK);

                        return view;
                    }
                };
                listView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CarComeInList.this, "internet connection failed", Toast.LENGTH_SHORT).show();
            }
        });






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {



                final DatabaseReference[] database = new DatabaseReference[1];
                database[0] = FirebaseDatabase.getInstance().getReference("Reservation").child(listID.get(position));
                database[0].addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Reservation g=  dataSnapshot.getValue(Reservation.class);
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarComeInList.this);

                        builder.setTitle("confirn car get in");
                        String s1="pin number >>"+g.getPin();
                        String s2="number of cars reserver >>"+g.getsNum();
                        builder.setMessage(s1+"\n"+s2);

                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int which) {
                                g.setType("ingarage");
                                Calendar cal = Calendar.getInstance();
                                g.setInTime(cal.getTime());
                                database[0] = FirebaseDatabase.getInstance().getReference("Reservation");
                                database[0].child(listID.get(position)).setValue(g);
                                Intent in = new Intent(CarComeInList.this, SuperVisiorHome.class);
                                startActivity(in);
                                finish();


                            }

                        });

                        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });





    }
}

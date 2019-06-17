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

import com.example.omar.rakna.HomePages.SuperVisiorHome;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CarInList extends AppCompatActivity {
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_in_list);
        listView=findViewById(R.id.caringarageList);
        Intent in=getIntent();
        final String gId=in.getStringExtra("gid");
        final ArrayList<String>listID = new ArrayList<>();
        final ArrayList<String>PinList = new ArrayList<>();

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference("Reservation");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //listID.clear();
                //PinList.clear();
                for (DataSnapshot Re : dataSnapshot.getChildren()) {
                    Reservation g = Re.getValue(Reservation.class);
                    if (g.getType().equals("ingarage")&&g.getgId().equals(gId)) {
                        listID.add(g.getId());
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

            }
        });






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");



                final DatabaseReference[] database = new DatabaseReference[1];
                database[0] = FirebaseDatabase.getInstance().getReference("Reservation").child(listID.get(position));
                database[0].addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Reservation g=  dataSnapshot.getValue(Reservation.class);
                        AlertDialog.Builder builder = new AlertDialog.Builder(CarInList.this);

                        builder.setTitle("confirn car get out and pay");
                        String s1="pin number >>"+g.getPin();
                        String s2="number of cars reserver >>"+g.getsNum();
                        String s3="hour price >>"+g.gethPrice();
                        String s4="car(s) came in at >>"+df.format(g.getInTime()) ;
                        Calendar call = Calendar.getInstance();
                        g.setOutTime(call.getTime());
                        String s5="car(s) came out at >>"+df.format(g.getOutTime()) ;
                        String s6="cash required>> "+g.calcMoney()+" LE.";


                        builder.setMessage(s1+"\n"+s2+"\n"+s3+"\n"+s4+"\n"+s5+"\n"+s6);

                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {


                            public void onClick(DialogInterface dialog, int which) {
                                g.setType("payment");
                                database[0] = FirebaseDatabase.getInstance().getReference("Reservation");
                                database[0].child(listID.get(position)).setValue(g);
                                g.remove();
                                Intent in = new Intent(CarInList.this, SuperVisiorHome.class);
                                startActivity(in);
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });



    }
}

package com.example.omar.rakna;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.Users.Garage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ReservationStatues extends AppCompatActivity {
    TextView t1, t2, t3, t4, t5, t6, t7, t8;
    Button getDirection, cancelRequest, checkOut;
    final Reservation reservation = null;
    DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_statues);
        t1 = findViewById(R.id.reservationStatues1);
        t2 = findViewById(R.id.reservationStatues2);
        t3 = findViewById(R.id.reservationStatues3);
        t4 = findViewById(R.id.reservationStatues4);
        t5 = findViewById(R.id.reservationStatues5);
        t6 = findViewById(R.id.reservationStatues6);
        t7 = findViewById(R.id.reservationStatues7);
        t8 = findViewById(R.id.reservationStatues8);
        getDirection=findViewById(R.id.reservationStatuesgetdBtn);
        cancelRequest=findViewById(R.id.reservationStatuescancelBtn);
        checkOut=findViewById(R.id.reservationStatuesoutBtn);

        final Reservation[] gg = new Reservation[1];
        final boolean[] found = {false};
        database = FirebaseDatabase.getInstance().getReference("Reservation");


        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");

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
                        gg[0] =g;
                        if(g.isExspired())
                        {
                            Toast.makeText(ReservationStatues.this, "your resevation is expired ", Toast.LENGTH_LONG).show();
                            Intent in = new Intent(ReservationStatues.this, UserHome.class);
                            startActivity(in);
                            finish();
                        }

                        if (g.getType().equals("onResponse")) {
                            checkOut.setEnabled(false);
                            cancelRequest.setEnabled(true);
                            t1.setText("Reservation status ");
                            t2.setText("you reserve "+g.getsNum()+" space(s)");
                            t3.setText("at garage  "+g.gname);
                            t4.setText("at "+df.format(g.getRtime().getTime()));
                            t5.setText("this garage hour price is "+g.gethPrice()+" LE.");
                            t6.setText("its address "+g.gaddress);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(g.getRtime());
                            cal.add(Calendar.MINUTE, 20);
                            t7.setText("your reservation expire at "+df.format(cal.getTime()));
                            t8.setText("your uniqe pin code is >> "+g.getPin());


                        }
                        else if (g.getType().equals("ingarage")) {
                            cancelRequest.setEnabled(false);
                            checkOut.setEnabled(true);
                            t1.setText("your car(s) save now ");
                            t2.setText("in garage "+g.getGname());
                            t3.setText("at "+g.getGaddress());
                            t4.setText("you entered the garage at "+df.format(g.getInTime()));
                            t5.setText("and you reserved "+g.getsNum()+" space(s)");
                            t6.setText("with hour price "+g.gethPrice()+" LE.");
                            t7.setText("your uniqe pin code is >> "+g.getPin());
                            t8.setText("if you want to get out just press checkout button!");

                        }
                        else if (g.getType().equals("checkout")) {
                            cancelRequest.setEnabled(false);
                            checkOut.setEnabled(false);
                            t1.setText("you now on your way to leave ");
                            t2.setText("garage "+g.getGname());
                            t3.setText("you entered the garage at "+df.format(g.getInTime()));
                            t4.setText("you leave the garage at "+df.format(g.getOutTime()));
                            t5.setText("you reserved "+g.getsNum()+" space(s)");
                            t6.setText("whith hour price "+g.gethPrice()+" LE.");
                            t7.setText("you shoud pay approximately >> "+g.calcMoney() +" LE.");
                            t8.setText("your uniqe pin code is >> "+g.getPin());

                        }
                        else if (g.getType().equals("payment")) {
                            cancelRequest.setEnabled(false);
                            checkOut.setEnabled(false);
                            t1.setText("you now on your way to leave ");
                            t2.setText("garage"+g.getGname());
                            t3.setText("you entered the garage at "+df.format(g.getInTime()));
                            t4.setText("you leave the garage at "+df.format(g.getOutTime()));
                            t5.setText("you reserved "+g.getsNum()+" space(s)");
                            t6.setText("whith hour price "+g.gethPrice()+" LE.");
                            t7.setText("you shoud pay approximately >> "+g.calcMoney() +" LE.");
                            t8.setText("your uniqe pin code is >> "+g.getPin());

                        }
                        break;

                    }
                }
                if (!found[0]) {
                    Toast.makeText(ReservationStatues.this, "there is no resevation ", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(ReservationStatues.this, UserHome.class);
                    startActivity(in);
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ReservationStatues.this, "some thing went rong", Toast.LENGTH_LONG).show();
                Intent in = new Intent(ReservationStatues.this, UserHome.class);
                startActivity(in);
                finish();

            }
        });

        cancelRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gg[0].remove();
                Toast.makeText(ReservationStatues.this, "reservation canceled", Toast.LENGTH_LONG).show();
                Intent in = new Intent(ReservationStatues.this, UserHome.class);
                startActivity(in);
                finish();

            }
        });


        getDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] URL = {"https://www.google.com/maps/dir/?api=1&destination="};
                URL[0]=URL[0]+gg[0].getGaddress().trim();


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL[0]));
                startActivity(intent);
            }
        });



        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference[] database = new DatabaseReference[1];
                database[0] = FirebaseDatabase.getInstance().getReference("Reservation").child(gg[0].id);
                database[0].addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Reservation g=  dataSnapshot.getValue(Reservation.class);
                        g.setType("checkout");
                        Date now=Calendar.getInstance().getTime();
                        g.setOutTime(now);
                        database[0] = FirebaseDatabase.getInstance().getReference("Reservation");
                        database[0].child(gg[0].id).setValue(g);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });







    }
}

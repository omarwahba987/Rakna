package com.example.omar.rakna;

import android.support.annotation.NonNull;

import com.example.omar.rakna.Users.Garage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Format;
import java.util.Calendar;
import java.util.Date;

public class Reservation {

    String id,uId,gId,type,gname,gaddress,pin;
    int hPrice,sNum;
    Date inTime,outTime,Rtime;

    public Reservation() {
    }

    public Reservation(String id, String uId, String gId, String type, int hPrice, String pin, Date inTime, Date outTime,int sNum,
                       String gname,String gaddress,Date Rtime) {
        this.id = id;
        this.uId = uId;
        this.gId = gId;
        this.type = type;
        this.hPrice = hPrice;
        this.pin = pin;
        this.inTime = inTime;
        this.outTime = outTime;
        this.sNum=sNum;
        this.gname=gname;
        this.gaddress=gaddress;
        this.Rtime=Rtime;
    }

    public String getGname() {
        return gname;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public String getGaddress() {
        return gaddress;
    }

    public void setGaddress(String gaddress) {
        this.gaddress = gaddress;
    }

    public Date getRtime() {
        return Rtime;
    }

    public void setRtime(Date rtime) {
        Rtime = rtime;
    }

    public int getsNum() {
        return sNum;
    }

    public void setsNum(int sNum) {
        this.sNum = sNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int gethPrice() {
        return hPrice;
    }

    public void sethPrice(int hPrice) {
        this.hPrice = hPrice;
    }

    public String getPin() {
        return String.valueOf(pin);
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public boolean isExspired()
    {
        Date now=Calendar.getInstance().getTime();
        long diff= now.getTime()-Rtime.getTime();
        diff=diff/(60*1000)%60;
        if(diff>20&&type.equals("onResponse"))
        {
            return true;
        }
        return false;
    }

    public int calcMoney ()
    {

        Date now=Calendar.getInstance().getTime();
        double diff= outTime.getTime()-inTime.getTime();
        diff=diff/(60*60*1000);
        int res= (int) (diff*hPrice*sNum);
        return res;
    }

    public void remove()
    {
        final DatabaseReference[] database = new DatabaseReference[1];
        database[0] = FirebaseDatabase.getInstance().getReference("Garages").child(gId);
        database[0].addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Garage g=  dataSnapshot.getValue(Garage.class);
                g.out(sNum);
                database[0] = FirebaseDatabase.getInstance().getReference("Garages");
                database[0].child(gId).setValue(g);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        database[0] = FirebaseDatabase.getInstance().getReference("Reservation");
        database[0].child(id).removeValue();



    }
}

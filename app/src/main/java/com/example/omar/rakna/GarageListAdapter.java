package com.example.omar.rakna;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.omar.rakna.Users.Garage;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GarageListAdapter extends ArrayAdapter<Garage> {
    Context context;
    int resource;
    ArrayList<Garage> glist;

    public GarageListAdapter(@NotNull Context context, @LayoutRes int resource, @NotNull ArrayList<Garage> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.glist = objects;
    }

    class  viewHolder {
        TextView gname;
        TextView gaddress;
        TextView gfreespaces;
        TextView ghourprice;
        TextView gdistance;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(resource ,null);
        viewHolder holder = new viewHolder();

        holder.gname = convertView.findViewById(R.id.garageCellName);
        holder.gaddress = convertView.findViewById(R.id.garageCellAddress);
        holder.gfreespaces =convertView.findViewById(R.id.garageCellFreeSpaces);
        holder.ghourprice = convertView.findViewById(R.id.garageCellhourPrice);
        holder.gdistance = convertView.findViewById(R.id.garageCellDistance);



        holder.gname.setText("Garage name >>"+glist.get(position).getName());
        holder.gaddress.setText("Garage Address >>"+glist.get(position).getAddress());
        holder.gfreespaces.setText("Garage free spaces number >>"+glist.get(position).getNumOfFreeSpaces());
        holder.ghourprice.setText("Garage hour price in >>"+glist.get(position).getHourPrice()+" LE.");
        holder.gdistance.setText("Garage far from here >> "+glist.get(position).getDistance()+" m");



        return convertView;
    }




}

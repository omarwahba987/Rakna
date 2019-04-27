package com.example.omar.rakna.HomePages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.omar.rakna.Login;
import com.example.omar.rakna.R;
import com.example.omar.rakna.RegisterPages.AddGarage;
import com.example.omar.rakna.RegisterPages.AdminRegister;
import com.example.omar.rakna.RegisterPages.SuperVisorRegister;
import com.example.omar.rakna.Root;
import com.google.firebase.auth.FirebaseAuth;

public class AdminHome extends AppCompatActivity {
private Button addadmin , signout, addgarage ,addSupervisor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        addadmin=findViewById(R.id.addAdmin);
        signout=findViewById(R.id.signoutadmin);
        addgarage=findViewById(R.id.addGarageBtn);
        addSupervisor=findViewById(R.id.addSupervisroBtn);
        addadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminHome.this,AdminRegister.class);
                Intent x = getIntent();
                 String a = x.getStringExtra("email");
                 String b = x.getStringExtra("password");
                intent.putExtra("email",a);
                intent.putExtra("password",b);
                startActivity(intent);
                finish();
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent= new Intent(AdminHome.this,Root.class);
                startActivity(intent);
                finish();
            }
        });
        addgarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminHome.this, AddGarage.class);
                Intent x = getIntent();
                String a = x.getStringExtra("email");
                String b = x.getStringExtra("password");
                intent.putExtra("email",a);
                intent.putExtra("password",b);
                startActivity(intent);
                finish();

            }
        });
        addSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(AdminHome.this, SuperVisorRegister.class);
                Intent x = getIntent();
                String a = x.getStringExtra("email");
                String b = x.getStringExtra("password");
                intent.putExtra("email",a);
                intent.putExtra("password",b);
                startActivity(intent);
                finish();
            }
        });
    }
}

package com.example.omar.rakna.HomePages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.omar.rakna.R;
import com.example.omar.rakna.Root;
import com.google.firebase.auth.FirebaseAuth;

public class SuperVisiorHome extends AppCompatActivity {
    Button signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visior_home);
        signout=findViewById(R.id.signoutSupervisroBtn);
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();

                Intent intent= new Intent(SuperVisiorHome.this, Root.class);
                startActivity(intent);
                finish();
            }
        });
    }
}

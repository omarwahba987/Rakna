package com.example.omar.rakna.RegisterPages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.AdminHome;
import com.example.omar.rakna.R;
import com.example.omar.rakna.Users.Admin;
import com.example.omar.rakna.Users.Garage;
import com.example.omar.rakna.Users.Supervisor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuperVisorRegister extends AppCompatActivity {
    private EditText editTextname,editTextemail,editTextpass;
    Spinner gSpinner;

    Button addSupervisor;
    DatabaseReference data;
     ArrayList<Garage>garageList = new ArrayList<>();
     ArrayList<String>garageNameList = new ArrayList<>();
    private FirebaseAuth mAuth;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visor_register);
        mAuth = FirebaseAuth.getInstance();
        gSpinner= findViewById(R.id.garageSpinner);
        addSupervisor=findViewById(R.id.addSupervisroDataBtn);
        editTextname=findViewById(R.id.nameSupervisor);
        editTextemail=findViewById(R.id.emailSupervisor);
        editTextpass=findViewById(R.id.passSupervisor);
        final String[] gid = new String[1];
        final String[] gname = new String[1];


        data = FirebaseDatabase.getInstance().getReference().child("Garages");
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                garageList.clear();
                garageNameList.clear();
                for(DataSnapshot Garages:dataSnapshot.getChildren())
                {
                    Garage g=Garages.getValue(Garage.class);
                    garageList.add(g);
                    garageNameList.add(g.getName());
                }
                ArrayAdapter <String>adapter=new ArrayAdapter<String>(SuperVisorRegister.this,android.R.layout.simple_spinner_dropdown_item,garageNameList);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                gSpinner.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        gSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                gname[0] =garageList.get(position).getName();
                gid[0] =garageList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                addSupervisor.setEnabled(false);

            }
        });

        addSupervisor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextname.getText().toString().trim();
                final String email = editTextemail.getText().toString().trim();
                String password = editTextpass.getText().toString().trim();
                if (name.isEmpty()) {
                    editTextname.setError("enter your name");
                    editTextname.requestFocus();
                    return;
                }

                if (email.isEmpty()) {
                    editTextemail.setError("enter your email");
                    editTextemail.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    editTextemail.setError("please enter a valid email");
                    editTextemail.requestFocus();
                    return;
                }
                if (password.isEmpty()) {
                    editTextpass.setError("enter the password");
                    editTextpass.requestFocus();
                    return;
                }
                if (password.length() < 6) {
                    editTextpass.setError("enter password longer than 6 characters");
                    editTextpass.requestFocus();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        { String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            Supervisor supervisor=new Supervisor(id,name,email,gname[0],gid[0],"supervisor");
                            FirebaseDatabase.getInstance().getReference("Accounts").child(id).setValue(supervisor);
                            Toast.makeText(SuperVisorRegister.this, "super visor added", Toast.LENGTH_LONG).show();

                            FirebaseAuth.getInstance().signOut();
                            Intent x = getIntent();
                            final String a = x.getStringExtra("email");
                            final String b = x.getStringExtra("password");

                            mAuth.signInWithEmailAndPassword(
                                    a,b)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Intent intent= new Intent(SuperVisorRegister.this, AdminHome.class);
                                            intent.putExtra("email",a);
                                            intent.putExtra("password",b);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });

                        }
                        else
                        {
                            Toast.makeText(SuperVisorRegister.this, "add failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });




            }
        });



    }
}

package com.example.omar.rakna;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.AdminHome;
import com.example.omar.rakna.HomePages.SuperVisiorHome;
import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.RegisterPages.UserRegister;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    private EditText editTextemail,editTextpass;
    private Button login;
TextView register;
    DatabaseReference database;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        register = findViewById(R.id.registerBtn);
        editTextemail=findViewById(R.id.emailLogin);
        editTextpass=findViewById(R.id.passLogin);
        login=findViewById(R.id.loginBtn);
        mAuth = FirebaseAuth.getInstance();



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Login.this,UserRegister.class);
                startActivity(intent);
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = editTextemail.getText().toString().trim();
                final String password = editTextpass.getText().toString().trim();

                if (email.isEmpty()) {
                    editTextemail.setError("enter the email");
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


                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            String id= FirebaseAuth.getInstance().getCurrentUser().getUid();
                            database = FirebaseDatabase.getInstance().getReference().child("Accounts").child(id);
                            database.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String type= dataSnapshot.child("type").getValue().toString();
                                    if (type.equals("user"))
                                    {
                                        Intent intent= new Intent(Login.this,UserHome.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (type.equals("admin"))
                                    {
                                        Intent intent= new Intent(Login.this,AdminHome.class);
                                        intent.putExtra("email",email);
                                        intent.putExtra("password",password);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else if (type.equals("supervisor"))
                                    {
                                        Intent intent= new Intent(Login.this,SuperVisiorHome.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });



                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}

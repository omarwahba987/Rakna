package com.example.omar.rakna.RegisterPages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.omar.rakna.HomePages.AdminHome;
import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.R;
import com.example.omar.rakna.Users.Admin;
import com.example.omar.rakna.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class AdminRegister extends AppCompatActivity {
private EditText editTextemail,editTextpass;
private Button add;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_register);
        editTextemail= findViewById(R.id.registerAdminEmail);
        editTextpass= findViewById(R.id.registerAdminpass);
        add = findViewById(R.id.registerAdminAddBtn);
        mAuth = FirebaseAuth.getInstance();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = editTextemail.getText().toString().trim();
                String password = editTextpass.getText().toString().trim();

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
                            Admin admin=new Admin(id,email,"admin");
                            FirebaseDatabase.getInstance().getReference("Accounts").child(id).setValue(admin);
                            Toast.makeText(AdminRegister.this, "admin added", Toast.LENGTH_LONG).show();

                            FirebaseAuth.getInstance().signOut();
                             Intent x = getIntent();
                            final String a = x.getStringExtra("email");
                            final String b = x.getStringExtra("password");

                            mAuth.signInWithEmailAndPassword(
                                    a,b)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    Intent intent= new Intent(AdminRegister.this,AdminHome.class);
                                    intent.putExtra("email",a);
                                    intent.putExtra("password",b);
                                    startActivity(intent);
                                    finish();
                                }
                            });

                        }
                        else
                        {
                            Toast.makeText(AdminRegister.this, "add failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });


    }
}

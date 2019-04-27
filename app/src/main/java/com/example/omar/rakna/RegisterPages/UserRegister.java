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

import com.example.omar.rakna.HomePages.UserHome;
import com.example.omar.rakna.R;
import com.example.omar.rakna.Root;
import com.example.omar.rakna.SplashScreen;
import com.example.omar.rakna.Users.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity {
private EditText editTextname,editTextemail,editTextpass,editTextphone;
private Button register;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);


        register=findViewById(R.id.registerUserBtn);
        editTextname=findViewById(R.id.registrationName);
        editTextemail=findViewById(R.id.registrationEmail);
        editTextpass=findViewById(R.id.registrationpassword);
        editTextphone=findViewById(R.id.registrationPhone);
        mAuth = FirebaseAuth.getInstance();




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextname.getText().toString().trim();
                final String email = editTextemail.getText().toString().trim();
                 String password = editTextpass.getText().toString().trim();
                final String phone = editTextphone.getText().toString().trim();



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

                if (phone.isEmpty()) {
                    editTextphone.setError("enter your phone number");
                    editTextphone.requestFocus();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        { String id=FirebaseAuth.getInstance().getCurrentUser().getUid();
                            User user=new User(id,email,name,phone,"user");
                           FirebaseDatabase.getInstance().getReference("Accounts").child(id).setValue(user);
                            Toast.makeText(UserRegister.this, "user added", Toast.LENGTH_LONG).show();
                            Intent intent= new Intent(UserRegister.this,UserHome.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });





            }
        });


    }
}

package com.example.shashanksinha.chatappytv1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username_et, password_et, email_et;
    Button register_btn;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    Toolbar toolbar;
    private ProgressDialog reg_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        username_et = findViewById(R.id.username_et);
        password_et = findViewById(R.id.password_et);
        email_et = findViewById(R.id.email_et);
        register_btn = findViewById(R.id.register_btn);

        // Firebase Stuff...
        firebaseAuth = FirebaseAuth.getInstance();
        reg_progress = new ProgressDialog(this);


        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_et.getText().toString();
                String email = email_et.getText().toString();
                String password = password_et.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){

                    Toast.makeText(RegisterActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password must be atleast 6 characters long.", Toast.LENGTH_SHORT).show();
                } else{
                    register(username,email,password);
                }
            }
        });

    }

    public void register(final String username, String email, String password)
    {
        // Creating new user...
        reg_progress.setTitle("Creating New Account");
        reg_progress.setMessage("Please wait while we create your new account !");
        reg_progress.setCanceledOnTouchOutside(false);
        reg_progress.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashmap = new HashMap<>();
                            hashmap.put("id",userid);
                            hashmap.put("username",username);
                            hashmap.put("imageURL","default");
                            hashmap.put("status","offline");
                            hashmap.put("search",username.toLowerCase());



                            databaseReference.setValue(hashmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Cannot register with this email id!!!", Toast.LENGTH_SHORT).show();
                        }

                        reg_progress.dismiss();

                    }
                });
    }
}

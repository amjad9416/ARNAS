package com.example.arna1;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.arna1.databinding.ActivityMainBinding;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class hospital_login extends AppCompatActivity {
    FirebaseAuth f1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.manager_login);
        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }

        f1=FirebaseAuth.getInstance();
        Button b=(Button)findViewById(R.id.loginbtn);
        Button b1=(Button)findViewById(R.id.createbtn);
        Button b3=(Button)findViewById(R.id.fp1);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText lt1 = (EditText) findViewById(R.id.lt1);
                EditText lt2 = (EditText) findViewById(R.id.lt2);
                String user = lt1.getText().toString();
                String pass = lt2.getText().toString();
                if (TextUtils.isEmpty(lt1.getText().toString())) {
                    lt1.setError("Enter Email id");

                } else if (TextUtils.isEmpty(lt2.getText().toString())) {
                    lt2.setError("Enter Password");

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(user).matches())
                {
                    lt1.setError("Invalid Email");

                }
                else {
                    f1.signInWithEmailAndPassword(user, pass)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(hospital_login.this, "Success", Toast.LENGTH_SHORT).show();
                                    ;
                                    startActivity(new Intent(hospital_login.this, hospital_details.class));
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(hospital_login.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                                    ;
                                }
                            });

                }
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            startActivity(new Intent(hospital_login.this,hospital_register.class));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText lt1 = (EditText) findViewById(R.id.lt1);
                EditText lt2 = (EditText) findViewById(R.id.lt2);
                lt2.setText("");

                String email = lt1.getText().toString();
                if (TextUtils.isEmpty(lt1.getText().toString())) {
                    lt1.setError("Enter Email to Send Reset Link");
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    lt1.setError("Invalid Email");

                }
                else {
                    f1.sendPasswordResetEmail(email)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(hospital_login.this, "Email sent", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(hospital_login.this, "invalid email", Toast.LENGTH_SHORT).show();
                                    ;
                                }
                            });


                }
            }
        });


    }
}
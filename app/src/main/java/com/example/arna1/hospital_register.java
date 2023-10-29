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

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.FirebaseFirestore;


 public class hospital_register extends AppCompatActivity {

    FirebaseAuth f;
    FirebaseFirestore f1;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.hospital_register);
        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }

        Button b=(Button)findViewById(R.id.button4);
        Button b1=(Button)findViewById(R.id.login1);
        f=FirebaseAuth.getInstance();
        f1=FirebaseFirestore.getInstance();
       // CharSequence email = t6.getText().toString().trim();
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(hospital_register.this,hospital_login.class));
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText t = (EditText) findViewById(R.id.t1);

                EditText t6 = (EditText) findViewById(R.id.t6);
                EditText t7 = (EditText) findViewById(R.id.t7);
                CharSequence t66 = t6.getText().toString().trim();
                String user = t6.getText().toString();
                String password = t7.getText().toString();

                if (TextUtils.isEmpty(t.getText().toString())) {
                    t.setError("Enter Username");

                } else if (TextUtils.isEmpty(t6.getText().toString())) {
                    t6.setError("Enter Email Id");


                } else if (TextUtils.isEmpty(t7.getText().toString())) {
                    t7.setError("Enter Password");

                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(t66).matches())
                {
                    t6.setError("Invalid Email");
                }
                else if(t7.getText().toString().length()<=8)
                {
                    t7.setError("Must be Min 8 characters");

                }
                else if(t7.getText().toString().length()>=16)
                {
                    t7.setError("exceeded 16 characters");

                }





                    else {

                    f.createUserWithEmailAndPassword(user, password)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    Toast.makeText(hospital_register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(hospital_register.this, hospital_login.class);
                                    startActivity(i);


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(hospital_register.this, "Something Went Wrong Try Again!!", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
            }
        });



    }

}

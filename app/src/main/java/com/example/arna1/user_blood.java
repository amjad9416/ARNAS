package com.example.arna1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.arna1.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class user_blood extends AppCompatActivity  {

    public String storedItem;
    DatabaseReference file ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        }
        // catch block to handle NullPointerException
        catch (NullPointerException e) {
        }


        setContentView(R.layout.user_find);
        Spinner s=(Spinner)findViewById(R.id.spinner);
        file= FirebaseDatabase.getInstance().getReference("UserDetails");

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                storedItem = selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                }
        });

        Button find=(Button)findViewById(R.id.button3);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (storedItem.equals("Select Blood Group")) {
                    Toast.makeText(user_blood.this, "Select Your Blood Group", Toast.LENGTH_SHORT).show();

                }

                /*else {


                    Intent intent = new Intent(user_blood.this, MapsActivity.class);
                    intent.putExtra("selectedItem", storedItem);
                    startActivity(intent);
                }*/
                else {
                    Query query = file.orderByChild("blood").equalTo(storedItem);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Intent intent = new Intent(user_blood.this, MapsActivity.class);
                                intent.putExtra("selectedItem", storedItem);
                                startActivity(intent);

                            } else {
                                Toast.makeText(user_blood.this,"No Donor Found",Toast.LENGTH_LONG).show();


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }


}
package com.example.arna1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_get extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    madapter madapter;
    ArrayList<userr> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_get);
        recyclerView=findViewById(R.id.userlist);
        Toast.makeText(user_get.this,"decfrcgf",Toast.LENGTH_SHORT).show();
        database= FirebaseDatabase.getInstance().getReference("users");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<>();
        madapter=new madapter(this,list);
        recyclerView.setAdapter(madapter);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             for (DataSnapshot dataSnapshot :snapshot.getChildren())
             {
                 userr userr=dataSnapshot.getValue(com.example.arna1.userr.class);
                 list.add(userr);
             }
             madapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
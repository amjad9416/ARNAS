package com.example.arna1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class madapter extends RecyclerView.Adapter<madapter.MyViewHolder> {
    public madapter(Context c, ArrayList<userr> list) {
        this.c = c;
        this.list = list;
    }

    Context c;
    ArrayList<userr> list;
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        userr u=list.get(position);
        holder.fullname.setText(u.getName());
        holder.age.setText(u.getAge());
        holder.phone.setText(u.getPhone());
        holder.address.setText(u.getAddress());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
    TextView fullname,age,phone,address;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            fullname=itemView.findViewById(R.id.name);
            age=itemView.findViewById(R.id.age);
            phone=itemView.findViewById(R.id.phone);
            address=itemView.findViewById(R.id.address);
        }
    }


}

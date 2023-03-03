package com.example.special;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.special.databinding.RecyclerRowBinding;

import java.io.File;
import java.util.ArrayList;

public class firstPageAdapter extends RecyclerView.Adapter<firstPageAdapter.firstHolder> {
    ArrayList<Special> specialArrayList;
    SQLiteDatabase db;
    File f;

    public firstPageAdapter(ArrayList<Special> specialArrayList){
        this.specialArrayList = specialArrayList;
    }

    @NonNull
    @Override
    public firstHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new firstPageAdapter.firstHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull firstHolder holder, int position) {
        holder.binding.Textviewws.setText(specialArrayList.get(position).title);
        holder.binding.deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(specialArrayList.get(holder.getAdapterPosition()).title);
                db = v.getContext().openOrCreateDatabase(specialArrayList.get(holder.getAdapterPosition()).title, Context.MODE_PRIVATE,null);
                String url = "/data/data/com.example.special/databases";

                f = new File(url);

                SQLiteDatabase.deleteDatabase(f);


                System.out.println(db.getPath().toString());
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(holder.itemView.getContext(),MainActivity.class);
                intent.putExtra("Text",specialArrayList.get(holder.getAdapterPosition()).title);
                holder.itemView.getContext().startActivity(intent);
                System.out.println(specialArrayList.get(holder.getAdapterPosition()).title.toString());


            }
        });
    }

    @Override
    public int getItemCount() {
        if(specialArrayList == null){
            return 0;
        }else{
            return specialArrayList.size();
        }

    }

    public class firstHolder extends RecyclerView.ViewHolder{
        private RecyclerRowBinding binding;

        public firstHolder(RecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

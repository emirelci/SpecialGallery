package com.example.special;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.special.databinding.RecyclerGridBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class SpecialAdapter extends RecyclerView.Adapter<SpecialAdapter.specialHolder> {

    ArrayList<Special> specialArrayList;
    SQLiteDatabase db;
    String txt;

    public SpecialAdapter(ArrayList<Special> specialArrayList,String txt){
        this.specialArrayList = specialArrayList;
        this.txt = txt;
    }

    @NonNull
    @Override
    public specialHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Show screen anythings
        RecyclerGridBinding recyclerGridBinding = RecyclerGridBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new specialHolder(recyclerGridBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull specialHolder holder, int position) {
        holder.binding.imageView3.setImageBitmap(specialArrayList.get(position).img);// Show set screen anythings
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage(specialArrayList.get(holder.getAdapterPosition()).comments).setPositiveButton("EDİT Comment", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println((holder.getAdapterPosition()+1));
                        db = v.getContext().openOrCreateDatabase(txt, Context.MODE_PRIVATE,null);
                        db.execSQL("DELETE FROM special WHERE id = ? ",new Integer[]{(holder.getAdapterPosition()+1)});
                        db.execSQL("UPDATE special SET id = 1 ");
                        SpecialAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                        //SpecialAdapter.this.notifyDataSetChanged();

                        //sqLiteDatabase.execSQL("UPDATE myexample SET name = ? WHERE id = ?",new String[]{nameInput,String.valueOf(1)});
                    }
                }).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return specialArrayList.size();
    }

    public class specialHolder extends RecyclerView.ViewHolder{
        private  RecyclerGridBinding binding;
        public specialHolder(RecyclerGridBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}

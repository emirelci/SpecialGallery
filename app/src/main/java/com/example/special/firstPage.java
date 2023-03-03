package com.example.special;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.special.databinding.ActivityFirstPageBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class firstPage extends AppCompatActivity {

    private ActivityFirstPageBinding binding;
    ArrayList<Special> arrayTitleList;
    AlertDialog dialog;
    String text;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFirstPageBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        binding.imageView4.bringToFront();
        loadData();

        firstPageAdapter fp = new firstPageAdapter(arrayTitleList);
        binding.RecyclerViewHome.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.RecyclerViewHome.setAdapter(fp);

    }

    public void buildDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Title");

        View view = getLayoutInflater().inflate(R.layout.headline,null);

        EditText editText = view.findViewById(R.id.editTextTextPersonName);
        ImageView submit = view.findViewById(R.id.submit);
        ImageView cancel = view.findViewById(R.id.cancel);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                text = editText.getText().toString();

                if(text.matches("")){
                    Toast.makeText(firstPage.this, "please fill in the blank!", Toast.LENGTH_LONG).show();
                }else{
                    saveData(text);
                    recyclerView();
                    db = v.getContext().openOrCreateDatabase(text,MODE_PRIVATE,null);

                    dialog.dismiss();
                }

            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                recyclerView();
                dialog.dismiss();
            }
        });

        builder.setView(view);
        dialog = builder.create();

        dialog.show();

    }

    private void saveData(String text) {
        SharedPreferences sp = getApplicationContext().getSharedPreferences("NameDataBases",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        Special ssp = new Special(text);
        arrayTitleList.add(ssp);
        String json = gson.toJson(arrayTitleList);
        editor.putString("DatabasesName",json);
        editor.apply();
        loadData();
    }

    private void loadData(){
        SharedPreferences sp = getApplicationContext().getSharedPreferences("NameDataBases",MODE_PRIVATE);

        Gson gson = new Gson();
        String json = sp.getString("DatabasesName",null);

        Type type = new TypeToken<ArrayList<Special>>(){
        }.getType();

        arrayTitleList = gson.fromJson(json,type);

        if(arrayTitleList == null){
            arrayTitleList = new ArrayList<>();
        }

    }

    public void recyclerView(){
        firstPageAdapter fp = new firstPageAdapter(arrayTitleList);
        binding.RecyclerViewHome.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        binding.RecyclerViewHome.setAdapter(fp);
    }

    public void addClick(View v){
        buildDialog();

    }
}
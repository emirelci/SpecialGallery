package com.example.special;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.example.special.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private ActivityMainBinding binding;
    ArrayList<Special> special;
    SpecialAdapter specialAdapter;
    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);

        binding.imageView2.bringToFront();
        special = new ArrayList<>();

        Intent intent = getIntent();
        text = intent.getStringExtra("Text");
        System.out.println(text);
        getData();

        binding.RecyclerView.setLayoutManager(new GridLayoutManager(this, 2,GridLayoutManager.VERTICAL,false));
        specialAdapter = new SpecialAdapter(special,text);
        binding.RecyclerView.setAdapter(specialAdapter);
    }

    public void getData(){

        try {
            SQLiteDatabase db = this.openOrCreateDatabase(text,MODE_PRIVATE,null);
            Cursor cr = db.rawQuery("SELECT * FROM special",null);

            int commentsIx = cr.getColumnIndex("comments");
            int idIx = cr.getColumnIndex("id");
            int imageIx = cr.getColumnIndex("image");

            while(cr.moveToNext()){
                String cm = cr.getString(commentsIx);
                int id = cr.getInt(idIx);
                byte[] img = cr.getBlob(imageIx);
                Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
                Special sp = new Special(id,cm,bitmap);
                special.add(sp);
            }

            specialAdapter.notifyDataSetChanged();
            cr.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //My Menu Drawable
    //click MenuClick then show Popups
    public void MenuClick(View v){
        PopupMenu PopUp = new PopupMenu(this,v);
        PopUp.setOnMenuItemClickListener(this);
        PopUp.inflate(R.menu.menu);
        PopUp.show();
    }

    //Click item then what process task
    @Override
    public boolean onMenuItemClick(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1:
                Intent intent = new Intent(this,SpecialActivity.class);
                intent.putExtra("text",text);
                startActivity(intent);
                return true;
            default:
                return false;
        }
    }

}
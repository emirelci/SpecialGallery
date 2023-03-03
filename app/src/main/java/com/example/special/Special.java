package com.example.special;

import android.graphics.Bitmap;

public class Special {

    public String comments;
    public int id;
    public Bitmap img;
    public String title;
    public String text;

    public Special(int id , String comments, Bitmap img){
        this.comments = comments;
        this.id = id;
        this.img = img;
    }

    public Special(String title){
        this.title = title;
    }



}

package com.example.special;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.example.special.databinding.ActivitySpecialBinding;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class SpecialActivity extends AppCompatActivity {

    private ActivitySpecialBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    ActivityResultLauncher<String> permissionLauncher;
    Bitmap selectedImage;
    SQLiteDatabase db;
    String texxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpecialBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        Intent intent = getIntent();
        texxt = intent.getStringExtra("text");
        RegisterLauncher();

    }

    public void ClickGallery(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            //Android 33+ -> Read_Media_Images
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)){
                    Snackbar.make(view,"Permission needed for Gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //request permission(attempt 1)
                            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                        }
                    }).show();
                }else{
                    //request permission(attempt 2)
                    permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            }else{
                //Gallery
                Intent intentToGall = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGall);
            }
        }else{
            //Android 32- -> Read_External_storage
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Snackbar.make(view,"Permission needed for Gallery",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //request permission(attempt 1)
                            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                        }
                    }).show();
                }else{
                    //request permission(attempt 2)
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }else{
                //Gallery
                Intent intentToGall = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                activityResultLauncher.launch(intentToGall);
            }
        }


    }

    public void Save(View view){
        String Comment = binding.ThisComment.getText().toString();

        Bitmap smallImage = reduceSizeImage(selectedImage,300);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
        byte[] byteArray = outputStream.toByteArray();

        try {

            db = this.openOrCreateDatabase(texxt,MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS special (id INTEGER PRIMARY KEY, comments VARCHAR, image BLOB)");

            String sqlString = "INSERT INTO special (comments,image) VALUES(?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(sqlString);
            sqLiteStatement.bindString(1,Comment);
            sqLiteStatement.bindBlob(2,byteArray);
            sqLiteStatement.execute();

        }catch (Exception e ){
            e.printStackTrace();
        }

        Intent intent = new Intent(SpecialActivity.this,MainActivity.class);
        intent.putExtra("Text",texxt);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//ALL OPEN ACTIVITY CLEAR AND OPEN NEW INTENT
        startActivity(intent);
        this.finish();
    }
    //we will reduce its size so that we can save it in the database
    public Bitmap reduceSizeImage(Bitmap image, int maximumSize ){
        int width = image.getWidth();
        int height = image.getHeight();
        float bitmapRatio = (float) width / (float) height;

        if(bitmapRatio > 1){
            //landscape
            width = maximumSize;
            height = (int) (width / bitmapRatio);
        }else{
            //portrait
            height = maximumSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image,width,height,true);
    }

    private void RegisterLauncher(){

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK){
                    Intent intentFromResult = result.getData();
                    if(intentFromResult != null){
                       Uri imageData = intentFromResult.getData();


                       try{

                           if (android.os.Build.VERSION.SDK_INT >= 28) {
                               ImageDecoder.Source sr = ImageDecoder.createSource(getContentResolver(),imageData);
                               selectedImage = ImageDecoder.decodeBitmap(sr);
                               binding.imageView.setImageBitmap(selectedImage);
                           }else{
                               selectedImage = MediaStore.Images.Media.getBitmap(SpecialActivity.this.getContentResolver(),imageData);
                               binding.imageView.setImageBitmap(selectedImage);
                           }

                       }catch (Exception e){
                           e.printStackTrace();
                       }

                    }
                }
            }
        });


        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if(result){
                    //Permission Granted
                    Intent intentToGall = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intentToGall);
                }else{
                    //Permission Denied
                    Toast.makeText(SpecialActivity.this, "Permission Needed!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
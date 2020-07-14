package com.ahadabd.qrscannerandgenerator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.IOException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class generate extends AppCompatActivity {
    EditText et_value;
    Button btn_generate_now,btn_save;
    ImageView qr_image;
    AdView banner_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        permissionCheck();
        et_value= findViewById(R.id.et_value);
        btn_generate_now= findViewById(R.id.btn_generate_now);
        btn_save=findViewById(R.id.btn_save);
        qr_image=findViewById(R.id.qr_image);
        banner_ad= findViewById(R.id.adView2);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        banner_ad.loadAd(adRequest);

        btn_generate_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data= et_value.getText().toString();
                if(TextUtils.isEmpty(data)){
                    et_value.setError("Enter some text first!");
                }
                else{
                    try {
                        QRGEncoder qrgEncoder = new QRGEncoder(data,null, QRGContents.Type.TEXT,500);
                        Bitmap bitmap = qrgEncoder.getBitmap();
                        qr_image.setImageBitmap(bitmap);
                    } catch (Exception e) {
                        Log.v("Error",e.toString());
                    }
                }

            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = et_value.getText().toString();
                if(TextUtils.isEmpty(data)){
                    et_value.setError("enter some text!");
                }
                else {
                    try {
                        QRGEncoder qrgEncoder = new QRGEncoder(data,null, QRGContents.Type.TEXT,500);
                        Bitmap bitmap = qrgEncoder.getBitmap();
                        qr_image.setImageBitmap(bitmap);
                        String fileName = "QRCode_"+System.currentTimeMillis()+".jpg";
                        File file = new File(Environment.getExternalStorageDirectory(),fileName);
                        file.createNewFile();
                        String saveLocation = file.getParent()+File.separator ;
                        fileName = file.getName().substring(0,file.getName().indexOf("."));
                        QRGSaver qrgSaver = new QRGSaver();
                        qrgSaver.save(saveLocation,fileName,bitmap,QRGContents.ImageType.IMAGE_JPEG);
                        Toast.makeText(generate.this, "QR Code successfully saved in the storage!", Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
    private void permissionCheck(){
        if (ContextCompat.checkSelfPermission(generate.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(generate.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},123);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 123){
            if(grantResults.length > 0){
                for(int i=0;i<grantResults.length;i++)
                    if(grantResults[0] != PackageManager.PERMISSION_GRANTED)
                        finish();
            }else{
                finish();
            }
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_item_info) {
            Intent intent_about = new Intent(getApplicationContext(), About.class);
            startActivity(intent_about);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

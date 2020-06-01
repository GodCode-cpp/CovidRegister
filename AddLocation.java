package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class AddLocation extends AppCompatActivity
{
    private static final String TAG = "Add Location Activity";
    private EditText location;
    private ImageView qrcode;
    private Bitmap bitmap;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    String loc;
    private FloatingActionButton fabqrDone, fabGen;
    OutputStream outputStream;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        InitializingWidgets();

        fabGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: QR GENERATE BUTTON CLICKED");
                makeTheQRCode();
            }
        });
        fabqrDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddingLocationToUserDatabase();

            }
        });
    }

    private void DoneAddingLocation() {
        Log.d(TAG, "DoneAddingLocation: Entered function");
        Intent intent = new Intent(AddLocation.this, MainActivity.class);
        startActivity(intent);
    }

    private void SaveImage() {
        Log.d(TAG, "SaveImage: Entered function");
        BitmapDrawable drawable = (BitmapDrawable)qrcode.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File (filepath.getAbsolutePath() + "/Register QR Code");
        dir.mkdir();
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        try {
            outputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        Toast.makeText(getApplicationContext(), "Image Save To Internal!!!",Toast.LENGTH_SHORT).show();
        try {
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeTheQRCode() {
        Log.d(TAG, "makeTheQRCode: Entered Function");
        try {
            String Text =location.getText().toString().trim();
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(Text, BarcodeFormat.QR_CODE,600,600);
            qrcode.setImageBitmap(bitmap);
            if(isStoragePermissionGranted()){
                SaveImage();
            }
        }catch(Exception e){
            Log.e(TAG, "makeTheQRCode: exception e caught ");

        }
    }

    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG,"Permission is granted");
                return true;
            } else {

                Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this , new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG,"Permission is granted");
            return true;
        }
    }

    private void InitializingWidgets() {
        location = (EditText)findViewById(R.id.mEnterLocation);
        qrcode = (ImageView)findViewById(R.id.QRCode);
        fabqrDone = (FloatingActionButton)findViewById(R.id.fabGenerateQRC);
        fabGen = (FloatingActionButton)findViewById(R.id.fabGenerate);
        loc = location.getText().toString().trim();

    }


    /**Firebase Cloud Store**/

    private void AddingLocationToUserDatabase() {



        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> locationUIDR = new HashMap<>();
        locationUIDR.put("Location" ,location.getText().toString().trim());

        db.collection("User/"+ user.getUid()+ "/location").document("Location names")
                .set(locationUIDR, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DoneAddingLocation();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "onFailure: Error adding document", e);
                    }
                });

    }

}

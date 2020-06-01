package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.covidregister.Utilities.FirebaseMethods;
import com.example.covidregister.Utilities.Location;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class settings extends AppCompatActivity {
    private static final String TAG = "Settings Activity";
    private EditText mNames,mSurname,mAddress,mAge,mSex,mID;
    private ImageView mBack;
    private ProgressBar mProgressBar;
    private FloatingActionButton fabDone;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private User mLocation;
    private String userID,nNames,nSurname,nAddress,nAge,nSex,nID;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar3);
        mProgressBar.setVisibility(View.VISIBLE);
        mFirebaseMethods = new FirebaseMethods(settings.this);
        initializeWidgets();
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SaveDataRealTime();
                Intent intent = new Intent(settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    private void initializeWidgets() {
        Log.d(TAG, "initializeWidgets");
        mID = (EditText)findViewById(R.id.mId);
        mNames = (EditText)findViewById(R.id.mNames);
        mAddress = (EditText)findViewById(R.id.mAddress);
        mSurname = (EditText)findViewById(R.id.mSurname);
        mAge = (EditText)findViewById(R.id.mAge);
        mSex = (EditText)findViewById(R.id.mSex);
        fabDone = (FloatingActionButton)findViewById(R.id.fabDone);
        mBack = (ImageView)findViewById(R.id.imageView);
        setData();
        Log.d(TAG, "initializeWidgets: Returned from setData();");

    }

    /**Real time database Edit and save**/

    private void SaveDataRealTime() {
        mFirebaseMethods.updateUserDets(mNames.getText().toString().trim(),mSurname.getText().toString().trim(),mAddress.getText().toString().trim(),mID.getText().toString().trim(),mSex.getText().toString().trim(),mAge.getText().toString().trim());
    }

    private void setData() {
        Log.d(TAG, "setData: Entered Function");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Log.d(TAG, "setData: Retrived user id number");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Entered function");
                mLocation = mFirebaseMethods.GetRegisterAUser(dataSnapshot);
                Log.d(TAG, "onDataChange: data snapshot returned");
                nNames = mLocation.getNames();
                nSurname = mLocation.getSurname();
                nID = mLocation.getId();
                nAddress = mLocation.getAddress();
                nSex = mLocation.getSex();
                nAge = mLocation.getAge();
                mID.setText(nID);
                mNames.setText(nNames);
                mSurname.setText(nSurname);
                mAge.setText(nAge);
                mAddress.setText(nAddress);
                mSex.setText(nSex);
                mProgressBar.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}

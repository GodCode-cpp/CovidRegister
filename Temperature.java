package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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

public class Temperature extends AppCompatActivity {
    private static final String TAG = "Temperature Activity";
    private EditText Temp;
    String loc, datetime, TempStr;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private FloatingActionButton fabTempDone;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private User mLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        mFirebaseMethods = new FirebaseMethods(Temperature.this);
        InitializingWidgets();
        fabTempDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
            }
        });

    }

    private void InitializingWidgets() {
        Temp = (EditText)findViewById(R.id.editText);
        loc = getIntent().getStringExtra("location");
        TempStr = Temp.getText().toString();
        fabTempDone = (FloatingActionButton)findViewById(R.id.floatingActionButton);
    }

    private void init(){
        mAuth = FirebaseAuth.getInstance();
        Calendar calendar = Calendar.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseFirestore dbr = FirebaseFirestore.getInstance();
        java.util.Date date=new java.util.Date();
        datetime = date.toString();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mLocation = mFirebaseMethods.GetRegisterAUser(dataSnapshot);
                final Map<String, Object> UserRegistration = new HashMap<>();
                UserRegistration.put("Id",mLocation.getId());
                UserRegistration.put("Phone Number", mLocation.getPhoneNumber());
                UserRegistration.put("Address",mLocation.getAddress());
                UserRegistration.put("Age",mLocation.getAge());
                UserRegistration.put("Names",mLocation.getNames());
                UserRegistration.put("Sex",mLocation.getSex());
                UserRegistration.put("Surname",mLocation.getSurname());
                UserRegistration.put("Temperature",Temp.getText().toString());
                UserRegistration.put("Date & Time",datetime);
                final Map<String, Object> LocTime = new HashMap<>();
                LocTime.put(loc,datetime);

                dbr
                        .collection("location/" + loc +"/"+ userID ).document(datetime)
                        .set(UserRegistration, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dbr.collection("User/" + userID+ "/scanned locations").document(datetime)
                                        .set(LocTime,SetOptions.merge());
                                        System.out.println(UserRegistration);

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "onFailure: Error adding document", e);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Intent intent = new Intent(Temperature.this, Done_page.class);
        startActivity(intent);
    }
}

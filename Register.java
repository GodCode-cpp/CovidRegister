package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.covidregister.Utilities.FirebaseMethods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {
    private static final String TAG = "Registry";
    EditText mNames,mSurname,mAddress,mAge, mSex, mID;
    FloatingActionButton fabDone;
    ProgressBar progressBar;
    String PhoneNumber,names,surname,address,age, sex, id;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private FirebaseDatabase mfirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;
    private User user;
    private Context mContext;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        InitialiseWidget();
        mFirebaseMethods = new FirebaseMethods(mContext);
        fabDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init();
                Intent intent = new Intent(Register.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        PhoneNumber = getIntent().getStringExtra("PhoneNumber");
        names = mNames.getText().toString();
        surname = mSurname.getText().toString();
        id = mID.getText().toString();
        address = mAddress.getText().toString();
        age = mAge.getText().toString();
        sex = mSex.getText().toString();

        if (checkInputs(names,surname,id,address,age,sex, PhoneNumber)){
            Log.d(TAG, "Checking Input if statement: successfully Entered if statement");
            progressBar.setVisibility(View.VISIBLE);
            Log.d(TAG, "Calling firebaseMethod: calling the firebaseMethods from the class");
            mFirebaseMethods.addNewUser(address,age,id,names,surname,sex,PhoneNumber);
        }


    }

    private boolean checkInputs(String names, String surname, String id, String address, String age, String sex, String PhoneNumber){
        Log.d(TAG, "checkInputs: checking inputs for null values.");
        if(names.equals("") || surname.equals("") || id.equals("")|| address.equals("") || age.equals("")|| sex.equals("")|| PhoneNumber.equals("")){
            Toast.makeText(mContext, "All fields must be filled out.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void InitialiseWidget() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        mNames = (EditText)findViewById(R.id.mNames);
        mSurname = (EditText)findViewById(R.id.mSurname);
        mAddress = (EditText)findViewById(R.id.mAddress);
        mAge = (EditText)findViewById(R.id.mAge);
        mSex = (EditText)findViewById(R.id.mSex);
        mID = (EditText)findViewById(R.id.mID);
        fabDone = (FloatingActionButton)findViewById(R.id.fabDone);
        mContext = Register.this;

    }
}

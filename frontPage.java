package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class frontPage extends AppCompatActivity {
    private FloatingActionButton fabPhoneDone;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    private EditText mPhone;
    private static final String TAG = "frontPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_page);
        mPhone = (EditText)findViewById(R.id.Phone);
        fabPhoneDone = (FloatingActionButton)findViewById(R.id.fabPhoneDone);

        fabPhoneDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = "+267";
                String PhoneNumber = mPhone.getText().toString().trim();

                if(PhoneNumber.isEmpty() || PhoneNumber.length() < 10 ) {
                    mPhone.setError("Valid Number is Required");
                    mPhone.requestFocus();
                    return;
                }

                Intent intent = new Intent(frontPage.this, VerifyPhoneActivity.class);
                intent.putExtra("PhoneNumber", PhoneNumber);
                startActivity(intent);
            }
        });
    }

}

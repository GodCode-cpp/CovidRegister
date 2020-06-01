package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity {
    private static final String TAG = "VerifyPhoneActivity";
    private FloatingActionButton fabVCode;
    private EditText Code;
    private FirebaseAuth mAuth;
    private String VerificationID;
    private ProgressBar mProgressBar;
    private String PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        mAuth = FirebaseAuth.getInstance();
        PhoneNumber = getIntent().getStringExtra("PhoneNumber");
        fabVCode = (FloatingActionButton) findViewById(R.id.fabVDone);
        Code = (EditText) findViewById(R.id.VCode);
        mProgressBar = (ProgressBar)findViewById(R.id.mProgressBar);
        mProgressBar.setVisibility(View.GONE);
        System.out.println(PhoneNumber);
        sendVerificationsCode(PhoneNumber);
        fabVCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Clicked fab ");
                String pinCode = Code.getText().toString().trim();

                if (pinCode.isEmpty()){
                    Code.setError("Please Enter a valid code");
                    Code.requestFocus();
                    return;
                }
                verifyCode(pinCode);
            }
        });
    }

    private void sendVerificationsCode(String Pnumber) {
        Log.d(TAG, "sendVerificationsCode: Entered send verification code");
            mProgressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                Pnumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallBack
        );
        Log.d(TAG, "sendVerificationsCode: Done");
    }

    private void verifyCode(String code){
        Log.d(TAG, "verifyCode: Entered");
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationID, code);
        SignInWithCredential(credential);
    }

    private void SignInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser user = task.getResult().getUser();
                            Intent intent = new Intent(VerifyPhoneActivity.this, Register.class);
                            intent.putExtra("PhoneNumber", PhoneNumber);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }else{
                            Toast.makeText(VerifyPhoneActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerificationID = s;
        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "Entered onVerificationCompleted");
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                Code.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    };

}

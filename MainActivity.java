package com.example.covidregister;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import com.example.covidregister.Utilities.Permissions;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

    public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final String TAG = "MainActivity";
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    static android.graphics.Camera camera = null;
    private FloatingActionButton fabMain,fabAddLocation, fabHistory, fabPrint, fabSettings;
    private Button capture;
    public Boolean flag = true;
    private TextView mTextPrint, mTextHistory, mTextAddLoc, mTextSettings, txtResult;
    String loc;
    private ZXingScannerView scannerView;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressBar progressBar;
    private User mUserStrID;
    private String mUserID;
    Animation fab_open,fab_close,rotate_backward,rotate_forward;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpFirebaseAuth();
        InitializingWidgets();
        // Check if user is signed in (non-null) and update UI accordingly.
        permissionSystem();
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabClick();
            }
        });


        fabSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabSettingsclick();
            }
        });

        fabAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabLocationclick();
            }
        });

    }

        private void permissionSystem() {
            if(checkPermissionsArray(Permissions.PERMISSIONS)){
                Log.d(TAG, "onCreate: Entered permission if statement");

            }else{
                verifyPermissions(Permissions.PERMISSIONS);
            }
            Dexter.withActivity(MainActivity.this)
                    .withPermission(Manifest.permission.CAMERA)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            scannerView.setResultHandler(MainActivity.this);
                            scannerView.startCamera();
                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {
                            Toast.makeText(MainActivity.this, "You must accept permission",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                        }
                    })
                    .check();


        }


        private void InitializingWidgets() {
        //Initialising widgets
        fabAddLocation =(FloatingActionButton)findViewById(R.id.fabAddLocation);
        fabMain = (FloatingActionButton)findViewById(R.id.mFloatingInitiator);
        fabHistory = (FloatingActionButton)findViewById(R.id.fabHistory);
        fabPrint =(FloatingActionButton)findViewById(R.id.fabPrint);
        fabSettings = (FloatingActionButton)findViewById(R.id.fabSettings);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mTextPrint = (TextView)findViewById(R.id.mTextPrint);
        mTextHistory = (TextView)findViewById(R.id.mTextHistory);
        mTextAddLoc = (TextView)findViewById(R.id.mTextAddLoc);
        mTextSettings = (TextView)findViewById(R.id.mTextSettings);
        fab_open = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(MainActivity.this,R.anim.fab_close);
        rotate_backward = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_backward);
        rotate_forward = AnimationUtils.loadAnimation(MainActivity.this,R.anim.rotate_forward);
        scannerView = (ZXingScannerView) findViewById(R.id.scanner_view);
        progressBar.setVisibility(View.GONE);
        txtResult = (TextView)findViewById(R.id.textView2);
        fabMain.show();
    }
    /*----------------------------------------Permissions--------------------------------------------*/
    private void verifyPermissions(String[] permissions) {
        Log.d(TAG, "verifyPermissions: verifying permissions.");

        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );

    }

    /**
     * Check an array of permissions
     * @param permissions
     * @return
     */
    public boolean checkPermissionsArray(String[] permissions){
        Log.d(TAG, "checkPermissionsArray: checking permissions array.");

        for(int i = 0; i< permissions.length; i++){
            String check = permissions[i];
            if(!checkPermissions(check)){
                return false;
            }
        }
        return true;
    }

    /**
     * Check a single permission is it has been verified
     * @param permission
     * @return
     */
    public boolean checkPermissions(String permission){
        Log.d(TAG, "checkPermissions: checking permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permission);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermissions: \n Permission was not granted for: " + permission);
            return false;
        }
        else{
            Log.d(TAG, "checkPermissions: \n Permission was granted for: " + permission);
            return true;
        }
    }

        @Override
        public void handleResult(Result rawResult) {
                txtResult.setText(rawResult.getText());
                loc = txtResult.getText().toString();
                Intent intent = new Intent(MainActivity.this, Temperature.class);
                intent.putExtra("location", loc);
                startActivity(intent);
        }

        /**----------------------------------------Button Clicks--------------------------------------------*/

        private void fabClick() {
            if (flag) {
                fabMain.startAnimation(rotate_forward);
                //visible
                mTextPrint.setVisibility(View.VISIBLE);
                mTextHistory.setVisibility(View.VISIBLE);
                mTextSettings.setVisibility(View.VISIBLE);
                mTextAddLoc.setVisibility(View.VISIBLE);
                fabAddLocation.show();
                fabHistory.show();
                fabPrint.show();
                fabSettings.show();
                flag = false;

            } else {
                fabMain.startAnimation(rotate_backward);
                //hide
                mTextPrint.setVisibility(View.GONE);
                mTextHistory.setVisibility(View.GONE);
                mTextSettings.setVisibility(View.GONE);
                mTextAddLoc.setVisibility(View.GONE);
                fabAddLocation.hide();
                fabHistory.hide();
                fabPrint.hide();
                fabSettings.hide();
                flag = true;
            }
        }

        private void fabSettingsclick() {
            Intent intent = new Intent(this, settings.class);
            startActivity(intent);
        }
        private void fabLocationclick() {
            Intent intent = new Intent(this, AddLocation.class);
            startActivity(intent);
        }



        /**---------------------------------firebase--------------------------------------------------**/

        /**
         * Setup the firebase auth object
         */

        private void setUpFirebaseAuth(){
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    //check if the user is logged in
                   mUserStrID = user.getUid();
                    checkCurrentUser(user);
                    if (user != null){
                        //User is signed in
                        Log.d(TAG, "onAuthStateChanged:signed in" + user.getUid());
                    }else{
                        //User is signed out
                        Log.d(TAG, "onAuthStateChanged:signed out" );
                        finish();
                    }
                }
            };
        }

        @Override
        public void onStart() {
            super.onStart();
            mAuth.addAuthStateListener(mAuthListener);
        }

        public void onStop(){
            super.onStop();
            if (mAuthListener != null){
                mAuth.removeAuthStateListener(mAuthListener);
            }
        }

        @Override
        protected void onDestroy() {
            scannerView.stopCamera();
            super.onDestroy();
        }

        /**
         * checks to see if the @param 'user' is logged in
         * @param user
         */

        private void checkCurrentUser(FirebaseUser user){
            Log.d(TAG, "checkCurrentUser: checking if user is logged in.");
            if(user == null){
                Intent intent = new Intent (MainActivity.this, frontPage.class);
                startActivity(intent);
            }

        }

        private void getMissingDetails(DataSnapshot dataSnapshot){
            for (DataSnapshot ds : dataSnapshot.getChildren()){
            User user = new User();
            mUserID = user.getId(ds.getChildren())

            }

        }


    }

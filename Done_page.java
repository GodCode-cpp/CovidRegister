package com.example.covidregister;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class Done_page extends AppCompatActivity {
        private IMainActivity mIMainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_page);
        Intent intent = new Intent(Done_page.this, MainActivity.class);
        startActivity(intent);
    }


}

package com.example.myandroidlibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class MainAndroidLibraryActivity extends AppCompatActivity {
    private TextView tvAndroidLibraryJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_android_library);
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        initViews();

    }

    private void initViews() {
        tvAndroidLibraryJoke = findViewById(R.id.tv_joke);
        tvAndroidLibraryJoke.setText(getIntent().getStringExtra(getString(R.string.joke)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}

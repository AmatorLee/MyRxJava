package com.amatorlee.myrxjava;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createOperators(View view){
        startActivity(new Intent(MainActivity.this,CreateActivity.class));
    }

    public void changeOperators(View view){
        startActivity(new Intent(MainActivity.this,ChangeActivity.class));
    }
    public void flitOperators(View v){
        startActivity(new Intent(MainActivity.this,FiltActivity.class));
    }
}

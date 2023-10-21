package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void styleThem(View view) {
        Switch sw = findViewById(R.id.styleTheme);
        if (sw.isChecked()) {
            Logger.getLogger("main").log(Level.ALL, "checked");
            getApplication().setTheme(android.R.style.Theme_Holo);
        } else {
            Logger.getLogger("main").log(Level.ALL, "checked");
            getApplication().setTheme(android.R.style.Theme_Black);
        }
    }
    public void door(View view) {
        Switch sw = findViewById(R.id.door);
        if (sw.isChecked()) {
            //дверь открыть
        } else {
            //дверь закрыть
        }
    }
    public void light(View view) {
        Switch sw = findViewById(R.id.light);
        if (sw.isChecked()) {
            //включить свет
        } else {
            //выключить свет
        }
    }
}
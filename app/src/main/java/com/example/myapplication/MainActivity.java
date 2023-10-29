package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private final static Logger LOGER = Logger.getLogger(MainActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void styleThem(View view) {
        Switch sw = findViewById(R.id.styleTheme);
        if (!sw.isChecked()) {
            LOGER.log(Level.ALL, "Light theme");
            //добавлена смена ночного режима (темной темы)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            LOGER.log(Level.ALL, "Dark theme");
            //добавлена смена ночного режима (темной темы)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }
    public void door(View view) throws JSONException {
        Switch sw = findViewById(R.id.door);
        JSONObject json = new JSONObject("{\"status\":\"true\"}");
        sw.setChecked(json.getBoolean("status"));
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
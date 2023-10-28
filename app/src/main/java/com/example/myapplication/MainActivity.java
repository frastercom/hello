package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
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
        if (sw.isChecked()) {
            LOGER.log(Level.ALL, "Light theme");
            getApplication().setTheme(android.R.style.Theme_Black);
            getApplication().setTheme(R.style.Theme_MyApplication);
            setTheme(R.style.Base_Theme_MyApplication);
        } else {
            LOGER.log(Level.ALL, "Dark theme");
            setTheme(android.R.style.Theme_Black);
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
package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Switch;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private final static Logger LOGER = Logger.getLogger(MainActivity.class.getName());
    public final static String BASIC_URL = "https://simple-service.ddns.net"; //Базовый адрес сервера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //снимаем ограничение на асинхронный поток
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Получаем компонент с формы
        Switch sw = findViewById(R.id.door);
        //получаем статус с сервера и устанавливаем его значение
        sw.setChecked(Objects.equals("open", getValueServer("/arduino/door")));
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

    public void door(View view) {
        Switch sw = findViewById(R.id.door);
        if (sw.isChecked()) {
            //отправляем запрос на сервер для установки статуса закрытия двери
            setValueServer("/arduino/door", "open");
            //дверь открыть
        } else {
            //дверь закрыть
            //отправляем запрос на сервер для установки статуса закрытия двери
            setValueServer("/arduino/door", "close");
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

    //GET запрос для получения данных с сервера
    public String getValueServer(String api) {
        String value = null;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASIC_URL.concat(api))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                value = json.getString("status");
            }
        } catch (Exception e) {
            //игнорируем ошибку
        }
        return value;
    }

    //POST запрос для записи данных на сервер
    public String setValueServer(String api, String status) {
        String value = null;
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(String.format("{\"status\":\"%s\"}", status), JSON);
        Request request = new Request.Builder()
                .url(BASIC_URL.concat(api))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                value = json.getString("status");
            }
        } catch (Exception e) {
            e.toString();
            //игнорируем ошибку
        }
        return value;
    }
}
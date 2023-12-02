package com.example.myapplication.service;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerConnection {
    public final static String BASIC_URL = "https://simple-service.ddns.net"; //Базовый адрес сервера

    public static String getValueServer(String api) {
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
    //GET запрос для получения данных с сервера

    public static String setValueServer(String api, String status) {
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
    //POST запрос для записи данных на сервер

    public static String getTempServer() {
        String value = null;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASIC_URL.concat("/arduino/temp"))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                value = json.getString("size");
            }
        } catch (Exception e) {
            //игнорируем ошибку
        }
        return value;
    }
    //вывод показателей температуры на сервер
}

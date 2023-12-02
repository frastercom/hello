package com.example.myapplication.service;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArduinoConnection {
    public final static String BASIC_URL = "http://192.168.4.1"; //Базовый адрес сервера

    /**
     * записываем значение
     *
     * @param api    адрес
     * @param status значение датчиков
     */
    public static void setValue(String api, String status) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)//Устанавливается тайм-аут на соединение
                .writeTimeout(200, TimeUnit.MILLISECONDS)//Устанавливается тайм-аут на написание
                .readTimeout(200, TimeUnit.MILLISECONDS)//Устанавливается тайм-аут на чтение
                .build();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(String.format("{\"status\":\"%s\"}", status), JSON);//Устанавливается формат в виде JSON
        Request request = new Request.Builder()
                .url(BASIC_URL.concat(api))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                String value = json.getString("status");
            }
        } catch (Exception e) {//игнорируем ошибку
            e.toString();

        }
    }

    /**
     * получаем значение по wifi с arduino
     *
     * @param api адрес
     * @return значение температуры
     */
    public static String getValue(String api) {
        String value = null;
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(200, TimeUnit.MILLISECONDS)//Устанавливается тайм-аут на соединение
                .writeTimeout(200, TimeUnit.MILLISECONDS)//Устанавливается тайм-аут на написание
                .readTimeout(200, TimeUnit.MILLISECONDS)//Устанавливается тайм-аут на чтение
                .build();

        Request request = new Request.Builder()
                .url(BASIC_URL.concat(api))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                value = json.getString("status");
            }
        } catch (Exception e) {//игнорируем ошибку

        }
        return value;
    }

}

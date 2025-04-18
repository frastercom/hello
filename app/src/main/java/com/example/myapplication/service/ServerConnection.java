package com.example.myapplication.service;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerConnection {
    public final static String BASIC_URL = "http://simple-service.ddns.net"; //Базовый адрес сервера

    public static String getValueServer(String api) { //Запрос на сервер
        String value = null; //Устанавливается начальное значение показателя температуры
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(BASIC_URL.concat(api))
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                value = json.getString("status");
            }
        } catch (Exception e) { //игнорируем ошибку

        }
        return value;//В случае ошибки возвращаемся к исходному значению value
    }
    //GET запрос для получения данных с сервера

    public static String setValueServer(String api, String status) {
        String value = null;//Устанавливается начальное значение показателя температуры
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(String.format("{\"status\":\"%s\"}", status), JSON); //Устанавливается формат в виде JSON
        Request request = new Request.Builder()
                .url(BASIC_URL.concat(api))
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                JSONObject json = new JSONObject(response.body().string());
                value = json.getString("status");
            }
        } catch (Exception e) {//игнорируем ошибку
            e.toString();

        }
        return value;//В случае ошибки возвращаемся к исходному значению value
    }

    //POST запрос для записи данных на сервер
    public static String getTempServer() {//Метод вывода показаний температуры на сервер
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
        } catch (Exception e) { //игнорируем ошибку

        }
        return value;//В случае ошибки возвращаемся к исходному значению value
    }
    //вывод показателей температуры на сервер
}

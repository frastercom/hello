package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.myapplication.service.SelectConnection;
import com.example.myapplication.service.ServerConnection;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {

    private final static Logger LOGER = Logger.getLogger(MainActivity.class.getName()); //логирование
    private final SelectConnection connection = new SelectConnection(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //снимаем ограничение на асинхронный поток
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //Получаем компонент с формы
        Switch swDoor = findViewById(R.id.door);
        //получаем статус с сервера и устанавливаем его значение
        swDoor.setChecked(Objects.equals("open", ServerConnection.getValueServer("/arduino/door")));
        Switch swLight = findViewById(R.id.light);
        //получаем статус с сервера и устанавливаем его значение
        swLight.setChecked(Objects.equals("on", ServerConnection.getValueServer("/arduino/light")));

        //Этот метод будет выполняться в побочном потоке
        Thread myThready = new Thread(() -> {
            while (true) {
                TextView temp = findViewById(R.id.temp);
                //получаем статус с сервера и устанавливаем его значение
                temp.setText(ServerConnection.getTempServer() + "°C");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        myThready.start();    //Запуск потока


    }

    public void styleThem(View view) { //Смена темы
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
    //По умолчанию включена светлая тема, всегда когда кнопка выключена
    //Первым действием тёмная тема выключена
    //Вторым действием тёмная тема включена

    public void door(View view) { //Управление дверью
        Switch sw = findViewById(R.id.door);
        if (sw.isChecked()) {
            connection.DoorOpen(); //отправляем запрос на сервер для установки статуса открытия двери
        } else {
            connection.DoorClose();//отправляем запрос на сервер для установки статуса закрытия двери

        }
    }

    public void light(View view) { //Управление светом
        Switch sw = findViewById(R.id.light);
        if (sw.isChecked()) {
            connection.lightOn();//включить свет
        } else {
            connection.LightOff();//выключить свет
        }
    }

    public void connection(View view) { //Выбор способа подключения
        Switch sw = findViewById(R.id.connectionMethod);
        connection.setServerConnection(!sw.isChecked());
    }

}
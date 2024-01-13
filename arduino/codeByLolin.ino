/*
 * Менеджер плат http://arduino.esp8266.com/stable/package_esp8266com_index.json
 * Код предназначен для Lolin NodeMCU V3 на базе ESP8266
 * Цифровые пины 0,1 отвечают за индикацию открытой двери и включения света
 * Аналоговый пин А0 отвечает за определение температуры
 *
 * Программа получает данные с сервера по состоянию откртой двери и включения света,
 * в зависимости от получаемого статуса открыват/закрывает дверь, включает/отключает свет.
 * Передает на сервер температуру
 *
 * Программа работает с http запросами типа GET и POST
 */

#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClient.h>

WiFiClient wifiClient;
const char* ssid = ""; //Наименование сети W-Fi
const char* password = ""; //пароль от сети
const String basicUrl = "http://simple-service.ddns.net"; // базовый адрес сервера

void setup () {

    Serial.begin(115200);
    WiFi.begin(ssid, password); //подключение к сети

    while (WiFi.status() != WL_CONNECTED) { //цикл с ожиданием подключения

        delay(1000);
        Serial.print("Connecting..");

    }
 //при инициализации портов, программа работает не корректно, необходимо исправить
    pinMode(0, OUTPUT);  // D0 как выход
    pinMode(1, OUTPUT);  // D1 как выход
    pinMode(A0, INPUT);  // A0 как вход

}

void loop() {

    if (WiFi.status() == WL_CONNECTED) { //проверка подключения к сети Wi-Fi
        if (reqest("/arduino/door").indexOf("open") >= 0) //проверка статуса двери
        {
            digitalWrite(0, HIGH);//не работает
            Serial.println("OPEN");
        } else {
            digitalWrite(0, LOW);//не работает
            Serial.println("CLOSE");
        }
        if (reqest("/arduino/light").indexOf("\"on\"") >=0)//проверка статуса света
        {
            digitalWrite(1, HIGH);//не работает
            Serial.println("ON");
        } else {
            digitalWrite(1, LOW);//не работает
            Serial.println("OFF");
        }
        double analog = analogRead(A0); //чтение аналогового пина
        String temp = String(analog / 25, 2);//преобразование значения в привычный формат температуры
        reqest("/arduino/temp", temp);//запись значения на сервер
    }

    delay(1000);    //задержка в 1 секунду

}

String reqest(String restApi) // GET запрос на сервер
{
    HTTPClient http;    //Объявить объект класса HttpClient

    http.begin(wifiClient, basicUrl+restApi);      //Укажите адрес запроса
    int httpCode = http.GET();
    String payload = http.getString();                  //Получите полезную нагрузку ответа

    Serial.println(httpCode);   //Распечатать код возврата HTTP
    Serial.println(payload);    //Полезная нагрузка для ответа на запрос печати

    http.end();
    return payload;
}

void reqest(String restApi, String body)// POST запрос на сервер
{
    HTTPClient http;    //Объявить объект класса HttpClient

    http.begin(wifiClient, basicUrl+restApi);      //Укажите адрес запроса
    http.addHeader("Content-Type", "application/json");
    int httpCode = http.POST("{\"size\":\""+body+"\"}");
    String payload = http.getString();                  //Получите полезную нагрузку ответа

    Serial.println(httpCode);   //Распечатать код возврата HTTP
    Serial.println(payload);    //Полезная нагрузка для ответа на запрос печати

    http.end();
}
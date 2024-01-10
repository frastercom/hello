/*
 * Код предназначен для Arduino uno в комплексе с ESP 8266
 * Выбирается модуль Arduino Uno
 * пины 8,9 отвечают за софтверный сериал порт для подключения ESP
 * цифровые пины 4, 5 отвечают за переключения режима работы модуля с сервера на клиента и наоборот
 * цифровые пины 6,7 отвечают за индикацию открытой двери и включения света
 * аналоговый пин А0 отвечает за температуру, к нему подключался реостат
 */


boolean modeServer = true; // флаг что ардуино как сервер
#include <SoftwareSerial.h>
//https://randomnerdtutorials.com/esp8266-nodemcu-http-get-post-arduino/
// создаём объект для работы с программным Serial
// и передаём ему пины TX и RX
SoftwareSerial mySerial(8, 9);

// serial-порт к которому подключён Wi-Fi модуль
#define WIFI_SERIAL    mySerial
#define TIMEOUT 5000 // mS

int countTrueCommand;
int countTimeCommand;

void setup() {
    // put your setup code here, to run once:
    //общие настройки
    Serial.begin(9600);
    while (!Serial) {
        // ждём, пока не откроется монитор последовательного порта
        // для того, чтобы отследить все события в программе
    }
    Serial.print("Serial init OK\r\n");
    // открываем Serial-соединение с Wi-Fi модулем на скорости 9600 бод
    WIFI_SERIAL.begin(9600);
    if (modeServer) {
        setupServer();
    } else {
        setupClient();
    }
}

void setupServer() {
    SendCommandServer("AT+RST", "Ready");
    delay(5000);
    SendCommandServer("AT+CWMODE=2","OK");
    SendCommandServer("AT+CIFSR", "OK");
    SendCommandServer("AT+CIPMUX=1","OK");
    SendCommandServer("AT+CIPSERVER=1,80","OK");
}

void setupClient() {
    //настроки режима как клиент

    SendCommand("AT+RST", "Ready");
    delay(5000);
    SendCommand("AT+CWMODE=2","OK");
    SendCommand("AT+CIFSR", "OK");
    SendCommand("AT+CIPMUX=1","OK");
    SendCommand("AT+CIPSERVER=1,80","OK");
    SendCommand("AT+RST","OK");
    SendCommand("AT+CWMODE=1","OK");
    SendCommand("AT+CWJAP=\"McDonald’s Free WiFi\",\"qwerty73\"","OK");
}

void loop() {
    // put your main code here, to run repeatedly:
    checkMode();
    if (modeServer) {
        loopServerMode();
    } else {
        loopClientMode();
    }
}

//проверка переключения режима
void checkMode() {
    if (digitalRead(4) == 1) {
        Serial.print("ServerMode OK\r\n");
        setupServer();
        delay(5000);
    } else if (digitalRead(5) == 1) {
        Serial.print("ClientMode OK\r\n");
        setupClient();
        delay(5000);
    }

}

//режим сервер обработка
void loopServerMode() {
    String IncomingString="";
    boolean StringReady = false;

    while (WIFI_SERIAL.available()){
        IncomingString=WIFI_SERIAL.readString();
        StringReady= true;
    }

    if (StringReady){
        Serial.println("Received String: " + IncomingString);

        if (IncomingString.indexOf("LED=ON") != -1) {
            digitalWrite(6,HIGH);
            WIFI_SERIAL.println("AT+CIPSEND=0,23");
            WIFI_SERIAL.println("<h1>Button was pressed!</h1>");
            delay(1000);
            SendCommandServer("AT+CIPCLOSE=0","OK");
        }

        if (IncomingString.indexOf("LED=OFF") != -1) {
            digitalWrite(6,LOW);
            WIFI_SERIAL.print("ok");
        }

        if (IncomingString.indexOf("DOOR=OPEN") != -1) {
            digitalWrite(7,HIGH);
            WIFI_SERIAL.print("door open ok");
        }

        if (IncomingString.indexOf("DOOR=CLOSE") != -1) {
            digitalWrite(7,LOW);
            WIFI_SERIAL.print("door close ok");
        }
    }
}

//режим клиента обработка
void loopClientMode() {
        String getData = "GET /arduino/door";
        sendCommand("AT+CIPMUX=1", 5, "OK");
        sendCommand("AT+CIPSTART=0,\"TCP\",\"simple-service.ddns.net\"," + 80, 15, "OK");
        sendCommand("AT+CIPSEND=0," + String(getData.length() + 4), 4, ">");
        sendCommand(getData, 5, "OK");
        //esp8266.println(getData);
        delay(1500);
        countTrueCommand++;
        sendCommand("AT+CIPCLOSE=0", 5, "OK");
}


//методы для сервера

boolean SendCommandServer(String cmd, String ack){
    WIFI_SERIAL.println(cmd); // Send "AT+" command to module
    if (!echoFind(ack)) // timed out waiting for ack string
        return true; // ack blank or ack found
}

boolean echoFind(String keyword){
    byte current_char = 0;
    byte keyword_length = keyword.length();
    long deadline = millis() + TIMEOUT;
    while(millis() < deadline){
        if (WIFI_SERIAL.available()){
            char ch = WIFI_SERIAL.read();
            Serial.write(ch);
            if (ch == keyword[current_char])
                if (++current_char == keyword_length){
                    Serial.println();
                    return true;
                }
        }
    }
    return false; // Timed out
}

//методы для клиента
void sendCommand(String command, int maxTime, char readReplay[]) {
    Serial.print(countTrueCommand);
    Serial.print(". at command => ");
    Serial.print(command);
    Serial.print(" ");
    int found = 0;
    while (countTimeCommand < (maxTime * 1))
    {
        WIFI_SERIAL.println(command);//at+cipsend
        if (WIFI_SERIAL.find(readReplay)) //ok
        {
            found = true;
            break;
        }

        countTimeCommand++;
    }
    if (found == true)
    {
        Serial.println("OYI");
        countTrueCommand++;
        countTimeCommand = 0;
    }

    if (found == false)
    {
        Serial.println("Fail");
        countTrueCommand = 0;
        countTimeCommand = 0;
    }

    found = false;
}
boolean SendCommand(String cmd, String ack){
    WIFI_SERIAL.println(cmd); // Send "AT+" command to module
    if (!echoFind(ack)) // timed out waiting for ack string
        return true; // ack blank or ack found
}
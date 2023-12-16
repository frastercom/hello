
boolean modeServer = true; // флаг что ардуино как сервер

void setup() {
    // put your setup code here, to run once:
    //общие настройки
    if (modeServer) {
        setupServer();
    } else {
        setupClient();
    }
}

void setupServer() {
    //настроки режима как сервер
}

void setupClient() {
    //настроки режима как клиент
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
        setupServer();
    } else if (digitalRead(5) == 1) {
        setupClient();
    }
    delay(5000);
}

//режим сервер обработка
void loopServerMode() {

}

//режим клиента обработка
void loopClientMode() {

}

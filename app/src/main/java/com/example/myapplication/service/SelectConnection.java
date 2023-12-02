package com.example.myapplication.service;

public class SelectConnection {
    private boolean serverConnection;

    public SelectConnection(boolean serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void setServerConnection(boolean serverConnection) {
        this.serverConnection = serverConnection;
    }

    public void lightOn() {
        if (serverConnection) {
            ServerConnection.setValueServer("/arduino/light", "on"); //свет включить server
        } else {
            ArduinoConnection.getValue("/LED=ON");//arduino wifi
        }
    }

    public void LightOff() {
        if (serverConnection) {
            ServerConnection.setValueServer("/arduino/light", "off"); // свет выключить server
        } else {
            ArduinoConnection.getValue("/LED=OFF"); // arduino wifi
        }
    }

    public void DoorOpen() {
        if(serverConnection){
            ServerConnection.setValueServer("/arduino/door", "open"); //дверь открыть server
        }else{
            ArduinoConnection.getValue("/DOOR=OPEN"); //arduino wifi
        }
    }

    public void DoorClose() {
        if(serverConnection){
            ServerConnection.setValueServer("/arduino/door", "close"); //дверь закрыть server
        }else{
            ArduinoConnection.getValue("/DOOR=CLOSE"); //arduino wifi
        }
    }
}

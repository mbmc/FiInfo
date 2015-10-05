package com.mbmc.fiinfo.data;


public class ConnectivityEvent {

    public Event event;
    public String name;
    public String speed;


    public ConnectivityEvent(Event event) {
        this(event, "");
    }

    public ConnectivityEvent(Event event, String name) {
        this(event, name, "");
    }

    public ConnectivityEvent(Event event, String name, String speed) {
        this.event = event;
        this.name = name;
        this.speed = speed;
    }

}
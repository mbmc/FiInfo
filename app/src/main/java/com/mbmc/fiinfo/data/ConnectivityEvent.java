package com.mbmc.fiinfo.data;

import org.parceler.Parcel;


@Parcel
public class ConnectivityEvent {

    public Event event;
    public String name;
    public String mobile;
    public String speed;


    public ConnectivityEvent() {
        this(Event.NONE);
    }

    public ConnectivityEvent(Event event) {
        this(event, "");
    }

    public ConnectivityEvent(Event event, String name) {
        this(event, name, "");
    }

    public ConnectivityEvent(Event event, String name, String speed) {
        this(event, name, "", speed);
    }

    public ConnectivityEvent(Event event, String name, String mobile, String speed) {
        this.event = event;
        this.name = name;
        this.mobile = mobile;
        this.speed = speed;
    }

}

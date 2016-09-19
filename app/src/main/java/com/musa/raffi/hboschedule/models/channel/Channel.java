package com.musa.raffi.hboschedule.models.channel;

/**
 * Created by Asus on 9/7/2016.
 */

public class Channel {
    private String name;
    private String id;

    public Channel(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

}

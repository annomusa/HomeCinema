package com.musa.raffi.hboschedule.models;

/**
 * Created by Asus on 9/10/2016.
 */

public class Schedule {
    private int id;
    private String channel;
    private String date;
    private String filmName;
    private String showTime;
    private String filmPlot;
    private int reminder;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getFilmPlot() {
        return filmPlot;
    }

    public void setFilmPlot(String filmPlot) {
        this.filmPlot = filmPlot;
    }

    public int isReminder() {
        return reminder;
    }

    public void setReminder(int reminder) {
        this.reminder = reminder;
    }
}

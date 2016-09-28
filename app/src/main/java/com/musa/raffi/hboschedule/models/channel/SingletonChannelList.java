package com.musa.raffi.hboschedule.models.channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Asus on 9/7/2016.
 */
public class SingletonChannelList {
    private List<Channel> channelList;
    HashMap<String, String> a;

    private static SingletonChannelList ourInstance = new SingletonChannelList();
    public static SingletonChannelList getInstance() {
        return ourInstance;
    }

    private SingletonChannelList() {
        channelList = new ArrayList<>();
        channelList.add(new Channel("HBO HD", "0"));
        channelList.add(new Channel("HBO Sign", "1"));
        channelList.add(new Channel("HBO Fam", "2"));
        channelList.add(new Channel("HBO Hits", "3"));
        channelList.add(new Channel("Cinemax", "4"));
    }

    public Channel getChannel(Integer index){
        return channelList.get(index);
    }

    public List<Channel> getChannelList() {
        return channelList;
    }
}

package com.musa.raffi.hboschedule.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 9/7/2016.
 */
public class ChannelList {
    private List<Channel> channelList;

    private static ChannelList ourInstance = new ChannelList();
    public static ChannelList getInstance() {
        return ourInstance;
    }

    private ChannelList() {
        channelList = new ArrayList<>();
        channelList.add(new Channel("HBO HD", "0"));
        channelList.add(new Channel("HBO Sign", "1"));
        channelList.add(new Channel("HBO Fam", "2"));
        channelList.add(new Channel("HBO Hits", "3"));
        channelList.add(new Channel("Cinemax", "4"));
    }

    public Channel getChannel(int index){
        return channelList.get(index);
    }

    public List<Channel> getChannelList() {
        return channelList;
    }
}

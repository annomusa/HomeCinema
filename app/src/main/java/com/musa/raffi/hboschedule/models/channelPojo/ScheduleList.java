
package com.musa.raffi.hboschedule.models.channelPojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ScheduleList {

    private String channel;
    private String date;
    private List<Item> items = new ArrayList<Item>();
    private String response;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The channel
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 
     * @param channel
     *     The channel
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 
     * @return
     *     The date
     */
    public String getDate() {
        return date;
    }

    /**
     * 
     * @param date
     *     The date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * 
     * @return
     *     The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     * 
     * @param items
     *     The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * 
     * @return
     *     The response
     */
    public String getResponse() {
        return response;
    }

    /**
     * 
     * @param response
     *     The response
     */
    public void setResponse(String response) {
        this.response = response;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

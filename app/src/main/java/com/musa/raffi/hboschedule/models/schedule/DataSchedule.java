
package com.musa.raffi.hboschedule.models.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class DataSchedule {

    private String channelName;
    private Object channelDescription;
    private Integer schedulesTotal;
    private List<Schedule> schedules = new ArrayList<Schedule>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The channelName
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * 
     * @param channelName
     *     The channel_name
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * 
     * @return
     *     The channelDescription
     */
    public Object getChannelDescription() {
        return channelDescription;
    }

    /**
     * 
     * @param channelDescription
     *     The channel_description
     */
    public void setChannelDescription(Object channelDescription) {
        this.channelDescription = channelDescription;
    }

    /**
     * 
     * @return
     *     The schedulesTotal
     */
    public Integer getSchedulesTotal() {
        return schedulesTotal;
    }

    /**
     * 
     * @param schedulesTotal
     *     The schedules_total
     */
    public void setSchedulesTotal(Integer schedulesTotal) {
        this.schedulesTotal = schedulesTotal;
    }

    /**
     * 
     * @return
     *     The schedules
     */
    public List<Schedule> getSchedules() {
        return schedules;
    }

    /**
     * 
     * @param schedules
     *     The schedules
     */
    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

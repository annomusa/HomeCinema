
package com.musa.raffi.hboschedule.models.schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class ScheduleList {

    private Integer totalChannel;
    private List<DataSchedule> dataSchedule = new ArrayList<DataSchedule>();
    private String response;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The totalChannel
     */
    public Integer getTotalChannel() {
        return totalChannel;
    }

    /**
     * 
     * @param totalChannel
     *     The total_channel
     */
    public void setTotalChannel(Integer totalChannel) {
        this.totalChannel = totalChannel;
    }

    /**
     * 
     * @return
     *     The dataSchedule
     */
    public List<DataSchedule> getDataSchedule() {
        return dataSchedule;
    }

    /**
     * 
     * @param dataSchedule
     *     The data_schedule
     */
    public void setDataSchedule(List<DataSchedule> dataSchedule) {
        this.dataSchedule = dataSchedule;
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

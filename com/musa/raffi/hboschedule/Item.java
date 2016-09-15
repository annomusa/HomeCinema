
package com.musa.raffi.hboschedule;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Item {

    private String filmName;
    private Object filmPlot;
    private String showTime;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The filmName
     */
    public String getFilmName() {
        return filmName;
    }

    /**
     * 
     * @param filmName
     *     The film_name
     */
    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    /**
     * 
     * @return
     *     The filmPlot
     */
    public Object getFilmPlot() {
        return filmPlot;
    }

    /**
     * 
     * @param filmPlot
     *     The film_plot
     */
    public void setFilmPlot(Object filmPlot) {
        this.filmPlot = filmPlot;
    }

    /**
     * 
     * @return
     *     The showTime
     */
    public String getShowTime() {
        return showTime;
    }

    /**
     * 
     * @param showTime
     *     The show_time
     */
    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

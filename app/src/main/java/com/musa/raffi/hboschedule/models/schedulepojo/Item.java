
package com.musa.raffi.hboschedule.models.schedulepojo;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("org.jsonschema2pojo")
public class Item {
    @SerializedName("jadwal_id")
    private String jadwalId;
    @SerializedName("film_id")
    private String filmId;
    @SerializedName("film_name")
    private String filmName;
    @SerializedName("film_plot")
    private Object filmPlot;
    @SerializedName("show_time")
    private String showTime;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     *
     * @return
     * The jadwalId
     */
    public String getJadwalId() {
        return jadwalId;
    }

    /**
     *
     * @param jadwalId
     * The jadwal_id
     */
    public void setJadwalId(String jadwalId) {
        this.jadwalId = jadwalId;
    }

    /**
     *
     * @return
     * The filmId
     */
    public String getFilmId() {
        return filmId;
    }

    /**
     *
     * @param filmId
     * The film_id
     */
    public void setFilmId(String filmId) {
        this.filmId = filmId;
    }

    /**
     *
     * @return
     * The filmName
     */
    public String getFilmName() {
        return filmName;
    }

    /**
     *
     * @param filmName
     * The film_name
     */
    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    /**
     *
     * @return
     * The filmPlot
     */
    public Object getFilmPlot() {
        return filmPlot;
    }

    /**
     *
     * @param filmPlot
     * The film_plot
     */
    public void setFilmPlot(Object filmPlot) {
        this.filmPlot = filmPlot;
    }

    /**
     *
     * @return
     * The showTime
     */
    public String getShowTime() {
        return showTime;
    }

    /**
     *
     * @param showTime
     * The show_time
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

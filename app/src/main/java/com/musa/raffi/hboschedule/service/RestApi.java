package com.musa.raffi.hboschedule.service;

import com.musa.raffi.hboschedule.models.schedulepojo.ScheduleList;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Asus on 9/8/2016.
 */

public interface RestApi {
    @GET("schedule")
    Observable<ScheduleList> getScheduleList(@Query("daftar_channel") String channelName,
                                             @Query("date_now") String dateReq);

    @GET
    Observable<ScheduleList> getScheduleListByUrl(@Url String url);
}

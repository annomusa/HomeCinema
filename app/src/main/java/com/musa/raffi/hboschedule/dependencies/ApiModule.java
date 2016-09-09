package com.musa.raffi.hboschedule.dependencies;

import com.musa.raffi.hboschedule.service.RestApi;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by Asus on 9/8/2016.
 */
@Module
public class ApiModule {
    @Provides
    @CustomScope
    RestApi provideRestApi(Retrofit retrofit){
        return retrofit.create(RestApi.class);
    }
}

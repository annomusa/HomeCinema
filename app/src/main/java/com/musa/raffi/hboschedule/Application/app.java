package com.musa.raffi.hboschedule.Application;

import android.app.Application;

import com.musa.raffi.hboschedule.dependencies.ApiComponent;
import com.musa.raffi.hboschedule.dependencies.DaggerApiComponent;
import com.musa.raffi.hboschedule.dependencies.DaggerNetworkComponent;
import com.musa.raffi.hboschedule.dependencies.NetworkComponent;
import com.musa.raffi.hboschedule.dependencies.NetworkModule;
import com.musa.raffi.hboschedule.models.Constant;

import javax.annotation.Resource;

/**
 * Created by Asus on 9/8/2016.
 */

public class app extends Application {
    private ApiComponent mApiComponent;

    @Override
    public void onCreate() {
        resolveDependency();
        super.onCreate();
    }

    private void resolveDependency() {
        mApiComponent = DaggerApiComponent.builder()
                .networkComponent(getNetworkComponent())
                .build();

    }

    public NetworkComponent getNetworkComponent() {
        return DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(Constant.ROOT_URL))
                .build();
    }

    public ApiComponent getApiComponent(){
        return mApiComponent;
    }
}

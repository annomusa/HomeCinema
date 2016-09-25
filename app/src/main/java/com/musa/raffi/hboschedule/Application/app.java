package com.musa.raffi.hboschedule.Application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.musa.raffi.hboschedule.dependencies.ApiComponent;
import com.musa.raffi.hboschedule.dependencies.DaggerApiComponent;
import com.musa.raffi.hboschedule.dependencies.DaggerNetworkComponent;
import com.musa.raffi.hboschedule.dependencies.NetworkComponent;
import com.musa.raffi.hboschedule.dependencies.NetworkModule;
import com.musa.raffi.hboschedule.models.Constant;

import javax.annotation.Resource;

import static android.content.ContentValues.TAG;

/**
 * Created by Asus on 9/8/2016.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: App");
    }

    private ApiComponent mApiComponent;

    public NetworkComponent getNetworkComponent() {
        return DaggerNetworkComponent.builder()
                .networkModule(new NetworkModule(Constant.ROOT_URL))
                .build();
    }

    public static ApiComponent getApiComponent(Context context){
        App app = (App) context.getApplicationContext();
        if(app.mApiComponent == null){
            app.mApiComponent = DaggerApiComponent.builder()
                    .networkComponent(app.getNetworkComponent())
                    .build();
        }
        return app.mApiComponent;
    }
}

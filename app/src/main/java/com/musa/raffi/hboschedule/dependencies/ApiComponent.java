package com.musa.raffi.hboschedule.dependencies;

import android.app.Fragment;

import com.musa.raffi.hboschedule.schedule.MainActivity;

import dagger.Component;

/**
 * Created by Asus on 9/8/2016.
 */
@CustomScope
@Component(modules = ApiModule.class, dependencies = NetworkComponent.class)
public interface ApiComponent {
    void bind(MainActivity activity);
    void inject(Fragment activity);
}

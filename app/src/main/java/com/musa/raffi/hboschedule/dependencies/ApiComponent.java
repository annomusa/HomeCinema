package com.musa.raffi.hboschedule.dependencies;

import android.app.Fragment;

import com.musa.raffi.hboschedule.schedule.MainActivity;
import com.musa.raffi.hboschedule.schedule.PageFragment;

import dagger.Component;

/**
 * Created by Asus on 9/8/2016.
 */
@CustomScope
@Component(modules = ApiModule.class, dependencies = NetworkComponent.class)
public interface ApiComponent {
    MainActivity inject(MainActivity activity);
    PageFragment inject(PageFragment fragment);
}

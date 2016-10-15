package com.musa.raffi.hboschedule.dependencies;

import com.musa.raffi.hboschedule.MainActivity;
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

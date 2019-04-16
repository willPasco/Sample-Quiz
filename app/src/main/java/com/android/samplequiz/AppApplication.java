package com.android.samplequiz;

import android.app.Application;

import com.android.samplequiz.di.DaggerMainComponent;
import com.android.samplequiz.di.MainComponent;

public class AppApplication extends Application {

    private static MainComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
    }

    private void initDagger() {
        component = DaggerMainComponent.builder().build();
    }

    public static MainComponent getComponent() {
        return component;
    }
}

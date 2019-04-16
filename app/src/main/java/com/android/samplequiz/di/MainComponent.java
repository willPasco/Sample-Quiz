package com.android.samplequiz.di;

import com.android.samplequiz.MainActivity;

import dagger.Component;

@Component(modules = {ModelModule.class,
        ViewModelModule.class,
        RepositoryModule.class,
        NetworkModule.class})
public interface MainComponent {

    void inject(MainActivity mainActivity);
}

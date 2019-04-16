package com.android.samplequiz.di;

import android.arch.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelModule {



    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);
}

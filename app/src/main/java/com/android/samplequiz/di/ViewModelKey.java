package com.android.samplequiz.di;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import dagger.MapKey;

@Target({ElementType.METHOD})
@MapKey
@interface ViewModelKey {
    Class<? extends ViewModel> value();
}

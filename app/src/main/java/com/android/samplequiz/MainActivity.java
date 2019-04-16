package com.android.samplequiz;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.viewmodel.QuestionViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @Inject
    ViewModelProvider.Factory factory;

    QuestionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppApplication.getComponent().inject(this);

        viewModel = ViewModelProviders.of(this, factory).get(QuestionViewModel.class);
        viewModel.loadQuestion().observe(this, new Observer<DataWrapper<Question>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<Question> dataWrapper) {
                Log.i(TAG, dataWrapper.toString());
            }
        });
    }
}

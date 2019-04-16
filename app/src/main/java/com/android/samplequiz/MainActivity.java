package com.android.samplequiz;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.samplequiz.viewmodel.QuestionViewModel;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory factory;

    QuestionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppApplication.getComponent().inject(this);

        viewModel = ViewModelProviders.of(this, factory).get(QuestionViewModel.class);
        viewModel.loadQuestion();
    }
}

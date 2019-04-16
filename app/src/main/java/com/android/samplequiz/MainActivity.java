package com.android.samplequiz;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.viewmodel.QuestionViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import javax.inject.Inject;

import static com.android.samplequiz.service.QuizService.REQUEST_OK;
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @ViewById(R.id.button_action)
    Button buttonAction;

    @Inject
    ViewModelProvider.Factory factory;

    QuestionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppApplication.getComponent().inject(this);
    }

    @AfterViews
    void afterViews(){

        viewModel = ViewModelProviders.of(this, factory).get(QuestionViewModel.class);
        viewModel.loadQuestion().observe(this, new Observer<DataWrapper<Question>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<Question> dataWrapper) {
                if (dataWrapper != null) {
                    if (dataWrapper.getCode() == REQUEST_OK) {
                        showContentState();
                        configView(dataWrapper.getData());
                    } else {
                        showErrorState();
                        Log.e(TAG, String.valueOf(dataWrapper.getCode()));
                    }
                } else {
                    Log.e(TAG, "DataWrapper is null");
                    showErrorState();
                }
            }
        });
    }

    private void showContentState() {

    }

    private void showErrorState() {

    }

    private void configView(Question data) {
        Log.i(TAG, data.toString());

        enableAnswerButton(28);
    }

    private void enableAnswerButton(final int id) {
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getAnswer("6:20", id).observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean result) {
                        Log.i(TAG, String.valueOf(result));
                    }
                });
            }
        });
    }
}

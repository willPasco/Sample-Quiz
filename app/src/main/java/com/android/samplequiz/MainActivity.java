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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.viewmodel.QuestionViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import javax.inject.Inject;

import static com.android.samplequiz.service.QuizService.REQUEST_OK;
@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @ViewById(R.id.button_action)
    Button buttonAction;

    @ViewById(R.id.question_radio_group)
    RadioGroup questionRadioGroup;

    @ViewById(R.id.text_view_question)
    TextView questionTextView;

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
        viewModel.getQuestionLiveData().observe(this, new Observer<DataWrapper<Question>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<Question> dataWrapper) {
                if (dataWrapper != null) {
                    if (dataWrapper.getCode() == REQUEST_OK) {
                        if(dataWrapper.getData().getOptions() != null) {
                            showContentState();
                            configView(dataWrapper.getData());
                        }
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

        questionTextView.setText(data.getStatement());
        List<String> optionsList = data.getOptions();
        createRadioButtons(optionsList);

        Log.i(TAG, data.toString());
        enableAnswerButton(28);
    }

    private void createRadioButtons(List<String> optionsList) {
        for(String option : optionsList){
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setId(option.length());

            questionRadioGroup.addView(radioButton);
        }
    }

    private void enableAnswerButton(final int id) {

        buttonAction.setText("answer");
        buttonAction.setVisibility(View.VISIBLE);
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getAnswer("6:20", id).observe(MainActivity.this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(@Nullable Boolean result) {
                        if(result != null){
                            enableNextQuestionButton();
                            Log.i(TAG, String.valueOf(result));
                        }
                    }
                });
            }
        });
    }

    private void enableNextQuestionButton() {
        buttonAction.setText("Next");
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRadioGroup();
                viewModel.loadQuestion();
            }
        });
    }

    private void clearRadioGroup() {
        int count = questionRadioGroup.getChildCount();
        for(int i=0; i < count; i++){
            questionRadioGroup.removeViewAt(0);
        }
    }

    public void startQuiz(View view) {
        view.setVisibility(View.GONE);
        loadNewQuestion();
    }

    private void loadNewQuestion() {
        viewModel.loadQuestion();
    }

}

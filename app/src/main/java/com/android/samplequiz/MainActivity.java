package com.android.samplequiz;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.samplequiz.model.DataWrapper;
import com.android.samplequiz.model.Question;
import com.android.samplequiz.utils.SimpleIdlingResource;
import com.android.samplequiz.viewmodel.QuestionViewModel;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import static com.android.samplequiz.service.QuizService.REQUEST_OK;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int LOAD_QUESTION = 0;
    private static final int GET_ANSWER = 1;

    @Extra
    String userName;

    @ViewById(R.id.edit_text_user_name)
    EditText editTextUserName;

    @ViewById(R.id.button_action)
    Button buttonAction;

    @ViewById(R.id.button_error)
    Button buttonError;

    @ViewById(R.id.question_radio_group)
    RadioGroup questionRadioGroup;

    @ViewById(R.id.text_view_question)
    TextView questionTextView;

    @ViewById(R.id.text_view_error)
    TextView errorTextView;

    @ViewById(R.id.text_view_initial)
    TextView initTextView;

    @ViewById(R.id.progress_bar)
    ProgressBar questionProgressBar;

    @ViewById(R.id.answer_progress_bar)
    ProgressBar answerProgressBar;

    @Inject
    ViewModelProvider.Factory factory;

    QuestionViewModel viewModel;
    private int questionId;
    private SimpleIdlingResource idlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIdlingResource();
        AppApplication.getComponent().inject(this);
    }

    @AfterViews
    void afterViews() {

        if (userName != null) {
            editTextUserName.setText(userName);
        }

        viewModel = ViewModelProviders.of(this, factory).get(QuestionViewModel.class);
        viewModel.getQuestionLiveData().observe(this, new Observer<DataWrapper<Question>>() {
            @Override
            public void onChanged(@Nullable DataWrapper<Question> dataWrapper) {
                if (dataWrapper != null) {
                    if (dataWrapper.getCode() == REQUEST_OK) {
                        if (dataWrapper.getData().getOptions() != null) {
                            configView(dataWrapper.getData());
                            showContentState();
                        }
                    } else {
                        showErrorState();
                        configErrorButton(LOAD_QUESTION, 0);
                        Log.e(TAG, String.valueOf(dataWrapper.getCode()));
                    }
                } else {
                    Log.e(TAG, "DataWrapper is null");
                    showErrorState();
                    configErrorButton(LOAD_QUESTION, 0);
                }
                idlingResource.setIdleState(true);
            }
        });

        viewModel.getAnswerLivedata().observe(MainActivity.this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean result) {
                if (result != null) {
                    checkAnswer(result);
                    if (viewModel.getQuestionCount() < 10) {
                        enableNextQuestionButton();
                    } else {
                        enableResultButton();
                    }
                    unblockView();
                    showContentState();
                } else {
                    showErrorState();
                    configErrorButton(GET_ANSWER, questionId);
                }
                idlingResource.setIdleState(true);
            }
        });
    }

    private void enableResultButton() {
        buttonAction.setText(getString(R.string.result));
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResultActivity_.intent(MainActivity.this)
                        .correctAnswers(viewModel.getCorrectPoints())
                        .userName(editTextUserName.getText().toString())
                        .start();
                finish();
            }
        });
    }

    private void configErrorButton(int typeListener, final int questionId) {

        switch (typeListener) {

            case LOAD_QUESTION:
                buttonError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showContentState();
                        loadNewQuestion();
                    }
                });
                break;
            case GET_ANSWER:
                buttonError.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showContentState();
                        loadAnswer(questionId);
                    }
                });
                break;
        }
    }

    private void enableAnswerButton(final int id) {

        buttonAction.setText(getString(R.string.answer));
        loadAnswer(id);
    }

    private void loadAnswer(final int id) {
        questionId = id;
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (questionRadioGroup.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(MainActivity.this, getString(R.string.radio_not_selected), Toast.LENGTH_LONG).show();
                } else {
                    if (isOnline()) {
                        blockView();
                        String value = getRadioCheckedValue();
                        idlingResource.setIdleState(false);
                        viewModel.loadAnswer(value, id);
                    } else {
                        Toast.makeText(MainActivity.this, "Não conseguimos conectar, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void loadNewQuestion() {
        if (isOnline()) {
            idlingResource.setIdleState(false);
            viewModel.loadQuestion();
        } else {
            Toast.makeText(MainActivity.this, "Não conseguimos conectar, verifique sua conexão com a internet.", Toast.LENGTH_LONG).show();
        }
    }

    private void enableNextQuestionButton() {
        buttonAction.setText(getString(R.string.nest_question));
        buttonAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoadingState();
                clearRadioGroup();
                loadNewQuestion();
            }
        });
    }

    private void configView(Question data) {

        questionTextView.setText(data.getStatement());
        List<String> optionsList = data.getOptions();
        createRadioButtons(optionsList);

        Log.i(TAG, data.toString());
        enableAnswerButton(data.getId());

    }

    private void createRadioButtons(List<String> optionsList) {
        for (int i = 0; i < optionsList.size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(optionsList.get(i));
            radioButton.setId(i);
            radioButton.setPadding(0, 16, 16, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                radioButton.setGravity(View.TEXT_ALIGNMENT_CENTER);
            }

            questionRadioGroup.addView(radioButton);
        }
    }

    private void checkAnswer(Boolean result) {
        int checkedId = questionRadioGroup.getCheckedRadioButtonId();
        View checkedRadio = questionRadioGroup.findViewById(checkedId);

        if (result) {
            viewModel.increaseCorrectPoint();
            checkedRadio.setBackground(getResources().getDrawable(R.drawable.background_correct_answer));
        } else {
            checkedRadio.setBackground(getResources().getDrawable(R.drawable.background_wrong_answer));
        }
    }

    private void clearRadioGroup() {
        questionRadioGroup.clearCheck();
        int count = questionRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            questionRadioGroup.removeViewAt(0);
        }
    }

    private String getRadioCheckedValue() {
        int id = questionRadioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = questionRadioGroup.findViewById(id);
        return radioButton.getText().toString();
    }

    public void startQuiz(View view) {
        String userName = editTextUserName.getText().toString();

        if (userName.length() <= 0) {
            Toast.makeText(this, getString(R.string.user_empty), Toast.LENGTH_LONG).show();
        } else {
            editTextUserName.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            showLoadingState();
            loadNewQuestion();
        }
    }

    private void blockView() {
        int count = questionRadioGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            RadioButton radioButton = (RadioButton) questionRadioGroup.getChildAt(i);
            radioButton.setEnabled(false);
        }
        buttonAction.setVisibility(View.GONE);
        answerProgressBar.setVisibility(View.VISIBLE);
    }

    private void unblockView() {
        answerProgressBar.setVisibility(View.GONE);
    }

    private void showLoadingState() {

        questionTextView.setVisibility(View.GONE);
        questionRadioGroup.setVisibility(View.GONE);
        buttonAction.setVisibility(View.GONE);
        initTextView.setVisibility(View.GONE);
        buttonError.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        questionProgressBar.setVisibility(View.VISIBLE);
    }

    private void showContentState() {

        questionTextView.setVisibility(View.VISIBLE);
        questionRadioGroup.setVisibility(View.VISIBLE);
        buttonAction.setVisibility(View.VISIBLE);
        initTextView.setVisibility(View.GONE);
        buttonError.setVisibility(View.GONE);
        errorTextView.setVisibility(View.GONE);
        questionProgressBar.setVisibility(View.GONE);
    }

    private void showErrorState() {

        questionTextView.setVisibility(View.GONE);
        questionRadioGroup.setVisibility(View.GONE);
        buttonAction.setVisibility(View.GONE);
        initTextView.setVisibility(View.GONE);
        buttonError.setVisibility(View.VISIBLE);
        errorTextView.setVisibility(View.VISIBLE);
        questionProgressBar.setVisibility(View.GONE);
    }

    private boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    @VisibleForTesting
    public IdlingResource getIdlingResource(){
        if(idlingResource == null){
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

}

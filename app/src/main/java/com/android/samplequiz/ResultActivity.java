package com.android.samplequiz;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_result)
public class ResultActivity extends AppCompatActivity {

    @Extra
    int correctAnswers;

    @Extra
    String userName;

    @ViewById(R.id.text_view_congrats)
    TextView textViewCongrats;

    @ViewById(R.id.text_view_result)
    TextView textViewResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppApplication.getComponent().inject(this);
    }

    @AfterViews
    void afterViews(){

        textViewResult.setText(String.valueOf(correctAnswers));
        String congratsText = "Parabéns! "+userName+ " "+getString(R.string.congrats_text);
        textViewCongrats.setText(congratsText);

    }

    public void restartQuiz(View view) {
        MainActivity_.intent(this).userName(userName).start();
        finish();
    }
}

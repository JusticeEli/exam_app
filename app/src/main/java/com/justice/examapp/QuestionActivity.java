package com.justice.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {
    private TextView questionTxtView;

    private RadioGroup radioGroup;
    private RadioButton aRadioButton;
    private RadioButton bRadioButton;
    private RadioButton cRadioButton;
    private RadioButton dRadioButton;
    private Button editBtn;
    private QuestionData questionDataOriginal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        questionDataOriginal = ApplicationClass.documentSnapshot.toObject(QuestionData.class);
        initWidgets();
        setOnClickListeners();
        setDefaultValues();
    }

    private void setDefaultValues() {
        questionTxtView.setText(questionDataOriginal.getQuestion());
        aRadioButton.setText(questionDataOriginal.getFirstChoice());
        bRadioButton.setText(questionDataOriginal.getSecondChoice());
        cRadioButton.setText(questionDataOriginal.getThirdChoice());
        dRadioButton.setText(questionDataOriginal.getForthChoice());

        setTheAnswerForQuestion();

    }

    private void setTheAnswerForQuestion() {

        if (questionDataOriginal.getAnswer().equals(questionDataOriginal.getFirstChoice())) {
            aRadioButton.setChecked(true);
        } else if (questionDataOriginal.getAnswer().equals(questionDataOriginal.getSecondChoice())) {
            bRadioButton.setChecked(true);
        } else if (questionDataOriginal.getAnswer().equals(questionDataOriginal.getThirdChoice())) {
            cRadioButton.setChecked(true);

        } else if (questionDataOriginal.getAnswer().equals(questionDataOriginal.getForthChoice())) {
            dRadioButton.setChecked(true);
        }
    }

    private void setOnClickListeners() {
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QuestionActivity.this, QuestionAddActivity.class));
                onBackPressed();
            }
        });
    }

    private void initWidgets() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        questionTxtView = findViewById(R.id.questionTxtView);
        radioGroup = findViewById(R.id.radioGroup);
        aRadioButton = findViewById(R.id.aRadioBtn);
        bRadioButton = findViewById(R.id.bRadioBtn);
        cRadioButton = findViewById(R.id.cRadioBtn);
        dRadioButton = findViewById(R.id.dRadioBtn);
        editBtn = findViewById(R.id.editBtn);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.clear_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteMenu) {
            ApplicationClass.documentSnapshot.getReference().delete();
            finish();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        updateAnswer();
        super.onBackPressed();
    }

    private void updateAnswer() {
        RadioButton radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        String answer = radioButton.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("answer", answer);
        ApplicationClass.documentSnapshot.getReference().set(map, SetOptions.merge());
    }
}

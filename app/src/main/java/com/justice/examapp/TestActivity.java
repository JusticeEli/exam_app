package com.justice.examapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;

public class TestActivity extends AppCompatActivity {
    private TextView questionTxtView;

    private RadioGroup radioGroup;
    private RadioButton aRadioButton;
    private RadioButton bRadioButton;
    private RadioButton cRadioButton;
    private RadioButton dRadioButton;
    private ImageView nextImageView;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initWidgets();
        setOnClickListeners();
        setDefaultValues();

    }

    private void setOnClickListeners() {
        nextImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateMarks();
                updateQuestion();
                if (ApplicationClass.questionList.size() == position + 1) {
                    startActivity(new Intent(TestActivity.this, TestCompleteActivity.class));
                }


                position++;//go to the next question
            }
        });
    }

    private void updateQuestion() {
        setDefaultValues();
    }

    private void updateMarks() {
        RadioButton radioButton=findViewById(radioGroup.getCheckedRadioButtonId());
        if (ApplicationClass.questionList.get(position).getAnswer().equals(radioButton.getText().toString())){
            ApplicationClass.marks++;
            Toasty.success(this, "Correct", Toast.LENGTH_SHORT, true).show();

        }else {
            Toasty.error(this, "Wrong", Toast.LENGTH_SHORT, true).show();

        }
    }

    private void setDefaultValues() {
        questionTxtView.setText(ApplicationClass.questionList.get(position).getQuestion());
        aRadioButton.setText(ApplicationClass.questionList.get(position).getFirstChoice());
        bRadioButton.setText(ApplicationClass.questionList.get(position).getSecondChoice());
        cRadioButton.setText(ApplicationClass.questionList.get(position).getThirdChoice());
        dRadioButton.setText(ApplicationClass.questionList.get(position).getForthChoice());

    }

    private void initWidgets() {
        questionTxtView = findViewById(R.id.questionTxtView);
        radioGroup = findViewById(R.id.radioGroup);
        aRadioButton = findViewById(R.id.aRadioBtn);
        bRadioButton = findViewById(R.id.bRadioBtn);
        cRadioButton = findViewById(R.id.cRadioBtn);
        dRadioButton = findViewById(R.id.dRadioBtn);
        nextImageView = findViewById(R.id.nextImageView);

    }
}

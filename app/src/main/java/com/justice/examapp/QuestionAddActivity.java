package com.justice.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class QuestionAddActivity extends AppCompatActivity {
    private ProgressBar progressBar;

    private TextInputLayout questionEdtTxt;
    private TextInputLayout aChoiceEdtTxt;
    private TextInputLayout bChoiceEdtTxt;
    private TextInputLayout cChoiceEdtTxt;
    private TextInputLayout dChoiceEdtTxt;
    private Button saveBtn;

    private boolean updating = false;
    private QuestionData questionDataOriginal;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);
        initWidgets();
        setOnClickListeners();
        check_if_question_is_being_updated();

    }

    private void check_if_question_is_being_updated() {
        if (ApplicationClass.documentSnapshot != null) {
            questionDataOriginal = ApplicationClass.documentSnapshot.toObject(QuestionData.class);
            updating = true;
            setDefaultValues();
        }
    }

    private void setDefaultValues() {
        questionEdtTxt.getEditText().setText(questionDataOriginal.getQuestion());
        aChoiceEdtTxt.getEditText().setText(questionDataOriginal.getFirstChoice());
        bChoiceEdtTxt.getEditText().setText(questionDataOriginal.getSecondChoice());
        cChoiceEdtTxt.getEditText().setText(questionDataOriginal.getThirdChoice());
        dChoiceEdtTxt.getEditText().setText(questionDataOriginal.getForthChoice());

    }

    private void setOnClickListeners() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBtn.setVisibility(View.GONE);
                saveData();
            }
        });
    }

    private void saveData() {
        QuestionData questionData = new QuestionData();
        String question = questionEdtTxt.getEditText().getText().toString().trim();
        String first = aChoiceEdtTxt.getEditText().getText().toString().trim();
        String second = bChoiceEdtTxt.getEditText().getText().toString().trim();
        String third = cChoiceEdtTxt.getEditText().getText().toString().trim();
        String four = dChoiceEdtTxt.getEditText().getText().toString().trim();

        if (question.isEmpty() || first.isEmpty() || second.isEmpty()) {
            Toasty.error(this, "Please fill fields", Toast.LENGTH_SHORT).show();
            return;
        }

        questionData.setQuestion(question);
        questionData.setFirstChoice(first);
        questionData.setSecondChoice(second);
        questionData.setThirdChoice(third);
        questionData.setForthChoice(four);

        Map<String, Object> map = null;
        map = new HashMap<>();
        map.put("question", questionData.getQuestion());
        map.put("firstChoice", questionData.getFirstChoice());
        map.put("secondChoice", questionData.getSecondChoice());
        map.put("thirdChoice", questionData.getThirdChoice());
        map.put("forthChoice", questionData.getForthChoice());
        map.put("answer", questionData.getAnswer());
        map.put("date", FieldValue.serverTimestamp());

        saveDataInDatabase(questionData, map);


    }

    private void saveDataInDatabase(QuestionData questionData, Map map) {
        if (updating) {
            progressBar.setVisibility(View.VISIBLE);

            ApplicationClass.documentSnapshot.getReference().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toasty.success(QuestionAddActivity.this, "Question saved ", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(QuestionAddActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        } else {
            questionData.setAnswer(questionData.getFirstChoice());
            map.put("answer", questionData.getFirstChoice());
            progressBar.setVisibility(View.VISIBLE);
            firebaseFirestore.collection("questions").add(questionData).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if (task.isSuccessful()) {
                        startQuestionActivity(task);

                    } else {
                        Toast.makeText(QuestionAddActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                    progressBar.setVisibility(View.GONE);
                }
            });
        }


    }

    private void startQuestionActivity(Task<DocumentReference> task) {
        progressBar.setVisibility(View.VISIBLE);
        task.getResult().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    ApplicationClass.documentSnapshot = task.getResult();
                    Toasty.success(QuestionAddActivity.this, "Question saved ", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(QuestionAddActivity.this, QuestionActivity.class));
                    finish();

                } else {
                    Toast.makeText(QuestionAddActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initWidgets() {
        progressBar = findViewById(R.id.progressBar);
        questionEdtTxt = findViewById(R.id.questionEdtTxt);
        aChoiceEdtTxt = findViewById(R.id.achoiceEdtTxt);
        bChoiceEdtTxt = findViewById(R.id.bchoiceEdtTxt);
        cChoiceEdtTxt = findViewById(R.id.cchoiceEdtTxt);
        dChoiceEdtTxt = findViewById(R.id.dchoiceEdtTxt);
        saveBtn = findViewById(R.id.saveBtn);

    }
}

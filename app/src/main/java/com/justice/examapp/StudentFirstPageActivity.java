package com.justice.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class StudentFirstPageActivity extends AppCompatActivity {
    private TextInputLayout firstNameEdtTxt;
    private TextInputLayout lastNameEdtTxt;
    private TextInputLayout idEdtTxt;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_first_page);
        initWidgets();
        setOnClickListeners();
    }

    private void setOnClickListeners() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitButtonClicked();
            }
        });
    }

    private void onSubmitButtonClicked() {
        String firstName = firstNameEdtTxt.getEditText().getText().toString().trim();
        String lastName = lastNameEdtTxt.getEditText().getText().toString().trim();
        String id = idEdtTxt.getEditText().getText().toString().trim();

        if (field_is_empty(firstName, lastName, id)) {
            return;
        }

        ApplicationClass.student = new Student(firstName, lastName, id, 0);

        get_all_the_questions_and_start_the_test();
        startActivity(new Intent(this, TestActivity.class));


    }

    private void get_all_the_questions_and_start_the_test() {
        ApplicationClass.questionList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ApplicationClass.questionList.add(documentSnapshot.toObject(QuestionData.class));
                    }

                } else {
                    Toast.makeText(StudentFirstPageActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private boolean field_is_empty(String firstName, String lastName, String id) {
        if (firstName.isEmpty()) {
            firstNameEdtTxt.setError("Please Fill Field");
            firstNameEdtTxt.requestFocus();
            return true;
        }
        if (lastName.isEmpty()) {
            lastNameEdtTxt.setError("Please Fill Field");
            lastNameEdtTxt.requestFocus();
            return true;
        }
        if (id.isEmpty()) {
            idEdtTxt.setError("Please Fill Field");
            idEdtTxt.requestFocus();
            return true;
        }
        return false;
    }

    private void initWidgets() {
        firstNameEdtTxt = findViewById(R.id.firstNameEdtTxt);
        lastNameEdtTxt = findViewById(R.id.lastNameEdtTxt);
        idEdtTxt = findViewById(R.id.idEdtTxt);
        submitBtn = findViewById(R.id.submitBtn);

    }
}

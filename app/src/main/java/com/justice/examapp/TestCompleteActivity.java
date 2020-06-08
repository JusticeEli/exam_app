package com.justice.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestCompleteActivity extends AppCompatActivity {
    private TextView resultsTxtView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_complete);
        resultsTxtView = findViewById(R.id.resultsTxtView);
        resultsTxtView.setText(ApplicationClass.marks+" out of "+ApplicationClass.questionList.size());
        ApplicationClass.student.setOutOf(ApplicationClass.questionList.size());
        ApplicationClass.student.setMarks(ApplicationClass.marks);
        saveMarksInDatabase();
    }

    private void saveMarksInDatabase() {
        FirebaseFirestore.getInstance().collection("marks").add(ApplicationClass.student).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()){
                    Toast.makeText(TestCompleteActivity.this, "Success saving marks", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(TestCompleteActivity.this, "Error: "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

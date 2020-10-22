package com.justice.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TestCompleteActivity extends AppCompatActivity {
    public static final String COLLECTION_RESULTS = "results";
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String currentUserId;

    private String quizId;

    private TextView resultCorrect;
    private TextView resultWrong;
    private TextView resultMissed;

    private TextView resultPercent;
    private ProgressBar resultProgress;

    private Button resultHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_complete);
        checkIfUserIsLoggedIn();
        initWidgets();
        init_firestore();
        setOnClickListeners();
        getResults();
    }

    private void getResults() { //Get Results
        firebaseFirestore
                .collection(COLLECTION_RESULTS)
                .document(FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful() && task.getResult().exists()) {
                    Student student = task.getResult().toObject(Student.class);
                    Results results = student.getResults();
                    Long correct = results.getCorrect();
                    Long wrong = results.getWrong();
                    Long missed = results.getUnanswered();

                    resultCorrect.setText(correct.toString());
                    resultWrong.setText(wrong.toString());
                    resultMissed.setText(missed.toString());

                    //Calculate Progress
                    Long total = correct + wrong + missed;
                    Long percent = (correct * 100) / total;

                    resultPercent.setText(percent + "%");
                    resultProgress.setProgress(percent.intValue());
                }
            }
        });
    }

    private void setOnClickListeners() {
        resultHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(TestCompleteActivity.this, StudentFirstPageActivity.class));
            }
        });

    }

    private void initWidgets() {  //Initialize UI Elements
        resultCorrect = findViewById(R.id.results_correct_text);
        resultWrong = findViewById(R.id.results_wrong_text);
        resultMissed = findViewById(R.id.results_missed_text);

        resultHomeBtn = findViewById(R.id.results_home_btn);
        resultPercent = findViewById(R.id.results_percent);
        resultProgress = findViewById(R.id.results_progress);

    }

    private void init_firestore() {
        firebaseFirestore = FirebaseFirestore.getInstance();

    }

    private void checkIfUserIsLoggedIn() {
        firebaseAuth = FirebaseAuth.getInstance();

        //Get User ID
        if (firebaseAuth.getCurrentUser() != null) {
            currentUserId = firebaseAuth.getCurrentUser().getUid();
        } else {
            //Go Back to Home Page
            finish();
        }

    }


}


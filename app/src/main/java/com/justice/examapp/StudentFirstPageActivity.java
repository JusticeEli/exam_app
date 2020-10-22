package com.justice.examapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;

import static com.justice.examapp.QuestionAddActivity.COLLECTION_QUESTIONS;

public class StudentFirstPageActivity extends AppCompatActivity {
    private static final String TAG = "StudentFirstPageActivit";
    public static final String COLLECTION_STUDENTS="students";
    public static final int RC_SIGN_IN = 6;
    private TextInputLayout firstNameEdtTxt;
    private TextInputLayout lastNameEdtTxt;
    private TextInputLayout idEdtTxt;
    private Button submitBtn;
    private Button logoutBtn;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_first_page);
        setUplogin();
    }

    private void setUplogin() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // already signed in
            alreadySignedIn();

        } else {
            // not signed in
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.PhoneBuilder().build(),
                                    new AuthUI.IdpConfig.AnonymousBuilder().build()))
                            .setTosAndPrivacyPolicyUrls("https://www.linkedin.com/in/JusticeEli", "https://github.com/JusticeEli/")
                            .build(),
                    RC_SIGN_IN);


        }
    }

    private void alreadySignedIn() {
        initWidgets();
        setOnClickListeners();

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                alreadySignedIn();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showToast(response.getError().getMessage());
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showToast("no internet connection");
                    return;
                }

                showToast("unknown error");
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    private void showToast(String message) {
        Toasty.error(this, message).show();
    }

    private void setOnClickListeners() {
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSubmitButtonClicked();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutBtnClicked();
            }
        });

    }

    private void onLogoutBtnClicked() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        // user is now signed out
                        Toasty.success(StudentFirstPageActivity.this, "logout successful").show();
                        finish();
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

        ApplicationClass.student = new Student(firstName, lastName);
        uploadStudentData();

        get_all_the_questions_and_start_the_test();


    }

    private void uploadStudentData() {
        FirebaseFirestore.getInstance().collection(COLLECTION_STUDENTS).document(FirebaseAuth.getInstance().getUid()).set(ApplicationClass.student).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                 Toasty.success(StudentFirstPageActivity.this,"Student data uploaded successfully").show();
                }else {
                    Toasty.error(StudentFirstPageActivity.this,task.getException().getMessage()).show();
                }
            }
        });
    }

    private void get_all_the_questions_and_start_the_test() {
        ApplicationClass.questionList = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        submitBtn.setVisibility(View.GONE);
        FirebaseFirestore.getInstance().collection(COLLECTION_QUESTIONS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        ApplicationClass.questionList.add(documentSnapshot.toObject(QuestionModel.class));
                    }

                    startActivity(new Intent(StudentFirstPageActivity.this, TestActivity.class));

                } else {
                    Toast.makeText(StudentFirstPageActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
                submitBtn.setVisibility(View.VISIBLE);

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
        logoutBtn = findViewById(R.id.logoutBtn);
        progressBar = findViewById(R.id.progressBar);

    }
}

package com.justice.examapp;

import android.app.Application;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class ApplicationClass extends Application {
    public static DocumentSnapshot documentSnapshot;
    public static Student student;
    public static int marks;

    public static List<QuestionModel> questionList;

}

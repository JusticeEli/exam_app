package com.justice.examapp;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class StudentsMarksAdapter extends FirestoreRecyclerAdapter<Student, StudentsMarksAdapter.ViewHolder> {
    private Context context;

    public StudentsMarksAdapter(Context context, @NonNull FirestoreRecyclerOptions<Student> options) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Student model) {
        holder.studentNameTxtView.setText(model.getFirstName()+" "+model.getLastName());
        holder.studentIdTxtView.setText(model.getId());
        holder.studentMarksTxtView.setText(model.getMarks()+" out of "+model.getOutOf());

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_students_marks, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView studentNameTxtView;
        private TextView studentIdTxtView;
        private TextView studentMarksTxtView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentNameTxtView = itemView.findViewById(R.id.studentNameTxtView);
            studentIdTxtView = itemView.findViewById(R.id.studentIdTxtView);
            studentMarksTxtView = itemView.findViewById(R.id.studentsMarksTxtView);

        }


    }


}

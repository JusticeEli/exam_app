package com.justice.examapp;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class TeacherFirstPageAdapter extends FirestoreRecyclerAdapter<QuestionData, TeacherFirstPageAdapter.ViewHolder> {
    private Context context;

    public TeacherFirstPageAdapter(Context context, @NonNull FirestoreRecyclerOptions<QuestionData> options) {
        super(options);
        this.context = context;

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final QuestionData model) {
        /**
         *   new Handler().postDelayed(new Runnable() {
         *             @Override
         *             public void run() {
         *                 String date = new SimpleDateFormat("HH:mm\ndd-MM-yyy").format(model.getDate());
         *                 holder.dateTxtView.setText(date);
         *
         *             }
         *         }, 100);
         */


        holder.questionTxtView.setText(model.getQuestion());
        holder.answerEdtTxt.setText(model.getAnswer());

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_first_page, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView questionTxtView;
        private TextView answerEdtTxt;

        private TextView dateTxtView;

        private View itemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            questionTxtView = itemView.findViewById(R.id.questionTxtView);
            answerEdtTxt = itemView.findViewById(R.id.answerEdtTxt);
            dateTxtView = itemView.findViewById(R.id.dateTxtView);

            setOnClickListeners();

        }

        private void setOnClickListeners() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplicationClass.documentSnapshot = getSnapshots().getSnapshot(getAdapterPosition());
                    context.startActivity(new Intent(context, QuestionActivity.class));
                }
            });

        }

    }


}

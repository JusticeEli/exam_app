package com.justice.examapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.flaviofaria.kenburnsview.KenBurnsView;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onClick(View view){
        if (view.getId()==R.id.studentImageView){
            startActivity(new Intent(this,StudentFirstPageActivity.class));
        }else {
            startActivity(new Intent(this,TeacherFirstPageActivity.class));

        }
    }
}

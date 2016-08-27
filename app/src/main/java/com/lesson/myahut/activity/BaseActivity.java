package com.lesson.myahut.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by qidunwei on 2016/1/14.
 */
public class BaseActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void openActivity(Class<?> cls) {
        Intent i = new Intent(this, cls);
        startActivity(i);
    }

}

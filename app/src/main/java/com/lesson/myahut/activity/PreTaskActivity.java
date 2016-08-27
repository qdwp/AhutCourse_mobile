package com.lesson.myahut.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.PreScoreSerializable;
import com.lesson.myahut.entity.PreTaskSerializable;
import com.lesson.myahut.handler.ViewScoreHandler;
import com.lesson.myahut.handler.ViewTaskHandler;
import com.lesson.myahut.util.Util;

public class PreTaskActivity extends AppCompatActivity {
    private EditText textTaskUid;
    private EditText textTaskPwd;
    private Spinner spinnerXn;
    private Spinner spinnerXq;
    private Button taskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_task);

        /* 添加新版本的 actionbar 功能，并显示返回按钮 */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        textTaskUid = (EditText) findViewById(R.id.task_uid);
        textTaskPwd = (EditText) findViewById(R.id.task_pwd);
        spinnerXn = (Spinner) findViewById(R.id.task_xn);
        spinnerXq = (Spinner) findViewById(R.id.task_xq);
        taskBtn = (Button) findViewById(R.id.taskBtn);

        ViewTaskHandler.getInstance(PreTaskActivity.this).initTimeList();
        ArrayAdapter<String> arrayAdapterXn = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ViewTaskHandler.getInstance(PreTaskActivity.this).yearList);
        ArrayAdapter<String> arrayAdapterXq = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ViewTaskHandler.getInstance(PreTaskActivity.this).monthList);
        arrayAdapterXn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterXq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerXn.setAdapter(arrayAdapterXn);
        spinnerXq.setAdapter(arrayAdapterXq);

        taskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = textTaskUid.getText().toString();
                String pwd = textTaskPwd.getText().toString();
                String xnd = spinnerXn.getSelectedItem().toString();
                String xqd = spinnerXq.getSelectedItem().toString();

                if (uid.length() == 0 || pwd.length() == 0) {
                    Util.makeToast(PreTaskActivity.this, "请输入学号或密码");
                    return;
                }
                if (!Util.isOnline(PreTaskActivity.this)) {
                    Util.makeToast(PreTaskActivity.this, "请检查网络链接");
                    return;
                }
                /* 将数据序列化后传入到下一个 Activity */
                PreTaskSerializable preTaskSerializable = new PreTaskSerializable(uid, pwd, xnd, xqd);
                Bundle bundle = new Bundle();
                bundle.putSerializable("preTaskSerializable", preTaskSerializable);
                Intent intent = new Intent(PreTaskActivity.this, ViewTaskActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    
    /**
     * 捕获 actionbar 返回按钮点击事件，并实现回退功能
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}

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
import com.lesson.myahut.entity.PreMakeUpSerializable;
import com.lesson.myahut.entity.PreTaskSerializable;
import com.lesson.myahut.handler.ViewMakeUpHandler;
import com.lesson.myahut.handler.ViewTaskHandler;
import com.lesson.myahut.util.Util;

public class PreMakeUpActivity extends AppCompatActivity {
    private EditText textMakeUpUid;
    private EditText textMakeUpPwd;
    private Spinner spinnerXn;
    private Spinner spinnerXq;
    private Button makeupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_makeup);

        /* 添加新版本的 actionbar 功能，并显示返回按钮 */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        textMakeUpUid = (EditText) findViewById(R.id.makeup_uid);
        textMakeUpPwd = (EditText) findViewById(R.id.makeup_pwd);
        spinnerXn = (Spinner) findViewById(R.id.makeup_xn);
        spinnerXq = (Spinner) findViewById(R.id.makeup_xq);
        makeupBtn = (Button) findViewById(R.id.makeupBtn);

        ViewMakeUpHandler.getInstance(PreMakeUpActivity.this).initTimeList();
        ArrayAdapter<String> arrayAdapterXn = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ViewMakeUpHandler.getInstance(PreMakeUpActivity.this).yearList);
        ArrayAdapter<String> arrayAdapterXq = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ViewMakeUpHandler.getInstance(PreMakeUpActivity.this).monthList);
        arrayAdapterXn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterXq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerXn.setAdapter(arrayAdapterXn);
        spinnerXq.setAdapter(arrayAdapterXq);

        makeupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = textMakeUpUid.getText().toString();
                String pwd = textMakeUpPwd.getText().toString();
                String xnd = spinnerXn.getSelectedItem().toString();
                String xqd = spinnerXq.getSelectedItem().toString();

                if (uid.length() == 0 || pwd.length() == 0) {
                    Util.makeToast(PreMakeUpActivity.this, "请输入学号或密码");
                    return;
                }
                if (!Util.isOnline(PreMakeUpActivity.this)) {
                    Util.makeToast(PreMakeUpActivity.this, "请检查网络链接");
                    return;
                }

                /* 将数据序列化后传入到下一个 Activity */
                PreMakeUpSerializable preMakeUpSerializable = new PreMakeUpSerializable(uid, pwd, xnd, xqd);
                Bundle bundle = new Bundle();
                bundle.putSerializable("preMakeUpSerializable", preMakeUpSerializable);
                Intent intent = new Intent(PreMakeUpActivity.this, ViewMakeUpActivity.class);
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

package com.lesson.myahut.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.PreScoreSerializable;
import com.lesson.myahut.handler.ViewScoreHandler;
import com.lesson.myahut.util.Util;

public class PreScoreActivity extends BaseActivity {
    private EditText textScoreUid;
    private EditText textScoreTid;
    private Spinner spinnerXn;
    private Spinner spinnerXq;
    private Button scoreBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_score);

        /* 添加新版本的 actionbar 功能，并显示返回按钮 */
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        textScoreUid = (EditText) findViewById(R.id.score_uid);
        textScoreTid = (EditText) findViewById(R.id.score_tid);
        spinnerXn = (Spinner) findViewById(R.id.score_xn);
        spinnerXq = (Spinner) findViewById(R.id.score_xq);
        scoreBtn = (Button) findViewById(R.id.scoreBtn);

        ViewScoreHandler.getInstance(PreScoreActivity.this).initTimeList();
        ArrayAdapter<String> arrayAdapterXn = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ViewScoreHandler.getInstance(PreScoreActivity.this).yearList);
        ArrayAdapter<String> arrayAdapterXq = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                ViewScoreHandler.getInstance(PreScoreActivity.this).monthList);
        arrayAdapterXn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        arrayAdapterXq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerXn.setAdapter(arrayAdapterXn);
        spinnerXq.setAdapter(arrayAdapterXq);

        scoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uid = textScoreUid.getText().toString();
                String tid = textScoreTid.getText().toString();
                String drop_xn = spinnerXn.getSelectedItem().toString();
                String drop_xq = spinnerXq.getSelectedItem().toString();

                if (uid.length() == 0 || tid.length() == 0) {
                    Util.makeToast(PreScoreActivity.this, "请输入学号或身份证号");
                    return;
                }
                if (!Util.isOnline(PreScoreActivity.this)) {
                    Util.makeToast(PreScoreActivity.this, "请检查网络链接");
                    return;
                }
                /* 将数据序列化后传入到下一个 Activity */
                PreScoreSerializable preScoreSerializable = new PreScoreSerializable(uid, tid, drop_xn, drop_xq);
                Bundle bundle = new Bundle();
                bundle.putSerializable("preScoreSerializable", preScoreSerializable);
                Intent intent = new Intent(PreScoreActivity.this, ViewScoreActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }


    /**
     * 捕获 actionbar 返回按钮点击事件，并实现回退功能
     *
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

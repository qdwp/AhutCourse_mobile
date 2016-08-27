package com.lesson.myahut.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.UserInfo;
import com.lesson.myahut.handler.SettingManager;
import com.lesson.myahut.handler.TimeTable;
import com.lesson.myahut.handler.UpdateHandler;
import com.lesson.myahut.handler.UserManager;
import com.lesson.myahut.ui.GridView;
import com.lesson.myahut.util.ChangeLog;
import com.lesson.myahut.util.GlobalContext;
import com.lesson.myahut.util.Util;


public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridView;
    private TextView tvDateInfo;

    public static boolean needRefresh = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalContext.mainActivity = this;
        /**
         * 如果未登录
         */
        UserManager userManager = UserManager.getInstance(this);
        if (!userManager.hasLocalUser()) {
//            openActivity(LoginActivity.class);
//            finish();
//            return;
            LoginActivity.getInstance().showLoginDialog();
        }

        setContentView(R.layout.activity_main);
        /**
         * 设置标题栏
         */
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * 标题栏按钮显示，用于打开侧边栏
         * 注释后不显示按钮，但侧边栏可以划开
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /**
         * 执行侧边栏点击后的操作
         * 注释后，点击侧边栏菜单不响应
         */
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /**
         * 显示日期信息，与标题栏无关
         */
        refreshDateInfo();
        loadView();
        loadLocalInfo();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (needRefresh) {
            refresh();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        needRefresh = true;
    }

    @Override
    public void onBackPressed() {
        /**
         * 如果侧边栏处于打开状态，按返回键则先关闭侧边栏
         * 否则直接执行正常的返回按钮功能
         */
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_begin_time) {
            SettingManager.getInstance().showSetBeginTimeDialog();
            return true;
        } else if (id == R.id.action_setting_winter) {
            final String[] arrayFruit = new String[]{"冬季时间"};
            final boolean[] arrayFruitSelected = new boolean[]{SettingManager.getInstance().getWinter()};

            Dialog alertDialog = new AlertDialog.Builder(this)
                    .setTitle("设置当前课程季节")
                    .setMultiChoiceItems(arrayFruit, arrayFruitSelected, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            arrayFruitSelected[which] = isChecked;
                        }
                    })
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i = 0; i < arrayFruitSelected.length; i++) {
                                if (arrayFruitSelected[0] == true) {
                                    SettingManager.getInstance().setWinter(true);
                                } else {
                                    SettingManager.getInstance().setWinter(false);
                                }
                            }
                            /* 刷新当前视图 */
                            MainActivity.this.refreshView();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    })
                    .create();
            alertDialog.show();
            return true;
        } else if (id == R.id.action_logout) {
            new AlertDialog.Builder(this)
                    .setTitle("是否确认注销本次登录？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (UserManager.getInstance(MainActivity.this).logoutUser()) {
                                refreshView();
                                refreshActivity();
                            } else {
                                Util.makeToast(MainActivity.this, "注销用户失败");
                            }
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    }).create().show();
            return true;
        } else if (id == R.id.action_update) {
            if (!Util.isOnline(GlobalContext.mainActivity)) {
                Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                return false;
            }
            UpdateHandler updateHandler = new UpdateHandler();
            updateHandler.Check();
            return true;
        } else if (id == R.id.action_about) {
            ChangeLog changeLog = new ChangeLog(GlobalContext.mainActivity);
            changeLog.getFullLogDialog().show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_scoreInfo) {
            if (!Util.isOnline(GlobalContext.mainActivity)) {
//                Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                return false;
            }
            openActivity(PreScoreActivity.class);
        } else if (id == R.id.nav_taskInfo) {
            if (!Util.isOnline(GlobalContext.mainActivity)) {
//                Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                return false;
            }
            openActivity(PreTaskActivity.class);
        } else if (id == R.id.nav_makeup) {
            if (!Util.isOnline(GlobalContext.mainActivity)) {
//                Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                return false;
            }
            openActivity(PreMakeUpActivity.class);
        } else if (id == R.id.nav_avgPoint) {
            if (!Util.isOnline(GlobalContext.mainActivity)) {
//                Util.makeToast(GlobalContext.mainActivity, "请检查网络链接");
                return false;
            }
            openActivity(PreAvgPointActivity.class);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 执行数据刷新
     */
    public void refresh() {
        if (tvDateInfo != null) refreshDateInfo();
        if (gridView != null) gridView.refreshView();
    }

    /**
     * 刷新当前Activity
     */
    public void refreshActivity() {

        MainActivity.this.finish();
        openActivity(MainActivity.class);
    }

    public void loadView() {
        /**
         * 加载课表的表格布局，显示在主页面
         */
        gridView = new GridView(this, null);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.my_relativelayout);
        layout.addView(gridView);
    }

    public void refreshView() {
        gridView.refreshLessons();
        gridView.refreshTimeTable(SettingManager.getInstance().getWinter());
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.my_relativelayout);
        layout.removeAllViews();
        layout.addView(gridView);
    }

    /**
     * 更新标题栏时间信息
     *
     * @return
     */
    public void refreshDateInfo() {
        String info;
        TimeTable timetable = TimeTable.getInstance(this);
        timetable.refreshNumOfWeek();
        int numOfWeek = timetable.numOfWeek;
        if (numOfWeek > 0) {
            info = "第" + String.valueOf(numOfWeek) + "周" + " "
                    + timetable.weekName[timetable.weekDay];
        } else {
            info = "未开学" + " " + timetable.weekName[timetable.weekDay];
        }
        tvDateInfo = (TextView) findViewById(R.id.tvCumstomView);
        tvDateInfo.setText(info);
    }

    /**
     * 加载本地用户信息，显示在左侧边栏
     */
    public void loadLocalInfo() {
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        UserInfo userInfo = new UserInfo();
        userInfo.loadLocalUser(GlobalContext.mainActivity);
        textView1.setText(userInfo.getUserXM());
        textView2.setText(userInfo.getUserXY() + " " + userInfo.getUserBJ());

        return;
    }
}

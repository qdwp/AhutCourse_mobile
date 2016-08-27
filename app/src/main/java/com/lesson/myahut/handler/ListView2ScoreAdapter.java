package com.lesson.myahut.handler;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.ViewScoreInfo;
import com.lesson.myahut.util.GlobalContext;

/**
 * Created by qidunwei on 2016/3/19.
 */
public class ListView2ScoreAdapter extends BaseAdapter {

    View[] itemViews;

    public ListView2ScoreAdapter(ViewScoreInfo[] scoreList) {
        itemViews = new View[scoreList.length];
        Log.e("长度", itemViews.length + "");
        for (int i = 0; i < itemViews.length; i++) {
            itemViews[i] = makeItemView(scoreList[i]);
        }
    }

    public int getCount() {
        return itemViews.length;
    }

    public View getItem(int position) {
        return itemViews[position];
    }

    public long getItemId(int position) {
        return position;
    }

    private View makeItemView(ViewScoreInfo viewScoreInfo) {
        LayoutInflater inflater = (LayoutInflater) GlobalContext.mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater.inflate(R.layout.view_score_list_item, null);

        // 通过findViewById()方法实例R.layout.item内各组件
        TextView kcm = (TextView) itemView.findViewById(R.id.kcm);
        TextView kcsx = (TextView) itemView.findViewById(R.id.kcsx);
        TextView jsxm = (TextView) itemView.findViewById(R.id.jsxm);
        TextView xf = (TextView) itemView.findViewById(R.id.xf);
        TextView xn = (TextView) itemView.findViewById(R.id.xn);
        TextView xq = (TextView) itemView.findViewById(R.id.xq);
        TextView zpcj = (TextView) itemView.findViewById(R.id.zpcj);
        TextView qmcj = (TextView) itemView.findViewById(R.id.qmcj);
        TextView pscj = (TextView) itemView.findViewById(R.id.pscj);
        TextView bkcj = (TextView) itemView.findViewById(R.id.bkcj);
        kcm.setText(viewScoreInfo.kcm);
        kcsx.setText(viewScoreInfo.kcsx);
        jsxm.setText(viewScoreInfo.jsxm);
        xf.setText(viewScoreInfo.xf);
        xn.setText(viewScoreInfo.xn);
        xq.setText(viewScoreInfo.xq);
        zpcj.setText(viewScoreInfo.zpcj);
        qmcj.setText(viewScoreInfo.qmcj);
        pscj.setText(viewScoreInfo.pscj);
        bkcj.setText(viewScoreInfo.bkcj);
        if (Integer.valueOf(viewScoreInfo.zpcj) < 60) {
            zpcj.setTextColor(Color.parseColor("#FF0000"));
        }

        return itemView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null)
        return itemViews[position];
//        return convertView;
    }
}

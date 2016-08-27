package com.lesson.myahut.handler;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lesson.myahut.R;
import com.lesson.myahut.entity.ViewMakeUpInfo;
import com.lesson.myahut.entity.ViewTaskInfo;
import com.lesson.myahut.util.GlobalContext;

/**
 * Created by qidunwei on 2016/3/20.
 */
public class ListView2MakeUpAdapter extends BaseAdapter {

    View[] itemViews;

    public ListView2MakeUpAdapter(ViewMakeUpInfo[] makeupList) {
        itemViews = new View[makeupList.length];
        Log.e("长度", itemViews.length + "");
        for (int i = 0; i < itemViews.length; i++) {
            itemViews[i] = makeItemView(makeupList[i]);
        }
    }

    @Override
    public int getCount() {
        return itemViews.length;
    }

    @Override
    public View getItem(int position) {
        return itemViews[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View makeItemView(ViewMakeUpInfo viewMakeUpInfo) {
        LayoutInflater inflater = (LayoutInflater) GlobalContext.mainActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater.inflate(R.layout.view_makeup_list_item, null);

        // 通过findViewById()方法实例R.layout.item内各组件
        TextView kcmc = (TextView) itemView.findViewById(R.id.kcmc);
        TextView xkkh = (TextView) itemView.findViewById(R.id.xkkh);
        TextView kssj = (TextView) itemView.findViewById(R.id.kssj);
        TextView ksdd = (TextView) itemView.findViewById(R.id.ksdd);
        TextView zwh = (TextView) itemView.findViewById(R.id.zwh);
        TextView xm = (TextView) itemView.findViewById(R.id.xm);
        kcmc.setText(viewMakeUpInfo.kcmc);
        xkkh.setText(viewMakeUpInfo.xkkh);
        kssj.setText(viewMakeUpInfo.kssj);
        ksdd.setText(viewMakeUpInfo.ksdd);
        zwh.setText(viewMakeUpInfo.zwh);
        xm.setText(viewMakeUpInfo.xm);

        return itemView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null)
        return itemViews[position];
//        return convertView;
    }
}

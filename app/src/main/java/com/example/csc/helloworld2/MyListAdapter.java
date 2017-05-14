package com.example.csc.helloworld2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyListAdapter extends BaseAdapter{
    Context context;
    private ArrayList<List_item> listViewItemList = new ArrayList<List_item>() ;
    TextView name_TV;
    TextView recentPrice_TV;
    TextView stopPrice_TV;

    public MyListAdapter(Context context, ArrayList<List_item> listViewItemList) {//생성자
        this.context = context;
        this.listViewItemList = listViewItemList;
    }

    @Override
    public int getCount() {
        return this.listViewItemList.size();
    }//개수 얻어오기

    @Override
    public Object getItem(int position) {//position으로 아이템 얻어오기
        return this.listViewItemList.get(position);
    }
    @Override
    public long getItemId(int position) {//position으로 item id 얻어오기
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {//item의 변수들을 실제로 창에 뿌려주는 함수
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.activity_item, null);
            name_TV = (TextView) convertView.findViewById(R.id.itemName_TV);
            stopPrice_TV = (TextView) convertView.findViewById(R.id.itemStopPrice_TV);
            recentPrice_TV = (TextView) convertView.findViewById(R.id.itemRecentPrice_TV);
        }

        name_TV.setText(listViewItemList.get(position).getName());
        stopPrice_TV.setText(String.valueOf(listViewItemList.get(position).getStopPrice()));
        recentPrice_TV.setText(String.valueOf(listViewItemList.get(position).getRecentPrice()));
        return convertView;
    }
}

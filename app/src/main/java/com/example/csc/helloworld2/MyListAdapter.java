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

    public MyListAdapter(Context context, ArrayList<List_item> listViewItemList) {
        this.context = context;
        this.listViewItemList = listViewItemList;
    }

    @Override
    public int getCount() {
        return this.listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
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

package com.example.localtrader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AllItemsViewAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<AllItemsViewEntry> allItemsViewEntries;
    private LayoutInflater inflater;

    public AllItemsViewAdapter(Context ctx, ArrayList<AllItemsViewEntry> allItemsViewEntries) {
        this.ctx = ctx;
        this.allItemsViewEntries = allItemsViewEntries;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return this.allItemsViewEntries.size();
    }

    @Override
    public Object getItem(int i) {
        return this.allItemsViewEntries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return -1;
    }

    public String getUuid(int i) {
        return this.allItemsViewEntries.get(i).getUuid();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = this.inflater.inflate(R.layout.activity_items_list, null);
        TextView tv_title = view.findViewById(R.id.list_entry_title);
        TextView tv_descr = view.findViewById(R.id.list_entry_descr);
        TextView tv_price = view.findViewById(R.id.list_entry_price);
        tv_title.setText(this.allItemsViewEntries.get(i).getTitle());
        tv_descr.setText(this.allItemsViewEntries.get(i).getDescr());
        tv_price.setText(this.allItemsViewEntries.get(i).getPrice());
        return view;
    }
}

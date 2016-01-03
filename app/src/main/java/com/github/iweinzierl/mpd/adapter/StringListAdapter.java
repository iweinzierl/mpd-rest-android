package com.github.iweinzierl.mpd.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.iweinzierl.mpd.R;

import java.util.List;


public class StringListAdapter extends BaseListAdapter<String> {

    public StringListAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        String item = (String) getItem(i);

        view = LayoutInflater.from(context).inflate(R.layout.listitem_text, viewGroup, false);

        TextView valueField = (TextView) view.findViewById(R.id.value);
        valueField.setText(item);

        return view;
    }
}

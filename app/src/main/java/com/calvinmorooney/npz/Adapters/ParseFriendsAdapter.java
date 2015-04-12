package com.calvinmorooney.npz.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.calvinmorooney.npz.AppState;
import com.calvinmorooney.npz.R;

public class ParseFriendsAdapter extends BaseAdapter {

    Context context;

    public ParseFriendsAdapter(Context c)
    {
        context = c;
    }

    public int getCount()
    {
        return ((AppState) context.getApplicationContext()).friendsList.size();
    }

    public Object getItem(int position)
    {
        return ((AppState) context.getApplicationContext()).friendsList.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate (R.layout.friends_cell, parent, false);
        } else {
            view = convertView;
        }

        TextView text = (TextView) view.findViewById(R.id.friends_cell_text);
        text.setText (((AppState) context.getApplicationContext()).friendsList.get(position).getUsername());

        return view;
    }
}

package com.example.romek95a.bluetooth;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.Color.GREEN;

/**
 * Created by romek95a on 30.06.2018.
 */

public class MyArrayAdapter extends ArrayAdapter<String> {
    public MyArrayAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
        this.list =list;
        this.context=context;
    }

    @Override
    public Filter getFilter() {
        return super.getFilter();
    }

    ArrayList<String> list;
    Context context;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        if(Messenger.inOut){
            return 1;
        }
        else{
            return 0;
        }
    }
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int type=getItemViewType(position);
        // Get the data item for this position
        String text = getItem(position);
        int color=Color.BLACK;
        // Check if an existing view is being reused, otherwise inflate the view
        if(convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            switch (type){
                case 0:
                    convertView = inflater.inflate(R.layout.outgoing_message_layout, parent, false);
                    viewHolder.textView= (TextView) convertView.findViewById(R.id.singleOutgoingMessage);
                    color=Color.GREEN;
                    break;
                case 1:
                    convertView = inflater.inflate(R.layout.incoming_message_layout, parent, false);
                    viewHolder.textView= (TextView) convertView.findViewById(R.id.singleIncomingMessage);
                    color=Color.RED;
                    break;
            }
            convertView.setTag(viewHolder);
        }
        else viewHolder=(ViewHolder)convertView.getTag();
        String message=getItem(position);
        viewHolder.textView.setText(message);
        viewHolder.textView.setTextColor(color);

        return convertView;
    }
    public class ViewHolder{
        TextView textView;
    }
}

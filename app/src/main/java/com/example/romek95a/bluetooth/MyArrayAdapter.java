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

public class MyArrayAdapter extends ArrayAdapter<MessageWithType> {
    public MyArrayAdapter(Context context, ArrayList<MessageWithType> list) {
        super(context, 0, list);
        this.list =list;
        this.context=context;
    }
    public static final int TYPE_OUT = 0;
    public static final int TYPE_IN = 1;

    ArrayList<MessageWithType> list;
    Context context;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public MessageWithType getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemViewType(int position) {
        if(list.get(position).inOut){
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
        MessageWithType item=getItem(position);
        int type=getItemViewType(position);
        View row=convertView;
        ViewHolder viewHolder=null;
        if(row==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(type==TYPE_OUT){
                row=inflater.inflate(R.layout.outgoing_message_layout, parent, false);
            }
            if(type==TYPE_IN){
                row=inflater.inflate(R.layout.incoming_message_layout, parent, false);
            }
            TextView label=(TextView)row.findViewById(R.id.singleMessage);
            viewHolder=new ViewHolder(label);
            row.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)row.getTag();
        }
        viewHolder.textView.setText(item.message);

        return row;
    }
    public class ViewHolder{
        TextView textView;
        public ViewHolder(TextView textView){
            this.textView=textView;
        }
    }
}

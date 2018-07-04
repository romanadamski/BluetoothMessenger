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
        int type=getItemViewType(position);
        int color=Color.BLACK;
        View row=convertView;
        if(row==null){
            System.out.println("null");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if(Messenger.inOutList.get(position)==0){
                row=inflater.inflate(R.layout.outgoing_message_layout, parent, false);
            }
            if(Messenger.inOutList.get(position)==1){
                row=inflater.inflate(R.layout.incoming_message_layout, parent, false);
            }
        }
        String message=getItem(position);
        TextView label;
        System.out.println("inOut="+Messenger.inOutList.get(position)+", position="+position);
        if(Messenger.inOutList.get(position)==0){
            label=(TextView)row.findViewById(R.id.singleOutgoingMessage);
            label.setText(message);
            System.out.println("czyli "+0);
        }
        if(Messenger.inOutList.get(position)==1){
            label=(TextView)row.findViewById(R.id.singleIncomingMessage);
            label.setText(message);
            System.out.println("czyli "+1);
        }

        /*
        if(position%2==0){
            color=Color.RED;
        }
        if(position%2==1){
            color=Color.GREEN;
        }
        */
        return row;
    }
}

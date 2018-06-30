package com.example.romek95a.bluetooth;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by romek95a on 30.06.2018.
 */

public class MyArrayAdapter extends ArrayAdapter<String> {
    public MyArrayAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String text = getItem(position);
        TextView singleMessage;
        // Check if an existing view is being reused, otherwise inflate the view
        if(Messenger.inOut){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.incoming_message_layout, parent, false);
            singleMessage= (TextView) convertView.findViewById(R.id.singleIncomingMessage);
            singleMessage.setText(text);
        }
        else if(!Messenger.inOut){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.outgoing_message_layout, parent, false);
            singleMessage= (TextView) convertView.findViewById(R.id.singleOutgoingMessage);
            singleMessage.setText(text);
        }
        return convertView;
    }
}

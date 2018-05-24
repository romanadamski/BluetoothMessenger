package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class MainActivity extends Activity {
    Button bWlaczBluetooth;
    Button bDolaczDoGry;
    Button bUtworzNowaGre;
    private TextView klientserwer;
    String ks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bWlaczBluetooth=(Button) findViewById(R.id.bWlaczBluetooth);
        bDolaczDoGry=(Button) findViewById(R.id.bDolaczDoGry);
        bUtworzNowaGre=(Button) findViewById(R.id.bUtworzNowaGre);
        klientserwer=(TextView) findViewById(R.id.klientserwer);
        bWlaczBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wlaczBluetooth();
            }
        });

        bDolaczDoGry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ks="Jestem klientem";
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,ListaUrzadzen.class);
                startActivity(intent);
            }
        });
        bUtworzNowaGre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ks="Jestem serwerem";
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,Messenger.class);
                intent.putExtra("name", ks);
                startActivity(intent);
            }
        });
        BluetoothAdapter ba =BluetoothAdapter.getDefaultAdapter();
        if(!ba.isEnabled()){
            Intent i=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(i,1);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){
        if(resultCode==Activity.RESULT_OK){
            BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
        }
    }
    void wlaczBluetooth(){
        Intent pokazSie=new Intent (BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        pokazSie.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivity(pokazSie);
    }
}

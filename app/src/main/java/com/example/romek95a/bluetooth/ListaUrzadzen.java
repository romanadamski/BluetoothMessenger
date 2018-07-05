package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by romek95a on 14.05.2018.
 */

public class ListaUrzadzen extends Activity{
    private ListView lv;
    ArrayList<String> listOfUsers;
    List<String> listOfMacs;
    private UsersAdapter usersAdapter;

    private void initUrzadzeniaListView(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id){
                //pobranie adresu MAC:
                BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
                BluetoothDevice server=ba.getRemoteDevice(listOfMacs.get(pos));
                final ClientBluetooth clientBluetooth=ClientBluetooth.getInstance(server);
                Context context;
                context = getApplicationContext();
                Intent intent = new Intent(context,Messenger.class);
                intent.putExtra("adres", listOfMacs.get(pos));
                System.out.println("klient przekazuje "+listOfMacs.get(pos));
                startActivity(intent);
            }
        });
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_urzadzen);
        listOfUsers=new ArrayList<>();
        listOfMacs=new ArrayList<>();
        usersAdapter=new UsersAdapter(this, listOfUsers);
        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(usersAdapter);
        initUrzadzeniaListView();
        wykryjInne();
    }

    @Override
    protected void onStop(){
        try {
        unregisterReceiver(odbiorca);
        } catch (IllegalArgumentException ex) {
        // If Receiver not registered
        }
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    private final BroadcastReceiver odbiorca = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String a=intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(a)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String para="";
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    para="niesparowane";
                }
                else{
                    para="sparowane";
                }
                String nazwaPara=device.getName()+", "+para;
                usersAdapter.add(nazwaPara);
                listOfMacs.add(device.getAddress());
            }
        }

    };
    void wykryjInne(){
        IntentFilter iFiltr=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(odbiorca, iFiltr);
        BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
        ba.startDiscovery();
    }
}

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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by romek95a on 14.05.2018.
 */

public class ListOfDevices extends Activity{
    private ListView lv;
    ArrayList<String> listOfUsers;
    List<String> listOfMacs;
    private UsersAdapter usersAdapter;
    BluetoothAdapter ba;

    private void initUrzadzeniaListView(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id){
                Intent intent;
                BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
                //pobranie adresu MAC:
                BluetoothDevice server=ba.getRemoteDevice(listOfMacs.get(pos));
                if(server.getBondState()!=BluetoothDevice.BOND_BONDED){
                    unpaired().show();
                }
                else{
                    ClientBluetooth clientBluetooth=ClientBluetooth.getInstance(server);
                    Context context = getApplicationContext();
                    intent = new Intent(context,Messenger.class);
                    intent.putExtra("address", listOfMacs.get(pos));
                    intent.putExtra("isClient", true);
                    System.out.println("klient przekazuje "+listOfMacs.get(pos));
                    startActivity(intent);
                }
            }
        });
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_devices);
        listOfUsers=new ArrayList<>();
        listOfMacs=new ArrayList<>();
        usersAdapter=new UsersAdapter(this, listOfUsers);
        lv = (ListView) findViewById(R.id.listOfDevices);
        lv.setAdapter(usersAdapter);
        initUrzadzeniaListView();
        searchDevices();
        ba=BluetoothAdapter.getDefaultAdapter();

        AsyncCheckBluetooth asyncCheckBluetooth = new AsyncCheckBluetooth();
        asyncCheckBluetooth.execute();
    }
    class AsyncCheckBluetooth extends AsyncTask<String,Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            while(true) {
                if (!ba.isEnabled()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            lostBluetooth().show();
                        }
                    });
                    break;
                }
            }
            return null;
        }
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
                String pair="";
                String name = "<unknown>";
                if(device.getBondState()!=BluetoothDevice.BOND_BONDED){
                    pair=getResources().getString(R.string.not_paired);
                }
                else{
                    pair=getResources().getString(R.string.paired);
                }
                String temp = device.getName();
                if(temp != null)
                    name = temp;
                String namePair = name + ", "+pair;
                usersAdapter.add(namePair);
                listOfMacs.add(device.getAddress());
            }
        }

    };
    void searchDevices(){
        IntentFilter iFiltr=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(odbiorca, iFiltr);
        BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
        ba.startDiscovery();
    }
    private Dialog unpaired() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.problem_unpaired));
        dialogBuilder.setMessage(getResources().getString(R.string.info_unpaired));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.ok), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //reset
            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    private Dialog lostBluetooth(){
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.not_enable));
        dialogBuilder.setMessage(getResources().getString(R.string.please_enable));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.ok), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //reset
                resetApplication();
            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    void resetApplication(){
        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName() );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

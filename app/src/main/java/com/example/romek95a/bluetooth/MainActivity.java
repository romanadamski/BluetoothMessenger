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
    Button bDolaczDoCzatu;
    Button bUtworzNowyCzat;
    Button bWybierzJezyk;
    private TextView klientserwer;
    BluetoothAdapter ba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bWlaczBluetooth=(Button) findViewById(R.id.bWlaczBluetooth);
        bDolaczDoCzatu=(Button) findViewById(R.id.bDolaczDoCzatu);
        bUtworzNowyCzat=(Button) findViewById(R.id.bUtworzNowyCzat);
        bWybierzJezyk=(Button) findViewById(R.id.bWybierzJezyk);
        ba =BluetoothAdapter.getDefaultAdapter();
        bWlaczBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wlaczBluetooth();
            }
        });

        bDolaczDoCzatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ba.isEnabled()){
                    createBluetoothMessageDialog().show();
                }
                else{
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,ListaUrzadzen.class);
                    startActivity(intent);
                }
            }
        });

        bUtworzNowyCzat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ba.isEnabled()){
                    createBluetoothMessageDialog().show();
                }
                else{
                    final ServerBluetooth serwer=ServerBluetooth.getInstance();
                    if(!serwer.isAlive()){
                        serwer.start();
                    }
                    Intent intent;
                    Context context;
                    context = getApplicationContext();
                    intent = new Intent(context,Messenger.class);
                    startActivity(intent);
                }
            }
        });
        bWybierzJezyk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                Context context;
                context = getApplicationContext();
                intent = new Intent(context,Languages.class);
                startActivity(intent);
            }
        });
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
    private Dialog createPlainAlertDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.loading));
        dialogBuilder.setMessage(getResources().getString(R.string.connecting));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //reset
                Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName() );
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        });

        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    private Dialog createBluetoothMessageDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.not_enable));
        dialogBuilder.setMessage(getResources().getString(R.string.please_enable));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.ok), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //w sumie to nic nie musi robić
            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
}

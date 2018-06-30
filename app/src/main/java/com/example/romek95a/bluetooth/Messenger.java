package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by romek95a on 15.05.2018.
 */

public class Messenger extends Activity {
    String adres;
    private EditText wiadomosc;
    private Button wyslij;
    private ListView messages;
    private ArrayAdapter<String> adapter;
    private MyArrayAdapter myArrayAdapter;
    ArrayList<String> listOfMessages;
    String pom;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        wiadomosc=(EditText) findViewById(R.id.wiadomosc);
        wyslij=(Button) findViewById(R.id.wyslij);
        messages=(ListView) findViewById(R.id.messages);
        listOfMessages= new ArrayList<String>();
        myArrayAdapter=new MyArrayAdapter(this,listOfMessages);
        messages.setAdapter(myArrayAdapter);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            adres = extras.getString("adres");
        }
        if(!ClientBluetooth.getIsNull()){
            BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice server=ba.getRemoteDevice(adres);
            final ClientBluetooth klient=ClientBluetooth.getInstance(server);
            klient.start();
            //wysyłanie będąc klientem
            wyslij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    pom=wiadomosc.getText().toString();
                    klient.write(pom);
                    klient.wiadWych=pom;
                    //wych.setText(pom);
                    wiadomosc.setText("");
                    messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                }
            });
            //odbieranie będąc klientem
            //w petli
            class AsyncOdbior extends AsyncTask<String,Void, Void>{
                @Override
                protected Void doInBackground(String... strings) {
                    while(true){
                        //if od odswiezania listy
                        if(!klient.wiadPrzych.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myArrayAdapter.add(klient.wiadPrzych);
                                    klient.wiadPrzych="";
                                }
                            });
                            //if od scrollowania - tylko jak przyszla wiadomosc
                            messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            AsyncOdbior asyncOdbior = new AsyncOdbior();
            asyncOdbior.execute();
        }
        else if(!ServerBluetooth.getIsNull()){
            final ServerBluetooth serwer=ServerBluetooth.getInstance();
            //wysyłanie będąc serwerem
            wyslij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    pom=wiadomosc.getText().toString();
                    serwer.write(pom);
                    serwer.wiadWych=pom;
                    //wych.setText(pom);
                    wiadomosc.setText("");
                    messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                }
            });
            //odbieranie będąc serwerem
            //w petli
            class AsyncOdbior extends AsyncTask<String, Void, Void> {
                @Override
                protected Void doInBackground(String... strings) {
                    while(true){
                        //if od odswiezania listy
                        if(!serwer.wiadPrzych.equals("")){
                            runOnUiThread(new Runnable() {
                            @Override
                                public void run() {
                                    myArrayAdapter.add(serwer.wiadPrzych);
                                    serwer.wiadPrzych="";
                                }
                            });
                            messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        }
                        //if od scrollowania - tylko jak przyszla wiadomosc
                        messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            AsyncOdbior asyncOdbior = new AsyncOdbior();
            asyncOdbior.execute();
        }
    }


}

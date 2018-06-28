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
    String[] tableOfMessages;
    String pom;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        wiadomosc=(EditText) findViewById(R.id.wiadomosc);
        wyslij=(Button) findViewById(R.id.wyslij);
        messages=(ListView) findViewById(R.id.messages);
        tableOfMessages=new String[2];
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
                }
            });
            //odbieranie będąc klientem
            //w petli
            class AsyncOdbior extends AsyncTask<String,Void, Void>{
                @Override
                protected Void doInBackground(String... strings) {
                    while(true){
                        /*
                        ArrayList<String> listOfMessages = new ArrayList<String>();
                        listOfMessages.add(klient.wiadPrzych);
                        */
                        tableOfMessages[0]=klient.wiadPrzych;
                        tableOfMessages[1]="wiadomosc";
                        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,tableOfMessages );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.setAdapter(adapter);
                            }
                        });
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
                }
            });
            //odbieranie będąc serwerem
            //w petli
            class AsyncOdbior extends AsyncTask<String, Void, Void> {
                @Override
                protected Void doInBackground(String... strings) {
                    while(true){
                        /*
                        ArrayList<String> listOfMessages = new ArrayList<String>();
                        listOfMessages.add(klient.wiadPrzych);
                        */
                        tableOfMessages[0]=serwer.wiadPrzych;
                        tableOfMessages[1]="wiadomosc";
                        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,tableOfMessages );
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                messages.setAdapter(adapter);
                            }
                        });
                    }
                }
            }
            AsyncOdbior asyncOdbior = new AsyncOdbior();
            asyncOdbior.execute();
        }
    }


}

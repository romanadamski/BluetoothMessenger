package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    static ArrayList<Integer> inOutList;
    String pom;
    public static boolean inOut;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        wiadomosc=(EditText) findViewById(R.id.wiadomosc);
        wyslij=(Button) findViewById(R.id.wyslij);
        messages=(ListView) findViewById(R.id.messages);
        listOfMessages= new ArrayList<String>();
        inOutList=new ArrayList<>();
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
                    if(!pom.equals("")){
                        klient.write(pom);
                        klient.wiadWych=pom;
                        inOutList.add(0);
                        myArrayAdapter.add(pom);
                        wiadomosc.setText("");
                        messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                    }
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
                                    inOutList.add(1);
                                    klient.wiadPrzych="";
                                }
                            });
                        }
                        //if od scrollowania - tylko jak przyszla wiadomosc
                        //(+ klawiatura)
                        if(messages.getLastVisiblePosition()>myArrayAdapter.getCount() - 3)
                            messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        wiadomosc.setOnTouchListener(new View.OnTouchListener()
                        {
                            public boolean onTouch(View arg0, MotionEvent arg1)
                            {
                                messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                                System.out.println("dupaaaaaaaaaaa");
                                return false;
                            }
                        });
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
                    if(!pom.equals("")){
                        serwer.write(pom);
                        serwer.wiadWych=pom;
                        inOutList.add(0);
                        myArrayAdapter.add(pom);
                        wiadomosc.setText("");
                        messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                    }
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
                                    inOutList.add(1);
                                    serwer.wiadPrzych="";
                                }
                            });
                        }
                        //if od scrollowania - tylko jak przyszla wiadomosc
                        //(+ klawiatura)
                        if(messages.getLastVisiblePosition()>myArrayAdapter.getCount() - 3)
                            messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        wiadomosc.setOnTouchListener(new View.OnTouchListener()
                        {
                            public boolean onTouch(View arg0, MotionEvent arg1)
                            {
                                messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                                System.out.println("dupaaaaaaaaaaa");
                                return false;
                            }
                        });
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

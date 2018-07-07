package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
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
    ArrayList<MessageWithType> listOfMessages;
    String pom;
    public static boolean inOut;
    Dialog isConnected;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        wiadomosc=(EditText) findViewById(R.id.wiadomosc);
        wyslij=(Button) findViewById(R.id.wyslij);
        messages=(ListView) findViewById(R.id.messages);
        listOfMessages= new ArrayList<MessageWithType>();
        myArrayAdapter=new MyArrayAdapter(this,listOfMessages);
        messages.setAdapter(myArrayAdapter);
        isConnected=createPlainAlertDialog();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            adres = extras.getString("adres");
            System.out.println("serwer odbiera "+adres);
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
                        myArrayAdapter.add(new MessageWithType(pom, false));
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isConnected.show();
                        }
                    });
                    while(true){
                        if(ClientBluetooth.polaczono.equals("Połączono")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isConnected.hide();
                                }
                            });
                            break;
                        }
                    }
                    while(true){
                        //if od odswiezania listy
                        if(!klient.wiadPrzych.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myArrayAdapter.add(new MessageWithType(klient.wiadPrzych, true));
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
                                return false;
                            }
                        });
                        //rozlaczanie
                        if(klient.disconnect){
                            System.out.println("faktycznie");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    disconnectDialog().show();
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
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
                        myArrayAdapter.add(new MessageWithType(pom, false));
                        wiadomosc.setText("");
                        messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                    }
                }
            });
            //odbieranie będąc serwerem
            //w petli
            class AsyncOdbior extends AsyncTask<String, Void, Void> {
                @Override
                protected Void doInBackground(String... strings) {runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnected.show();
                    }
                });
                    while(true){
                        if(ServerBluetooth.polaczono.equals("Połączono")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    isConnected.hide();
                                }
                            });
                            break;
                        }
                    }
                    while(true){
                        //if od odswiezania listy
                        if(!serwer.wiadPrzych.equals("")){
                            runOnUiThread(new Runnable() {
                            @Override
                                public void run() {
                                    myArrayAdapter.add(new MessageWithType(serwer.wiadPrzych, true));
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
                                return false;
                            }
                        });
                        if(serwer.disconnect){
                            System.out.println("faktycznie");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    disconnectDialog().show();
                                }
                            });
                            break;
                        }
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    return null;
                }
            }
            AsyncOdbior asyncOdbior = new AsyncOdbior();
            asyncOdbior.execute();
        }
    }
    private Dialog backButtonDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Wyjście");
        dialogBuilder.setMessage("Czy na pewno chcesz wyjść?");
        dialogBuilder.setNegativeButton("Tak", new Dialog.OnClickListener() {
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
        dialogBuilder.setPositiveButton("Nie", new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    private Dialog disconnectDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Problem z połączeniem");
        dialogBuilder.setMessage("Zostałeś rozłączony z drugim użytkownikiem");
        dialogBuilder.setNegativeButton("Wróć do menu", new Dialog.OnClickListener() {
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
    private Dialog createPlainAlertDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Łączenie");
        dialogBuilder.setMessage("Poczekaj na połączenie z drugim użytkownikiem");
        dialogBuilder.setNegativeButton("Anuluj", new Dialog.OnClickListener() {
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
    @Override
    public void onBackPressed() {
        backButtonDialog().show();
    }
}

package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by romek95a on 15.05.2018.
 */

public class Messenger extends Activity {
    String address;
    private EditText singleMessage;
    private Button sendMessage;
    private ListView messages;
    private MyArrayAdapter myArrayAdapter;
    ArrayList<MessageWithType> listOfMessages;
    String temp;
    Dialog isConnected;
    BluetoothAdapter ba;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        singleMessage =(EditText) findViewById(R.id.singleMessage);
        sendMessage =(Button) findViewById(R.id.sendMessage);
        messages=(ListView) findViewById(R.id.messages);
        listOfMessages= new ArrayList<MessageWithType>();
        myArrayAdapter=new MyArrayAdapter(this,listOfMessages);
        messages.setAdapter(myArrayAdapter);
        isConnected=createPlainAlertDialog();
        Bundle extras = getIntent().getExtras();
        ba=BluetoothAdapter.getDefaultAdapter();

        if (extras != null) {
            address = extras.getString("address");
        }
        if(!ClientBluetooth.getIsNull()){
            BluetoothDevice server=ba.getRemoteDevice(address);
            final ClientBluetooth client=ClientBluetooth.getInstance(server);
            client.start();
            //wysyłanie będąc klientem
            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    temp = singleMessage.getText().toString();
                    if(!temp.equals("")){
                        client.write(temp);
                        client.outputMessage= temp;
                        myArrayAdapter.add(new MessageWithType(temp, false));
                        singleMessage.setText("");
                        messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                    }
                }
            });
            //odbieranie będąc klientem
            //w petli
            class AsyncReceive extends AsyncTask<String,Void, Void>{
                @Override
                protected Void doInBackground(String... strings) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isConnected.show();
                        }
                    });
                    while(true){
                        if(!ba.isEnabled()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lostBluetooth().show();
                                }
                            });
                            break;
                        }
                        if(ClientBluetooth.connected.equals("Connected")){
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
                        //odswiezanie listy
                        if(!client.incomingMessage.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myArrayAdapter.add(new MessageWithType(client.incomingMessage, true));
                                    client.incomingMessage="";
                                }
                            });
                        }
                        //przesuwanie listy do najnowszej wiadomosci
                        if(messages.getLastVisiblePosition()>myArrayAdapter.getCount() - 3)
                            messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        singleMessage.setOnTouchListener(new View.OnTouchListener()
                        {
                            public boolean onTouch(View arg0, MotionEvent arg1)
                            {
                                messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                                return false;
                            }
                        });
                        //rozlaczanie
                        if(client.disconnect){
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
            AsyncReceive asyncReceive = new AsyncReceive();
            asyncReceive.execute();
        }
        else if(!ServerBluetooth.getIsNull()){
            final ServerBluetooth server=ServerBluetooth.getInstance();
            //wysyłanie będąc serwerem
            sendMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    temp = singleMessage.getText().toString();
                    if(!temp.equals("")){
                        server.write(temp);
                        server.outputMessage= temp;
                        myArrayAdapter.add(new MessageWithType(temp, false));
                        singleMessage.setText("");
                        messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                    }
                }
            });
            //odbieranie będąc serwerem
            //w petli
            class AsyncReceive extends AsyncTask<String, Void, Void> {
                @Override
                protected Void doInBackground(String... strings) {runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnected.show();
                    }
                });
                    while(true){
                        if(!ba.isEnabled()){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    lostBluetooth().show();
                                }
                            });
                            break;
                        }
                        if(ServerBluetooth.connected.equals("Connected")){
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
                        //odswiezanie listy
                        if(!server.incomingMessage.equals("")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myArrayAdapter.add(new MessageWithType(server.incomingMessage, true));
                                    server.incomingMessage="";
                                }
                            });
                        }
                        //przesuwanie listy do najnowszej wiadomosci
                        if(messages.getLastVisiblePosition()>myArrayAdapter.getCount() - 3)
                            messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                        singleMessage.setOnTouchListener(new View.OnTouchListener()
                        {
                            public boolean onTouch(View arg0, MotionEvent arg1)
                            {
                                messages.smoothScrollToPosition(myArrayAdapter.getCount() - 1);
                                return false;
                            }
                        });
                        if(server.disconnect){
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
            AsyncReceive asyncReceive = new AsyncReceive();
            asyncReceive.execute();
        }
    }
    private Dialog backButtonDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.quit));
        dialogBuilder.setMessage(getResources().getString(R.string.sure));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.yes), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //reset
                resetApplication();
            }
        });
        dialogBuilder.setPositiveButton(getResources().getString(R.string.no), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    private Dialog disconnectDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.problem_disconnected));
        dialogBuilder.setMessage(getResources().getString(R.string.disconnected));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.go_to_menu), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //reset
                resetApplication();
            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
    private Dialog createPlainAlertDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.loading));
        dialogBuilder.setMessage(getResources().getString(R.string.connecting));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.cancel), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                //reset
                resetApplication();
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
    @Override
    public void onBackPressed() {
        backButtonDialog().show();
    }
}

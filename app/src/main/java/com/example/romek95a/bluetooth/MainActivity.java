package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.Locale;

public class MainActivity extends Activity {
    static ImageButton bToggleBluetooth;
    Button bJoinChat;
    Button bCreateNewChat;
    Button bChooseLanguage;
    BluetoothAdapter ba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bJoinChat =(Button) findViewById(R.id.bJoinChat);
        bCreateNewChat =(Button) findViewById(R.id.bCreateNewChat);
        bChooseLanguage =(Button) findViewById(R.id.bChooseLanguage);
        bToggleBluetooth =(ImageButton) findViewById(R.id.bToggleBluetooth);
        ba =BluetoothAdapter.getDefaultAdapter();

        setButtonFunctions();

        if(ba.isEnabled())
            bToggleBluetooth.setImageResource(R.drawable.bluetooth_image_blue);
        else
            bToggleBluetooth.setImageResource(R.drawable.bluetooth_image_black);
    }
    void setButtonFunctions(){
        bToggleBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleBluetooth();
            }
        });
        bJoinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!ba.isEnabled()){
                    createBluetoothMessageDialog().show();
                }
                else{
                    Context context;
                    context = getApplicationContext();
                    Intent intent = new Intent(context,ListOfDevices.class);
                    startActivity(intent);
                }
            }
        });

        bCreateNewChat.setOnClickListener(new View.OnClickListener() {
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
                    intent.putExtra("isClient", false);
                    startActivity(intent);
                }
            }
        });
        bChooseLanguage.setOnClickListener(new View.OnClickListener() {
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
    public static class BluetoothReceiver extends BroadcastReceiver {
        @Override public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int state;
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
                    if (state == BluetoothAdapter.STATE_ON) {
                        bToggleBluetooth.setImageResource(R.drawable.bluetooth_image_blue);
                    }
                    else if (state == BluetoothAdapter.STATE_OFF){
                        bToggleBluetooth.setImageResource(R.drawable.bluetooth_image_black);
                    }
            }
        }
    }
    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLanguage(language);
    }
    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getSharedPreferences("CommonPrefs",
                Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }
    public void changeLanguage(String language){
        if(language.equals(""))
            return;
        Locale locale = new Locale(language);
        saveLocale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getBaseContext().getResources().updateConfiguration(configuration,
                getBaseContext().getResources().getDisplayMetrics());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent i){
        if(resultCode==Activity.RESULT_OK){
            BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
        }
    }
    void toggleBluetooth(){
        if(!ba.isEnabled()){
            ba.enable();
        }
        else{
            ba.disable();
        }
    }
    private Dialog createBluetoothMessageDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(getResources().getString(R.string.not_enable));
        dialogBuilder.setMessage(getResources().getString(R.string.please_enable));
        dialogBuilder.setNegativeButton(getResources().getString(R.string.ok), new Dialog.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });
        dialogBuilder.setCancelable(false);
        return dialogBuilder.create();
    }
}

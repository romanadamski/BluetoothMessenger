package com.example.romek95a.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by romek95a on 15.05.2018.
 */

public class Messenger extends Activity {
    String ks="Nie wiem kim jestem xD";
    String adres;
    private TextView odbior;
    private TextView wych;
    private EditText wiadomosc;
    private Button wyslij;
    private Button odbierz;
    private TextView polacz;
    private TextView klientserwer;
    String pom;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messenger);
        odbior=(TextView) findViewById(R.id.odbior);
        wych=(TextView) findViewById(R.id.wych);
        wiadomosc=(EditText) findViewById(R.id.wiadomosc);
        wyslij=(Button) findViewById(R.id.wyslij);
        odbierz=(Button) findViewById(R.id.odbierz);
        polacz=(TextView)findViewById(R.id.polacz);
        klientserwer=(TextView) findViewById(R.id.klientserwer);
        Bundle extras = getIntent().getExtras();
        odbior.setText("Nic nie przysłano");
        wych.setText("Nic nie wysłano");
        if (extras != null) {
            ks = extras.getString("name");
            adres = extras.getString("adres");
        }
        klientserwer.setText(ks);
        if(ks.equals("Jestem klientem")){
            BluetoothAdapter ba=BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice server=ba.getRemoteDevice(adres);
            final ClientBluetooth klient=new ClientBluetooth(server);
            klient.start();
            //odbieranie będąc klientem
            odbior.setText(klient.wiadPrzych);
            polacz.setText(klient.polaczono);
            odbierz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    odbior.setText(klient.wiadPrzych);
                    polacz.setText(klient.polaczono);
                }
            });
            //wysylanie będąc klientem
            wyslij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    pom=wiadomosc.getText().toString();
                    klient.write(pom);
                    klient.wiadWych=pom;
                    wych.setText(pom);
                    wiadomosc.setText("");
                }
            });

        }
        else if(ks.equals("Jestem serwerem")){
            final ServerBluetooth serwer=new ServerBluetooth();
            polacz.setText(serwer.polaczono);
            serwer.start();
            //wysyłanie będąc serwerem
            wyslij.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    pom=wiadomosc.getText().toString();
                    serwer.write(pom);
                    serwer.wiadWych=pom;
                    wych.setText(pom);
                    wiadomosc.setText("");
                }
            });
            //odbieranie będąc serwerem
            odbierz.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    odbior.setText(serwer.wiadPrzych);
                    Log.d("Odbior serwerem",odbior.getText().toString());
                    polacz.setText(serwer.polaczono);
                }
            });

        }
    }
}

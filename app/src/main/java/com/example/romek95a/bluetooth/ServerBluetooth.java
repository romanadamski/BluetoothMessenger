package com.example.romek95a.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by romek95a on 14.05.2018.
 */

public class ServerBluetooth extends Thread {
    private static BluetoothServerSocket SerwerSocket;
    String wiadWych="Nic nie wysłano";
    String wiadPrzych="";
    String polaczono="Nie polaczono";
    PrintWriter out;
    private BluetoothSocket Socket;
    private static volatile ServerBluetooth instance=null;
    private static boolean isNull=true;
    private ServerBluetooth(){}
    public BluetoothSocket getSocket() {
        return Socket;
    }
    public static ServerBluetooth getInstance(){
        if(instance == null){
            synchronized (ServerBluetooth.class){
                instance = new ServerBluetooth();
                isNull=false;
                BluetoothAdapter mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
                BluetoothServerSocket temp=null;
                try{
                    UUID uuid=UUID.fromString("d83eac47-1eea-4654-8eca-74c691c13484");
                    temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord("Usluga witajaca", uuid);

                }catch(IOException e){}
                SerwerSocket=temp;
            }
        }
        return instance;
    }
    public static boolean getIsNull(){
        return isNull;
    }

    public void run(){
        Socket=null;
        while(true) {
            try {
                Socket = SerwerSocket.accept();
                if(Socket.isConnected()){
                    out = new PrintWriter(Socket.getOutputStream(), true);
                    Log.d("Socket","Nie jest nullem");
                    polaczono="Połączono";
                    Log.d("Info","Polaczono sie ze mna");
                    break;
                }
            } catch (IOException e) {

            }
        }
        while(true){
            try{
                BufferedReader in=new BufferedReader(new InputStreamReader(Socket.getInputStream()));
                wiadPrzych=in.readLine();
            }catch(IOException e){

            }
        }
    }
    public void write(String wiadomosc){
        out.println(wiadomosc);
        Log.d("Write serwer",wiadomosc);
    }
}

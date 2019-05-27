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
    private static BluetoothServerSocket serverSocket;
    String outputMessage ="Nic nie wysłano";
    String incomingMessage ="";
    static String connected ="";
    PrintWriter out;
    public boolean disconnect=false;
    private BluetoothSocket Socket;
    private static volatile ServerBluetooth instance=null;
    private static boolean isNull=true;
    private ServerBluetooth(){}
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
                serverSocket =temp;
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
                Socket = serverSocket.accept();
                if(Socket.isConnected()){
                    out = new PrintWriter(Socket.getOutputStream(), true);
                    Log.d("Socket","Nie jest nullem");
                    connected ="Connected";
                    Log.d("Info","Polaczono sie ze mna");
                    break;
                }
            } catch (IOException e) {
            }
        }
        while(true){
            try{
                BufferedReader in=new BufferedReader(new InputStreamReader(Socket.getInputStream()));
                incomingMessage =in.readLine();
            }catch(IOException e){
                disconnect=true;
                break;
            }
        }
    }
    public void write(String message){
        out.println(message);
    }
}

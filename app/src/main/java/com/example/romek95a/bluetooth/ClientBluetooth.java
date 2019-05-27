package com.example.romek95a.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.UUID;

/**
 * Created by romek95a on 14.05.2018.
 */

public class ClientBluetooth extends Thread {
    private static BluetoothSocket Socket;
    private static BluetoothDevice Device;
    String outputMessage ="Nic nie wysłano";
    String incomingMessage ="";
    static String connected ="Nie połączono";
    PrintWriter out;
    public boolean disconnect=false;
    private static volatile ClientBluetooth instance=null;
    private static boolean isNull=true;
    private ClientBluetooth(){}
    public static ClientBluetooth getInstance(BluetoothDevice device){
        if(instance==null){
            synchronized (ClientBluetooth.class){
                instance = new ClientBluetooth();
                isNull=false;
                BluetoothSocket temp = null;
                Device = device;
                try{
                    UUID uuid=UUID.fromString("d83eac47-1eea-4654-8eca-74c691c13484");
                    temp=device.createRfcommSocketToServiceRecord(uuid);

                }catch(Exception e){}
                Socket =temp;
            }
        }
        return instance;
    }
    public static boolean getIsNull(){
        return isNull;
    }
    public void run(){
        try{
            Socket.connect();
            connected ="Connected";
            Log.d("Info","Polaczylem sie");
            out = new PrintWriter(Socket.getOutputStream(), true);

        }catch (Exception e){
        }
        while(true){
            try{
                BufferedReader in=new BufferedReader(new InputStreamReader(Socket.getInputStream()));
                incomingMessage =in.readLine();
            }catch (Exception e){
                disconnect=true;
                break;
            }
        }
    }
    public void write(String message){
        out.println(message);
    }
}

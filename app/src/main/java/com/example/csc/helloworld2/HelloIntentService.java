package com.example.csc.helloworld2;

/**
 * Created by jisoo on 2017-05-12.
 */

import android.app.IntentService;
import android.content.Intent;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class HelloIntentService extends IntentService {

    static Socket clientSocket;
    static String HostIP;
    static int tcp_port;
    byte[] arr;
    String real;
    public HelloIntentService() {
        super("HelloIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        int a = -1;
        if(clientSocket == null)
            clientSocket = new Socket();
        if(!clientSocket.isConnected()) {
            HostIP = "121.180.236.95";
            tcp_port = 17776;
            try {
                clientSocket.connect(new InetSocketAddress(HostIP, tcp_port));
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            clientSocket.getOutputStream().write((intent.getStringExtra("request") + "\0").getBytes());
            arr = new byte[256];
            int b = clientSocket.getInputStream().read(arr, 0, 256);
            int strlen;
            for(strlen = 0; arr[strlen]!=0; strlen++)
                ;
            real = new String(arr,0,strlen,"UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(Intent.ACTION_SEND);
        broadcastIntent.putExtra("response",real);
        sendBroadcast(broadcastIntent);
    }
}
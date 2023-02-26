package com.example.irontextapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Client {
    private DataOutputStream output;
    private DataInputStream input;

    private final String host;
    private final int port;
    private Socket socket;

    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }


    // Starts the connection with the server
    public Socket startConnection() throws Exception {
        Socket socket = new Socket(host, port);
        this.socket = socket;
        return socket;
    }


    // Authenticates using token
    private void tokenAuth() throws Exception {
        output.writeInt(0);
        output.writeUTF("5V71I1LDi0yñI2ñSjYDkOdhNJBui6Git");
        int resultCode = input.readInt();
        System.out.println("EXIT CODE: " + resultCode);
        if (resultCode == 0){
            Thread.sleep(500);
            System.out.println("sending request");
            sendEvent(0, socket, "Message from 0");
            sendEvent(0, socket, "Message from 0 but twice");
            sendEvent(0, socket, "Message from 0 and 3 times one");
        }
    }

    // Create account

    private void passwordAuth() throws Exception {
        output.writeInt(1);
        //email
        output.writeUTF("GMAIL HERE");
        //password
        output.writeUTF("PASSWORD HERE");

        // You can see all codes iin AuthExitCodes.java
        int resultCode = input.readInt();


        // If success...
        if (resultCode == 0){
            String newToken = input.readUTF();

            Thread.sleep(1000);

            sendEvent(RequestTypes.SEND_MESSAGE, socket, "message");

        }

    }


    // Loggin with password

    private void registerAcc() throws Exception {
        output.writeInt(2);
        output.writeUTF("thedracon");
        //email
        output.writeUTF("vicvarcas2007@gmail.com");
        //password
        output.writeUTF("Proyecto103");
        int resultCode = input.readInt();

        if (resultCode == 0){

        }

    }



    // TO SEND THE DATA dataList
    public void sendEvent(int requestType, Socket serverSocket, Object... dataList)  {
        try {
            DataOutputStream output = new DataOutputStream(serverSocket.getOutputStream());
            output.writeInt(requestType);
            for (Object data : dataList){
                if (data instanceof String || data instanceof UUID){
                    output.writeUTF(data.toString());
                } else if (data instanceof Integer) {
                    output.writeInt(((Integer) data));
                } else if (data instanceof Long) {
                    output.writeFloat(((Long) data));
                } else if (data instanceof Boolean) {
                    output.writeBoolean(((Boolean) data));
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public int getPort() {
        return port;
    }

    public String getHost() {
        return host;
    }
}

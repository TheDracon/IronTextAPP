package com.example.irontextapp;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.irontextapp.Utils.Tuple2;
import com.example.irontextapp.activities.ChatActivity;
import com.example.irontextapp.activities.LoginActivity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class Client {
    private DataOutputStream output;
    private DataInputStream input;
    private final String host;
    private final int port;
    private Socket socket;
    private Thread listenerThread;
    private boolean isListening;

    private boolean isLoggedIn;


    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }
    public void closeConnection(){
        if (socket != null){
            try {
                socket.close();
            } catch (IOException e){
                throw new RuntimeException(e);
            }
            this.isLoggedIn = false;
        } else {
            throw new RuntimeException("Socket is null");
        }
    }
    public boolean isConnected(){
        if (socket != null){
            if (socket.isClosed()) return false;
            return socket.isConnected();
        }
        return false;
    }

    public boolean isLoggedIn(){
        return isLoggedIn;
    }
    // Starts the connection with the server
    public void startConnection() {
        try {
            this.socket = new Socket(host, port);
            output = new DataOutputStream(socket.getOutputStream());
            input = new DataInputStream(socket.getInputStream());
        } catch (Exception ignore){}
    }


    // Authenticates using token
    public int tokenAuth(String email, String token) {
        System.out.println("token auth");
        try {
            output.writeInt(0);
            output.writeUTF(email);
            output.writeUTF(token);
            System.out.println("a");
            int resultCode = input.readInt();
            System.out.println(resultCode);
            if (resultCode == 0){
                isLoggedIn = true;
            }
            return resultCode;
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }

    // Create account

    public Tuple2<Integer, Tuple2<String, String>> passwordAuth(String email, String password){
        try {
            output.writeInt(1);
            //email
            output.writeUTF(email);
            //password
            output.writeUTF(password);
            System.out.println("ABC");
            // You can see all codes iin AuthExitCodes.java
            int resultCode = input.readInt();
            String newToken = input.readUTF();
            String username = input.readUTF();
            System.out.println("ABC");
            // If success...
            if (resultCode == 0){
                isLoggedIn = true;
            }
            return new Tuple2<>(resultCode, new Tuple2<>(newToken, username));
        } catch (Exception e){
            e.printStackTrace();
        }
        return new Tuple2<>(-1, null);
    }


    // Loggin with password

    public int registerAcc(String username, String email, String password){
        try {
            output.writeInt(2);
            Thread.sleep(100);
            output.writeUTF(username);
            Thread.sleep(100);

            //email
            output.writeUTF(email);
            Thread.sleep(100);

            //password
            output.writeUTF(password);
            Thread.sleep(100);

            int resultCode = input.readInt();
            try {
                socket.close();
            } catch (Exception ingore){}
            return resultCode;
        } catch (Exception e){
            e.printStackTrace();
        }
        return -1;
    }


    // TO SEND THE DATA dataList
    public void sendEvent(int requestType, Object... dataList)  {
        try {
            output.writeInt(requestType);
            if (dataList.length == 0) {
                System.out.println("length 0 ");
                return;
            }
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
    public void listenForPackets(AppCompatActivity appActivity){
        if (isListening){
            System.out.println("Already listening");
            return;
        }
        this.isListening = true;
        listenerThread = new Thread(() ->{
            while (!socket.isClosed() && socket.isConnected() && isListening) {
                try {
                    // 0 = one new message | 1 = x new messages
                    int requestType = input.readInt();
                    System.out.println("REQTYPE:" + requestType);

                    System.out.println(requestType);
                    if (requestType == 3){
                        Toast.makeText(ChatActivity.getMessageAdapter().context, "You got disconnected", Toast.LENGTH_LONG).show();
                    } else if (requestType == 0) {
                        String message = input.readUTF();
                        String sender = input.readUTF();
                        long timestamp = input.readLong();
                        Message newMessage = new Message(message, sender, (sender.equals(UserDataManager.getUsername())), timestamp);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = sdf.format(new Date(timestamp));
                        System.out.println(" MESSAGE: '" + message + "' BY: '" + sender + "' ON: " + formattedDate);

                        ChatActivity.getMessageAdapter().add(newMessage, appActivity);

                    } else if (requestType == 1) {
                        int amountOfRows = input.readInt();
                        System.out.println("AMM: " + amountOfRows);
                        for (int i = 0; i < amountOfRows; i++) {
                            String content = input.readUTF();
                            String sender = input.readUTF();
                            long timestamp = input.readLong();
                            if (sender.equals(UserDataManager.getUsername())) continue;
                            System.out.println(content);
                            Message currentMessage = new Message(content, sender, (sender.equals(UserDataManager.getUsername())), timestamp);
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String formattedDate = sdf.format(new Date(currentMessage.getTimestamp()));
                            System.out.println("MESSAGE: '" + currentMessage.getContent() + "' BY: '" + currentMessage.getSender() + "' ON: " + formattedDate);
                            ChatActivity.getMessageAdapter().add(currentMessage, appActivity);
                        }

                    }
                }  catch (SocketException e){
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        });
        listenerThread.start();
        System.out.println("LISTENING...");

    }

    public void stopListening(){
        listenerThread.interrupt();
    }
    public boolean isListening() {
        return isListening;
    }

}

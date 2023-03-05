package com.example.irontextapp;

import androidx.appcompat.app.AppCompatActivity;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UserDataManager {

    public static File dataFile;
    public static JSONObject dataObject = new JSONObject();

    public static void init(AppCompatActivity a)  {
        System.out.println("RIP BOZO" + a.getFilesDir());
        dataFile = new File(a.getFilesDir() + "/userdata.dat");
        createFile();
        loadFile(dataFile);
    }

    public static void createFile(){
        if (doesFileExists()) return;
        try {
            dataFile.createNewFile();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public static boolean doesFileExists(){
        if (dataFile == null) return false;
        return dataFile.exists();
    }
    @Nullable
    public static String getUsername(){
        if(!doesFileExists()) return null;
        try {
            return dataObject.get("username").toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Nullable
    public static String getEmail(){
        if(!doesFileExists()) return null;
        try {
            return dataObject.get("email").toString();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static String getToken(){
        if(!doesFileExists()) return null;
        try {
            return dataObject.get("token").toString();

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }



    public static void setValues(@Nullable String token, @Nullable String email, @Nullable String username){
        try {
            if (token==null) token = "";
            if (email==null) email = "";
            if (username==null) username = "";
            dataObject.put("token", token);
            dataObject.put("email", email);
            dataObject.put("username", username);
            saveToFile();
        } catch (Exception e){
            e.printStackTrace();
        }

    }
    public static void saveToFile() throws IOException, JSONException {
        FileWriter fileWriter = new FileWriter(dataFile);
        fileWriter.write(dataObject.toString(4));
        fileWriter.close();
    }
    private static String loadFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            StringBuilder json = new StringBuilder();
            while (scanner.hasNextLine()){
                json.append(scanner.nextLine());
            }
            dataObject = new JSONObject(json.toString());
            return json.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }
}

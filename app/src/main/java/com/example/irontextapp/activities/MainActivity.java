package com.example.irontextapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.Client;
import com.example.irontextapp.Main;
import com.example.irontextapp.R;

import java.io.*;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
	VideoView video;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (getSupportActionBar() != null) getSupportActionBar().hide();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
			final WindowInsetsController insetsController = getWindow().getInsetsController();
			if (insetsController != null) {
				insetsController.hide(WindowInsets.Type.statusBars());
			}
		} else {
			getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN
			);
		}
		new Thread(()->{
			Client client = new Client("192.168.1.23", 1252);
			System.out.println("CONNECTED!");
			client.startConnection();
			Main.setClient(client);
		}).start();

		File file = new File(getBaseContext().getFilesDir(), "userDataB.dat");
		if (!file.exists()){
			// File does not exist, go to register acc
			registeringNeeded();

		} else{
			String email = null;
			String token = null;
			try {
				Scanner myReader = new Scanner(file);
				email = myReader.nextLine();
				try {
					token = myReader.nextLine();
				}catch (Exception e){
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (token == null){
				registeringNeeded();
				return;
			}
			int resultCode = Main.getClient().tokenAuth(token);
			if (resultCode == 0){
				loggedIn();
			} else {
				loginNeeded();
			}
		}

	}
	public void registeringNeeded(){
		Intent chatActivity = new Intent(this, RegisterActivity.class);
		startActivity(chatActivity);
	}
	public void loginNeeded(){
		// TODO: GO TO LOGGING
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	public void loggedIn(){
		Intent intent = new Intent(this, ChatActivity.class);
		startActivity(intent);
	}
}
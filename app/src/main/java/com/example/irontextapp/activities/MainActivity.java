package com.example.irontextapp.activities;

import android.content.Intent;
import android.os.Build;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
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
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		Client client = new Client("localhost", 1235);
		Main.setClient(client);
		client.startConnection();



		File file = new File(getBaseContext().getFilesDir(), "userdata.dat");
		if (file.exists() && file.length() <= 1){
			System.out.println("file does not exist or it's empty");

			// File does not exist, go to register acc
			registeringNeeded();

		} else{
			String username = null;
			String token = null;
			try {
				Scanner myReader = new Scanner(file);
				username = myReader.nextLine();
				try {
					token = myReader.nextLine();
				}catch (Exception ignore){}
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			if (token == null){
				registeringNeeded();
				return;
			}
			int resultCode = client.tokenAuth(token);
			if (resultCode == 0){
				loggedIn();
			} else {
				loginNeeded();
			}
		}
	}
	public void registeringNeeded(){
		// TODO: MAKE REGISTER ACTIVITY
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
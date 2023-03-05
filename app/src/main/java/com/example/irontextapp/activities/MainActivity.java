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
import com.example.irontextapp.*;

import java.io.*;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UserDataManager.init(this);
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
		new Thread(() ->{
			connectToTheServer();

			if (!UserDataManager.doesFileExists()){
				System.out.println("FILE DOES NOT EXIST");
				// File does not exist, go to register acc
				registeringNeeded();
			} else{
				System.out.println("FILE EXISTS");

				String token = UserDataManager.getToken();
				String email = UserDataManager.getEmail();
				if (token == null){
					System.out.println("token is null");
					registeringNeeded();
					return;
				}
				System.out.println("EMAIL: " + email);
				System.out.println("token: " + token);
				int resultCode = Main.getClient().tokenAuth(email, token);
				System.out.println("result code: " + resultCode);
				if (resultCode == 0 || resultCode == 500){
					loggedIn();
				} else {
					Main.getClient().closeConnection();
					connectToTheServer();
					loginNeeded();
				}
			}
		}).start();

	}
	public void connectToTheServer(){
		Client client = new Client("188.79.140.63", 52216);
		while (!client.isConnected()) {
			client.startConnection();
			System.out.println("TRYING!");

		}
		System.out.println("CONNECTED!");
		Main.setClient(client);

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
package com.example.irontextapp.activities;

import android.content.Intent;
import android.os.IInterface;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.Main;
import com.example.irontextapp.R;
import com.example.irontextapp.Utils.Tuple2;

import java.io.IOException;
import java.util.Scanner;

public class LoginActivity extends AppCompatActivity {

	@Override public void onBackPressed(){/*Do nothing*/}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (getSupportActionBar() != null) getSupportActionBar().hide();
		Button loggin = findViewById(R.id.logInButton);
		loggin.setOnClickListener(view ->{
			new Thread(() -> {
				// Allow toasts
				Looper.prepare();


				EditText emailInput = findViewById(R.id.emailInput);
				EditText passwordInput = findViewById(R.id.passwordInput);
				String email = emailInput.getText().toString();
				String password = passwordInput.getText().toString();


				//TODO: ADD PASSWORD AND EMAIL REGEX CHECK
				try {
					if (!Main.getClient().isConnected()) {
						Main.getClient().startConnection();
					}
					if (Main.getClient().isLoggedIn()) {
						Main.getClient().closeConnection();
						Main.getClient().startConnection();
					}
				} catch (Exception e){
					sendErrorMessage("Could not connect to the server", 1);
				}

				Tuple2<Integer, String> result = Main.getClient().passwordAuth(email, password);
				int resultCode = result.getFirst();
				String newToken = result.getSecond();
				if (newToken!= null){
					//TODO: MAKE TOKEN WORK
				}
				switch (resultCode){
					case -1 :
						sendErrorMessage("Unknown error, please try again or report this", 1);
						break;

					case 200:
						sendErrorMessage("Incorrect password", 1);
						break;

					case 201:
						sendErrorMessage("Invalid password", 1);
						break;

					case 300:
						sendErrorMessage("NON-existing email", 1);
						break;

					case 301:
						sendErrorMessage("Invalid email", 1);
						break;

					case 0:
						sendErrorMessage("SUCCESS", 1);
						loggedIn();
						break;

				}
			}).start();
		});

		Button registerButton = findViewById(R.id.registerButton);
		registerButton.setOnClickListener(view ->{
			Intent registerActivity = new Intent(this, RegisterActivity.class);
			startActivity(registerActivity);
		});
	}
	public void sendErrorMessage(String text, int duration){
		Toast.makeText(getApplicationContext(), text, duration).show();
	}
	public void loggedIn(){
		Intent chatActivity = new Intent(this, ChatActivity.class);
		startActivity(chatActivity);
	}
}
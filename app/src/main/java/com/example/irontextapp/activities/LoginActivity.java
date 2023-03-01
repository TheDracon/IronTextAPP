package com.example.irontextapp.activities;

import android.content.Intent;
import android.os.IInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.Main;
import com.example.irontextapp.R;

public class LoginActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (getSupportActionBar() != null) getSupportActionBar().hide();
		Button loggin = findViewById(R.id.logInButton);
		loggin.setOnClickListener(view ->{
			EditText emailInput = findViewById(R.id.emailInput);
			EditText passwordInput = findViewById(R.id.passwordInput);
			String email = emailInput.getText().toString();
			String password = passwordInput.getText().toString();


			// TODO: ADD PASSWORD AND EMAIL REGEX CHECK

			if (Main.getClient().isLoggedIn()) {
				Main.getClient().closeConnection();
				Main.getClient().startConnection();
			}
			int resultCode = Main.getClient().passwordAuth(email, password);
			switch (resultCode){

				case -1 : {
					sendErrorMessage("Unknown error, please try again or report this", 1);
				}
				case 200:{
					sendErrorMessage("Incorrect password", 1);
				}
				case 201:{
					sendErrorMessage("Invalid password", 1);
				}
				case 300:{
					sendErrorMessage("NON-existing email", 1);
				}
				case 301:{
					sendErrorMessage("Invalid email", 1);
				}
				case 0:{
					loggedIn();
				}
			}
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
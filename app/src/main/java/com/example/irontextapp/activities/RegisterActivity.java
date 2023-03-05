package com.example.irontextapp.activities;

import android.content.Intent;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.Main;
import com.example.irontextapp.R;

public class RegisterActivity extends AppCompatActivity {
	@Override public void onBackPressed(){/*Do nothing*/}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		if (getSupportActionBar() != null) getSupportActionBar().hide();

		Button registerButton = findViewById(R.id.registerButton);
		Button loginButton = findViewById(R.id.logInButton);

		registerButton.setOnClickListener(view ->{
			new Thread(()->{
				Looper.prepare();
				EditText emailInput = findViewById(R.id.emailInput);
				EditText usernameInput = findViewById(R.id.nicknameInput);

				EditText passwordInput = findViewById(R.id.passwordInput);
				EditText repeatPasswordInput = findViewById(R.id.repeatPasswordInput);
				String email = emailInput.getText().toString();
				String password = passwordInput.getText().toString();
				String repeatPassword = repeatPasswordInput.getText().toString();
				String username = usernameInput.getText().toString();

			if (!password.equals(repeatPassword)){
					sendErrorMessage("Passwords are not the same", 1);
				} else {

					if (Main.getClient().isLoggedIn()) {
						Main.getClient().closeConnection();
						Main.getClient().startConnection();
					}
					int resultCode = Main.getClient().registerAcc(username,email, password);
					System.out.println("RESULT CODE:" + resultCode);

					switch(resultCode){
						case 201:
							sendErrorMessage("Invalid password, password must contain at least 8 characters with numbers, capital and lowercase letters", 1);
							break;

						case 301:
							sendErrorMessage("Invalid Email", 1);
							break;


						case 302:
							sendErrorMessage("Email already registered", 1);
							break;

						case 400:
							sendErrorMessage("Invalid username", 1);
							break;

						case 401:
							sendErrorMessage("Invalid username", 1);
							break;

						case 0:
							sendErrorMessage("Succsess", 1);

							//TODO: GUARD THE USERNAME AND EMAIL

							goToLogin();
							break;

					}
				}
			}).start();
		});
		loginButton.setOnClickListener(view -> goToLogin());
	}
	public void sendErrorMessage(String text, int duration){
		Toast.makeText(getApplicationContext(), text, duration).show();
	}
	public void goToLogin(){
		Intent chatActivity = new Intent(this, LoginActivity.class);
		startActivity(chatActivity);
	}
}
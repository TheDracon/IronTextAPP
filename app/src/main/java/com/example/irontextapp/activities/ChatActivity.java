package com.example.irontextapp.activities;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.Main;
import com.example.irontextapp.Message;
import com.example.irontextapp.MessageAdapter;
import com.example.irontextapp.R;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
	private EditText editText;
	private MessageAdapter messageAdapter;
	private ListView messagesView;

	@Override public void onBackPressed(){/*Do nothing*/}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO: MAKE THIS ACTUALLY WORK!
		try {
			//socket = client.startConnection();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		// Hide ActionBar
		if (getSupportActionBar() != null) getSupportActionBar().hide();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		editText = findViewById(R.id.editMessage);
		messageAdapter = new MessageAdapter(this);
		messagesView = findViewById(R.id.messages_view);
		messagesView.setAdapter(messageAdapter);
		messagesView.setSelection(messageAdapter.getCount()-1);


		ImageButton button = findViewById(R.id.sendMessageBtn);
		EditText inputField = editText;

		button.setOnClickListener(view -> {
			String text = inputField.getText().toString();

			//client.sendEvent(RequestTypes.SEND_MESSAGE, socket, text);
		});
	}



}
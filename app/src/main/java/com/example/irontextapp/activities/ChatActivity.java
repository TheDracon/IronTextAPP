package com.example.irontextapp.activities;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.example.irontextapp.*;
import kotlin.jvm.internal.MagicApiIntrinsics;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
	private EditText editText;


	private static MessageAdapter messageAdapter;
	public static ListView messagesView;

	@Override public void onBackPressed(){/*Do nothing*/}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
			System.out.println("SENDING... " + text);
			MessageFilterer messageFilterer = new MessageFilterer();
			int messageCorrectness = messageFilterer.isMessageCorrect(text, getResources());
			if (messageCorrectness == 0){
				new Thread(() ->Main.getClient().sendEvent(RequestTypes.SEND_MESSAGE,text)).start();
				inputField.setText("");
				System.out.println(UserDataManager.getUsername());
				Message currentMessage = new Message(text, UserDataManager.getUsername(), true, System.currentTimeMillis());
				messageAdapter.addToStart(currentMessage, this);
			}

		});
		Main.getClient().listenForPackets(this);
		new Thread(() -> Main.getClient().sendEvent(RequestTypes.REQUEST_MESSAGES,50)).start();

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		getDelegate().onDestroy();
		new Thread(() -> Main.getClient().sendEvent(3)).start();
		Main.getClient().closeConnection();

	}

	public static MessageAdapter getMessageAdapter() {
		return messageAdapter;
	}
}
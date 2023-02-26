package com.example.irontextapp.activities;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.irontextapp.DummyMessage;
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
	private Socket socket;

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

			//client.sendEvent(RequestTypes.SEND_MESSAGE, socket, text);
		});
	}

	private void listenForPackets(){
		new Thread(() ->{
			while (true){
				if (socket.isClosed()) break;
				try {
					DataInputStream input = new DataInputStream(socket.getInputStream());

					// 0 = one new message | 1 = x new messages
					int requestType = input.readInt();
					if (requestType == 0){
						String message = input.readUTF();
						String sender = input.readUTF();
						long timestamp = input.readLong();
						DummyMessage newMessage = new DummyMessage(message, sender, timestamp);

						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						String formattedDate = sdf.format(new Date(newMessage.getTimestamp()));
						System.out.println(" MESSAGE: '" +newMessage.getContent() +"' BY: '" + newMessage.getSender() + "' ON: " + formattedDate);



						// Do something

					} else if (requestType == 1){
						int amountOfRows = input.readInt();
						ArrayList<DummyMessage> messages = new ArrayList<>();
						for (int i = 0; i < amountOfRows; i++) {
							String content = input.readUTF();
							String sender = input.readUTF();
							long timestamp = input.readLong();
							DummyMessage currentMessage = new DummyMessage(content, sender, timestamp);
							messages.add(currentMessage);
						}
						for (DummyMessage message : messages){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							String formattedDate = sdf.format(new Date(message.getTimestamp()));
							System.out.println("MESSAGE: '" +message.getContent() +"' BY: '" + message.getSender() + "' ON: " + formattedDate);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

}
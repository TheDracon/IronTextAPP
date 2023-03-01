package com.example.irontextapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends BaseAdapter {

	List<Message> messages = new ArrayList<>();

	Context context;

	public MessageAdapter(Context context){
		Message message = new Message("content1", "Juan", false, 100);
		Message message1 = new Message("content1","Mario", false, 100);
		Message message2 = new Message("content1", "Tu", true, 100);
		Message message3 = new Message("content1asdasd \n asdnaksg \na sidias\n aishgiibiaiisgfb81 \n aisgcuiagsiid \n", "Alvaro", false, 100);
		Message message4 = new Message("content1asdasd \n asdnaksg \na sidias\n aishgiibiaiisgfb81 \n aisgcuiagsiid \n", "Joel", false, 100);

		Message message5 = new Message("content1", "Víctor", false, 100);
		Message message6 = new Message("soy gay", "un gay", false, 100);
		Message message7 = new Message("content1", "Lucas", false, 100);
		Message message8 = new Message("content1", "IdkMen", false, 100);

		messages.add(message);
		messages.add(message1);
		messages.add(message2);
		messages.add(message3);
		messages.add(message4);
		messages.add(message5);
		messages.add(message6);
		messages.add(message7);
		messages.add(message8);

		this.context = context;
	}


	public void add(Message message){
		this.messages.add(message);
		notifyDataSetChanged();
	}


	public void addToStart(Message message) {
		// Shift all elements to the right by one slot
		for (int i = messages.size() - 1; i >= 0; i--) {
			messages.set(i + 1, messages.get(i));
		}
		// Add the new item to the first slot
		messages.set(0, message);
		notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		return messages.size();
	}

	@Override
	public Object getItem(int i) {
		return messages.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {


		MessageViewHolder holder = new MessageViewHolder();
		LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		Message message = messages.get(i);

		if (message.isMine()) {
			view = messageInflater.inflate(R.layout.message1, null);
			holder.messageBody = view.findViewById(R.id.message_body);
			view.setTag(holder);
			holder.messageBody.setText(message.getContent());
		} else {
			view = messageInflater.inflate(R.layout.message2, null);
			holder.avatar = view.findViewById(R.id.avatar);
			holder.name =  view.findViewById(R.id.name);
			holder.messageBody =  view.findViewById(R.id.message_body);
			view.setTag(holder);

			holder.name.setText(message.getSender());
			holder.messageBody.setText(message.getContent());
		}

		return view;
	}
}
class MessageViewHolder {
	public View avatar;
	public TextView name;
	public TextView messageBody;
}
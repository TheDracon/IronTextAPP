package com.example.irontextapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.irontextapp.activities.ChatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageAdapter extends BaseAdapter {

	List<Message> messages = new ArrayList<>();

	Context context;

	public MessageAdapter(Context context){
		this.context = context;
	}


	public void add(Message message, AppCompatActivity a){
		this.messages.add(message);

		a.runOnUiThread(() -> {
			ListView listView = a.findViewById(R.id.messages_view);
			listView.smoothScrollToPosition(getCount());
			notifyDataSetChanged();
		});

	}


	public void addToStart(Message message, AppCompatActivity a) {

		if (Objects.equals(message.getSender(), UserDataManager.getUsername())){
			message.setMine(true);
		}
		// Shift all elements to the right by one slot
		if (!messages.isEmpty()){
			messages.add(message);
			for (int i = messages.size() - 1; i >= 0; i--) {
				if (i == messages.size()-1) continue;
				messages.set(i + 1, messages.get(i));
			}
			messages.set(0, message);
			a.runOnUiThread(() -> {
				ListView listView = a.findViewById(R.id.messages_view);
				listView.smoothScrollToPosition(getCount());
				notifyDataSetChanged();
			});
		} else {
			add(message, a);
		}
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
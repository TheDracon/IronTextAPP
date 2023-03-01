package com.example.irontextapp;

public class Message {


	private final String content;
	private final String sender;
	private final boolean isMine;
	private final long timestamp;

	public Message(String content, String sender, boolean isMine, long timestamp){
		this.content = content;
		this.sender = sender;
		this.isMine = isMine;
		this.timestamp = timestamp;
	}


	public String getContent() {
		return content;
	}

	public String getSender() {
		return sender;
	}

	public boolean isMine() {
		return isMine;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

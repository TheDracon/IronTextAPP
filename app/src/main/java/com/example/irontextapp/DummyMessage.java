package com.example.irontextapp;

public class DummyMessage {

	private String content;
	private String sender;
	private long timestamp;

	public DummyMessage(String content, String sender, long timestamp){
		this.content = content;
		this.sender = sender;
		this.timestamp = timestamp;
	}

	public String getContent() {
		return content;
	}

	public String getSender() {
		return sender;
	}

	public long getTimestamp() {
		return timestamp;
	}
}

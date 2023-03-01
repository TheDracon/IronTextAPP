package com.example.irontextapp;

public class Main {


	private static String myUsername;

	private static Client client;

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		Main.client = client;
	}
	public static String getMyUsername() {
		return myUsername;
	}

	public static void setMyUsername(String myUsername) {
		Main.myUsername = myUsername;
	}

}

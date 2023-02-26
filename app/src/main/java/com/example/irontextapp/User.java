package com.example.irontextapp;

public class User {


	private final String name;
	private final String color;

	public User(String name, String color){
		this.name = name;
		this.color = color;
	}
	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}
}

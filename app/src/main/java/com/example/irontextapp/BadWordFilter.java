package com.example.irontextapp;

import android.content.res.Resources;
import com.example.irontextapp.activities.ChatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class BadWordFilter {
	// USE https://www.freewebheaders.com/full-list-of-bad-words-banned-by-google/
	private Resources resources;
	public BadWordFilter(Resources resources){
		this.resources = resources;
	}

	public ArrayList<String> filter(String message) {
		message = clearMessage(message);
		System.out.println(message);
		// load word list
		ArrayList<String> badWords = new ArrayList<>();
		try { badWords = loadWords(); } catch (Exception exception){ exception.printStackTrace();}
		ArrayList<String> detectedWords = new ArrayList<>();

		for (String word : badWords){

			word = clearMessage(word);
			if (word == null){
				continue;
			}
 			if (message.contains(" " + word + " ") || message.startsWith(word + " ") || message.endsWith(" "+ word)){
				detectedWords.add(word);
			}


		}
		return detectedWords;
	}

	private ArrayList<String> loadWords() throws IOException {
		InputStream en = resources.openRawResource(R.raw.en);
		InputStream es = resources.openRawResource(R.raw.es);
		InputStream more = resources.openRawResource(R.raw.more);

		ArrayList<String> wordList = new ArrayList<>();



		InputStreamReader streamReaderEN = new InputStreamReader(en, StandardCharsets.UTF_8);
		BufferedReader readerEN = new BufferedReader(streamReaderEN);

		InputStreamReader streamReaderES = new InputStreamReader(es, StandardCharsets.UTF_8);
		BufferedReader readerES = new BufferedReader(streamReaderES);

		InputStreamReader streamReaderMR = new InputStreamReader(more, StandardCharsets.UTF_8);
		BufferedReader readerMR = new BufferedReader(streamReaderMR);

		for (String line; (line = readerES.readLine()) != null;) {
			wordList.add(line.replace("\n", ""));
		}
		for (String line; (line = readerEN.readLine()) != null;) {
			wordList.add(line.replace("\n", ""));
		}
		for (String line; (line = readerMR.readLine()) != null;) {
			wordList.add(line.replace("\n", ""));

		}
		return wordList;
	}
	private String clearMessage(String message){
		message = message.toLowerCase();

		message = message.replaceAll("1","i");
		message = message.replaceAll("!","i");
		message = message.replaceAll("3","e");
		message = message.replaceAll("4","a");
		message = message.replaceAll("@","a");
		message = message.replaceAll("5","s");
		message = message.replaceAll("7","t");
		message = message.replaceAll("0","o");
		message = message.replaceAll("9","g");
		message = message.replaceAll("\n","");


		message = message.replaceAll("[^a-zA-Z ]", "");
		return message;
	}
}

package com.example.irontextapp;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageFilterer {
    private String messageRegex;
    public MessageFilterer(){}

    public int isMessageCorrect(String message, Resources resources){
        if (message.length() > 2048){
            return MessageErrorCodes.MESSAGE_TOO_LONG;
        }
        System.out.println("received: " + message);
        BadWordFilter badWordFilter = new BadWordFilter(resources);
        ArrayList<String> badWords = badWordFilter.filter(message);
        System.out.println(message);
        if (!badWords.isEmpty()){
            if (badWords.size() >= message.split(" ").length / 5 - 1){
                return MessageErrorCodes.INAPPROPRIATE_WORD;
            }
        }

        return MessageErrorCodes.SUCCESS;
    }

}

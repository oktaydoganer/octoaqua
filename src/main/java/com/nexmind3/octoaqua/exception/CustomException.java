package com.nexmind3.octoaqua.exception;

import java.util.List;

public class CustomException extends Exception{


    public CustomException(String message){
        super(message);
    }

    public CustomException(List<String> messageList){
        super(String.join(",", messageList));
    }

    public CustomException() {}
}
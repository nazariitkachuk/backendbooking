package com.hotelapp.exceptions;

public class OverrideNotAllowedException extends RuntimeException {


    public OverrideNotAllowedException(String message){

        super("Data change not allowed :"+message);
    }
}

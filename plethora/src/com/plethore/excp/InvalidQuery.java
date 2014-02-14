package com.plethore.excp;

public class InvalidQuery extends Exception { 
    public InvalidQuery(String message){
    	super(message);
    }
}

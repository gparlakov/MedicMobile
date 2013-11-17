package com.parlakov.medic.exceptions;

/**
 * Created by georgi on 13-11-17.
 */
public class MedicException extends Exception {
    private String mMessage;
    private Exception mInnerException;

    @Override
    public String getMessage() {
        return mMessage;
    }

    private void setMessage(String message) {
        this.mMessage = message;
    }

    public Exception getInnerException() {
        return mInnerException;
    }

    private void setInnerException(Exception innerException) {
        mInnerException = innerException;
    }

    public MedicException(){
        this("");
    }

    public MedicException(String message){
        this(message, null);
    }

    public MedicException(String message, Exception innerException) {
        setMessage(message);
        setInnerException(innerException);
    }
}
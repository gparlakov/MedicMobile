package com.parlakov.medic.data;

/**
 * Created by georgi on 13-10-30.
 */
public class Data {
    private final String BASE_URL = "http://api.everlive.com/v1/";
    private final String API_KEY = "9ZjuCuRmsDNSzQgC";


    HttpRequester mRequester;

    public Users getDoctors() {
        return mDoctors;
    }

    Users mDoctors;

    public Data(){
        mRequester = new HttpRequester();
        mDoctors = new Users(BASE_URL, mRequester, API_KEY);
    }

}

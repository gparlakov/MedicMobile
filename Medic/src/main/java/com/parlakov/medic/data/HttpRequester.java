package com.parlakov.medic.data;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by georgi on 13-10-30.
 */
public class HttpRequester {

    HttpClient mClient;

    public HttpRequester(){
        mClient = new DefaultHttpClient();
    }




}

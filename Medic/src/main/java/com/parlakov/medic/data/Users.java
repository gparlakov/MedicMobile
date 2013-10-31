package com.parlakov.medic.data;

import com.google.gson.Gson;

import com.parlakov.medic.models.User;

/**
 * Created by georgi on 13-10-30.
 */
public class Users {

    private final String mBaseUrl;
    private final String mApiKey;
    private final HttpRequester mHttpRequester;

    public String mId;

    public Users(String baseUrl, HttpRequester requester, String apiKey) {
        mBaseUrl = baseUrl;
        mHttpRequester = requester;
        mApiKey = apiKey;
    }

    public String register(User user){
        Gson gson = new Gson();
        String data = gson.toJson(user);
        String response = mHttpRequester.httpPost(mBaseUrl + mApiKey + "/Users", data, null, null);
        if (response != null)
            mId = getId(response);

        return response;
    }

    public String login(User user){

        return null;
    }

    private String getId(String response) {
        String id = null;

        int idIndex = response.indexOf("Id");

        int start = idIndex + 5;
        int end = response.indexOf('"', start);

        id = response.substring(start, end);
        return id;
    }
}

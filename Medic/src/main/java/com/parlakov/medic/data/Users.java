package com.parlakov.medic.data;

import com.google.gson.Gson;

import com.parlakov.medic.models.LoginUser;
import com.parlakov.medic.models.User;
import com.parlakov.uow.IUsersRepository;

import java.util.Collection;

/**
 * Created by georgi on 13-10-30.
 */
public class Users implements IUsersRepository<User>{

    private final String mBaseUrl;
    private final String mApiKey;
    private final HttpRequester mHttpRequester;
    private final Gson mGson;

    public String mId;

    public Users(String baseUrl, HttpRequester requester, String apiKey) {
        mBaseUrl = baseUrl;
        mHttpRequester = requester;
        mApiKey = apiKey;
        mGson = new Gson();
    }

    public String register(Object user){

        String data = mGson.toJson(user);
        String response = mHttpRequester.httpPost(mBaseUrl + mApiKey + "/Users", data, null, null);
        if (response != null)
            mId = getId(response);

        return response;
    }

    public String login(String username, String password){
        LoginUser loginUser = new LoginUser(username, password);
        String data = mGson.toJson(loginUser);

        String url = mBaseUrl + mApiKey + "/oauth/token";

        String response = mHttpRequester.httpPost(url, data, null, null);

        return response;
    }

    @Override
    public User getById(String id) {
        return null;
    }

    @Override
    public Collection<User> getAll() {
        return null;
    }

    @Override
    public Boolean delete(User entity) {
        return null;
    }

    @Override
    public Boolean delete(String id) {
        return null;
    }

    @Override
    public User update(User entity) {
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

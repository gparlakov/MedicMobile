package com.parlakov.medic.data;

import com.google.gson.Gson;

import com.parlakov.medic.models.LoginUser;
import com.parlakov.medic.models.User;
import com.parlakov.uow.IUsersRepository;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by georgi on 13-10-30.
 */
public class Users implements IUsersRepository<User>{

    public static final String NOT_CREATED_RESPONSE = "Not created";
    private static final String NOT_LOGGED_RESPONSE = "Not logged";

    private final String mBaseUrl;
    private final String mApiKey;
    private final HttpRequester mHttpRequester;
    private final Gson mGson;

    public String mPrincipalId;
    public String mAccessToken;


    public Users(String baseUrl, HttpRequester requester, String apiKey) {
        mBaseUrl = baseUrl;
        mHttpRequester = requester;
        mApiKey = apiKey;
        mGson = new Gson();
    }

    public void register(Object user) throws IOException {
        String data = mGson.toJson(user);
        String response = mHttpRequester.httpPost(mBaseUrl + mApiKey + "/Users", data, null, null);
        if (response != null){
            mPrincipalId = getFromJson(response, "Id");
        }
    }

    public void login(String username, String password) throws IOException {
        LoginUser loginUser = new LoginUser(username, password);
        String data = mGson.toJson(loginUser);

        String url = mBaseUrl + mApiKey + "/oauth/token";

        String response = mHttpRequester.httpPost(url, data, null, null);
        if(response != null){
            mAccessToken = getFromJson(response, "access_token");
            mPrincipalId = getFromJson(response, "principal_id");
        }
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

    private String getFromJson(String jsonString, String fieldName) {

        String fieldValue = null;

        int fieldNameIndex = jsonString.indexOf(fieldName);

        int start = fieldNameIndex + fieldName.length() + 3;
        int end = jsonString.indexOf('"', start);

        fieldValue = jsonString.substring(start, end);
        return fieldValue;
    }

}

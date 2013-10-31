package com.parlakov.medic.data;

import android.net.Uri;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;

/**
 * Created by georgi on 13-10-30.
 */
public class HttpRequester {
    private static final String USER_AGENT_STRING = "Mozilla/5.0 (Windows NT 6.3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/30.0.1599.101 Safari/537.36";


    HttpClient mHttpClient;

    public HttpRequester(){
        mHttpClient = new DefaultHttpClient();
    }

    public String httpGet(String url, Header header, String contentType){
        if (contentType == null)
            contentType = "application/json";


        HttpGet getRequest = new HttpGet(url);
        getRequest.setHeader("User-Agent", USER_AGENT_STRING);
        getRequest.setHeader(new BasicHeader("Content-Type", contentType));
        if (header != null){
            getRequest.setHeader(header);
        }

        HttpResponse response;
        String result = null;
        try {
            response = mHttpClient.execute(getRequest);

            HttpEntity entity = response.getEntity();
            if (entity != null){
                result = convertStreamToString(entity.getContent());
            }

        }
        catch (Exception e) {
            Log.e("Exception on Get", e.getMessage());
            // TODO - refactor
            // throw new HttpException(e.getMessage());
        }

        return result;
    }

    public String httpPost(String url,String data, Header header, String contentType){
        if (contentType == null)
            contentType = "application/json";

        HttpPost postRequest = new HttpPost(url);

        HttpEntity requestEntity = getHttpEntity(data);
        postRequest.setEntity(requestEntity);

        postRequest.setHeader(new BasicHeader("Content-Type", contentType));

        if (header != null){
            postRequest.setHeader(header);
        }

        HttpResponse response;
        String result = null;
        try {
            response = mHttpClient.execute(postRequest);

            HttpEntity entity = response.getEntity();
            if (entity != null){
                result = convertStreamToString(entity.getContent());
            }

        }
        catch (Exception e) {
            // TODO - refactor
            // throw new HttpException(e.getMessage());
        }

        return result;
    }

    private HttpEntity getHttpEntity(String data) {
        HttpEntity entity = new BasicHttpEntity();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(output);
        try{
            writer.write(data);

            entity.writeTo(output);
        }
        catch (Exception e){}
        return entity;
    }

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}

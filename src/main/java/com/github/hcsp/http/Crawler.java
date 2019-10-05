package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String URI = "http://47.91.156.35:8000";
    private static final HttpClient httpClient = HttpClients.createDefault();

    private static String loginCookie(String username, String password) throws IOException {
        HttpPost httpPost = new HttpPost( URI + "/auth/login" );
        httpPost.setHeader( "Content-Type", "application/json" );
        httpPost.setHeader( "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36" );
        Map<String, String> user = new HashMap<String, String>();
        user.put( "username", username );
        user.put( "password", password );
        String body = JSON.toJSONString( user );
        httpPost.setEntity( new StringEntity( body ) );
        HttpResponse execute = httpClient.execute( httpPost );
        String message = execute.getHeaders( "Set-Cookie" )[0].toString();
        return message.split( ":" )[1].split( ";" )[0].trim();
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = loginCookie( username, password );
        HttpGet httpGet = new HttpGet( URI + "/auth" );
        httpGet.setHeader( "Cookie", cookie );
        HttpResponse execute = httpClient.execute( httpGet );
        return EntityUtils.toString( execute.getEntity(), "UTF-8" );
    }

    public static void main(String[] args) throws IOException {
        String message = loginAndGetResponse( "xdml", "xdml" );
        System.out.println( message );
    }
}

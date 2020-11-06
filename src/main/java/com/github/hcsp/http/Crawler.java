package com.github.hcsp.http;

//import org.apache.http.NameValuePair;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import jdk.nashorn.internal.runtime.regexp.RegExp;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
//import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = loginAndGetCookie(username, password);
        return auth(cookie);
    }

    public static String loginAndGetCookie(String username, String password) throws UnsupportedEncodingException {
        HttpPost request = new HttpPost("http://47.91.156.35:8000/auth/login");
        StringBuilder json = new StringBuilder();
        json.append("{");
        json.append("\"username\":\"xdml\",");
        json.append("\"password\":\"xdml\"");
        json.append("}");
        // send a JSON data
        request.setEntity(new StringEntity(json.toString()));
        request.setHeader("Content-Type", "application/json");
        request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36");
        String result = "";
        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(request)){
            result = EntityUtils.toString(response.getEntity());
            result = response.getFirstHeader("Set-Cookie").getValue().replaceAll("JSESSIONID=(.+?);.+", "$1");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String auth(String cookie) throws IOException {
        HttpGet request = new HttpGet("http://47.91.156.35:8000/auth");
        request.setHeader("Cookie", "JSESSIONID=" + cookie);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        return responseBody;
    }
}

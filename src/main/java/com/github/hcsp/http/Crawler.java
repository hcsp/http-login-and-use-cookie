package com.github.hcsp.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        String json = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0");
        HttpResponse httpResponse = httpClient.execute(httpPost);
        Header[] headers = httpResponse.getHeaders("Set-Cookie");
        String cookie = headers[0].getValue().split(";")[0];
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("Cookie", cookie);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream is = entity.getContent();
        return IOUtils.toString(is, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

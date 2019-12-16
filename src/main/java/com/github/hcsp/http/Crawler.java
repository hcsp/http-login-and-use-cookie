package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.163 Safari/535.1";
    public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
    public static final String LOGIN_URI = "http://47.91.156.35:8000/auth/login";
    public static final String AUTH_URI = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        String cookie = login(httpClient, username, password);
        return auth(httpClient, cookie);
    }

    public static String login(HttpClient httpClient, String username, String password) throws Exception {
        HttpPost postMethod = new HttpPost(LOGIN_URI);
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("username", username);
        paramMap.put("password", password);

        StringEntity stringEntity = new StringEntity(JSON.toJSONString(paramMap));
        postMethod.addHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        postMethod.addHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        postMethod.setEntity(stringEntity);
        CloseableHttpResponse postResponse = (CloseableHttpResponse) httpClient.execute(postMethod);
        return postResponse.getFirstHeader("Set-Cookie").getValue();
    }

    public static String auth(HttpClient httpClient, String cookie) throws Exception {
        HttpGet getMethod = new HttpGet(AUTH_URI);
        getMethod.addHeader("Cookie", cookie);
        CloseableHttpResponse getResponse = (CloseableHttpResponse) httpClient.execute(getMethod);
        return EntityUtils.toString(getResponse.getEntity());
    }

    public static void main(String[] args) throws Exception {
        String s = loginAndGetResponse("xdml", "xdml");
        System.out.println(s);
    }
}

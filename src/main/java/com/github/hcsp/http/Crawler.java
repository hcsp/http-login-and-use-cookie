package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod("http://47.91.156.35:8000/auth/login");

        postMethod.addRequestHeader("Content-Type", "application/json");
        postMethod.addRequestHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.3");

        Map<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        String json = JSONObject.toJSONString(user);
        RequestEntity entity = new StringRequestEntity(json, "application/json", "UTF-8");
        postMethod.setRequestEntity(entity);
        httpClient.executeMethod(postMethod);
        Header header = postMethod.getResponseHeader("Set-Cookie");
        String sessionId = header.getElements()[0].getValue();


        GetMethod getMethod = new GetMethod("http://47.91.156.35:8000/auth");
        getMethod.addRequestHeader(new Header("Cookie", "JSESSIONID="+sessionId));
        httpClient.executeMethod(getMethod);
        return getMethod.getResponseBodyAsString();

    }

    public static void main(String[] args) throws IOException {
        System.out.println(loginAndGetResponse("xdml", "xdml"));
    }
}

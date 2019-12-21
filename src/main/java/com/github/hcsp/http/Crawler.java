package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String, String> authDto = new HashMap<>();
        authDto.put("username", username);
        authDto.put("password", password);
        String authJson = JSON.toJSONString(authDto);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0");
        StringEntity entity = new StringEntity(authJson, "UTF-8");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = client.execute(httpPost);
        Header[] headers = response.getHeaders("Set-Cookie");
        String[] headerString = new String[0];
        if (headers.length == 1) {
            headerString = headers[0].toString().split("; ");
        }
        String jSessionId = "";
        if (headerString.length != 0) {
            String[] split = headerString[0].split("=");
            jSessionId = split[split.length - 1];
        }

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("JSESSIONID", jSessionId);
        CloseableHttpResponse verifyAuthResponse = client.execute(httpGet);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String body = handler.handleResponse(verifyAuthResponse);
        client.close();
        return body;
    }
}
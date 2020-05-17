package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //login
        String loginUrl = "http://47.91.156.35:8000/auth/login";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(loginUrl);

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        Map<String, String> loginForm = new HashMap<>();
        loginForm.put("username", username);
        loginForm.put("password", password);

        HttpEntity entity = new StringEntity(JSON.toJSONString(loginForm));
        httpPost.setEntity(entity);

        String JSESSIONID = null;
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity responseEntity = response.getEntity();

            String responseStr = IOUtils.toString(responseEntity.getContent());
            System.out.println(responseStr);

            Header[]  headers = response.getAllHeaders();
            for (Header header : headers) {
                System.out.println(header.toString());
            }
            Header setCookieHeader = response.getFirstHeader("Set-Cookie");
            JSESSIONID = setCookieHeader.getValue().split("; ")[0];

        } finally {
            response.close();
        }

        //get response with JSESSIONID
        return getResponseWithJSESSIONID(JSESSIONID);
    }

    private static String getResponseWithJSESSIONID(String JSESSIONID) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", JSESSIONID);

        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity1 = response.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed

            String responseStr = IOUtils.toString(entity1.getContent(), StandardCharsets.UTF_8);
            System.out.println(responseStr);
            return responseStr;
        } finally {
            response.close();
        }
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

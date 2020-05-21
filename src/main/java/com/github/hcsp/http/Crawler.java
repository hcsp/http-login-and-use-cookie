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
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String JSESSIONID = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36");
        Map<String, String> map = new HashMap();
        map.put("username", "xdml");
        map.put("password", "xdml");
        String json = JSON.toJSONString(map);
        HttpEntity entity = new StringEntity(json);
        httpPost.setEntity(entity);
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            HttpEntity entity2 = response2.getEntity();
            InputStream is = entity2.getContent();
            String html = IOUtils.toString(is,"UTF-8");
            // do something useful with the response body
            // and ensure it is fully consumed
            Header setCookieHeader = response2.getFirstHeader("Set-Cookie");
            JSESSIONID = setCookieHeader.getValue().split("; ")[0];
            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }
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

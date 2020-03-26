package com.github.hcsp.http;
import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = "http://47.91.156.35:8000/auth/login";
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            Header header = response.getLastHeader("Set-Cookie");
            String value = header.getValue();
            if (null != value) {
                String cookie = value.split(";")[0];
                HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
                httpGet.setHeader("Cookie", cookie);
                CloseableHttpResponse resp = httpClient.execute(httpPost);
                String text = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
                System.out.println("Auth Response = " + text);
                return text;
            }

            String text = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            System.out.println("Post Response = " + text);
            return text;
        } finally {
            response.close();
        }
    }
}

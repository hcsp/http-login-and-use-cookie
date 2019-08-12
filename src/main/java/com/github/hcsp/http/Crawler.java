package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
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
    private static final String URL = "http://47.91.156.35:8000/auth/login";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = getCookie(username, password);
        return getResponseBody(cookie);
    }

    private static String getCookie(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        httpPost.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.10 Safari/537.36 Edg/77.0.235.5");
        httpPost.setEntity(getPostEntity(username, password));

        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        Header[] headers = httpResponse.getAllHeaders();

        Map<String, String> headerMap = new HashMap<>();

        String cookie = null;
        for (Header header : headers) {
            if (header.getName().equals("Set-Cookie")) {
                cookie = header.getValue().split(";")[0];
            }
        }

        return cookie;
    }

    private static StringEntity getPostEntity(String username, String password) {
        Map<String, String> postBody = new HashMap<>();
        postBody.put("username", username);
        postBody.put("password", password);
        String jsonObject = JSON.toJSONString(postBody);
        return new StringEntity(jsonObject, "UTF-8");
    }

    private static String getResponseBody(String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(URL);
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        httpPost.addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        httpPost.addHeader("cookie", cookie);

        httpPost.setEntity(getPostEntity("xdml", "xdml"));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        InputStream inputStream = httpResponse.getEntity().getContent();
        String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        EntityUtils.consume(httpResponse.getEntity());
        return content;
    }

}

package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
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
    private static final String URI = "http://47.91.156.35:8000";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String cookie = getLoginCookie(httpClient, username, password);

        return getResponseBody(httpClient, cookie);
    }

    private static String getLoginCookie(CloseableHttpClient httpClient, String username, String password) throws IOException {
        String api = "/auth/login";
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String body = JSON.toJSONString(map);

        HttpPost httpPost = new HttpPost(URI + api);
        HttpEntity httpEntity = new StringEntity(body);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.36");
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String setCookieHeader = response.getHeaders("Set-Cookie")[0].toString();

        return setCookieHeader.split(":")[1].split(";")[0].trim();
    }

    private static String getResponseBody(CloseableHttpClient httpClient, String cookie) throws IOException {
        String api = "/auth";

        HttpGet httpGet = new HttpGet(URI + api);
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse response = httpClient.execute(httpGet);

        return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
    }

}

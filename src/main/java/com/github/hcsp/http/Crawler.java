package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
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
    public static final String URI = "http://47.91.156.35:8000";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        String cookie = getCookieByLoginResponse(username, password, httpClient);

        return getAuthResponseBody(httpClient, cookie);

    }

    private static String getAuthResponseBody(CloseableHttpClient httpClient, String cookie) throws IOException {
        HttpResponse response = getAuthResponse(httpClient, cookie);
        return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
    }

    private static HttpResponse getAuthResponse(CloseableHttpClient httpClient, String cookie) throws IOException {
        final String API = "/auth/";

        HttpGet httpGet = new HttpGet(URI + API);
        httpGet.setHeader("Cookie", cookie);
        return httpClient.execute(httpGet);
    }

    private static String getCookieByLoginResponse(String username, String password, CloseableHttpClient httpClient) throws IOException {
        HttpResponse response = getLoginResponse(username, password, httpClient);
        String setCookieHeader = response.getHeaders("Set-Cookie")[0].toString();

        return setCookieHeader.split(":")[1].split(";")[0].trim();
    }

    private static HttpResponse getLoginResponse(String username, String password, CloseableHttpClient httpClient) throws IOException {
        final String API = "/auth/login/";

        HttpPost httpPost = new HttpPost(URI + API);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36 Edg/80.0.361.111");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String body = JSON.toJSONString(map);
        HttpEntity httpEntity = new StringEntity(body);
        httpPost.setEntity(httpEntity);

        return httpClient.execute(httpPost);
    }

}

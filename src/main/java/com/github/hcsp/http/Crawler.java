package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private final static String LOGIN_URL = "http://47.91.156.35:8000/auth/login";
    private final static String AUTH_URL = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        String body = getStringBody(username, password);

        HttpPost postLogin = setupLogin(body);
        CloseableHttpResponse loginResponse = httpclient.execute(postLogin);

        String rawCookie = loginResponse.getFirstHeader("Set-Cookie").getValue();
        String sessionId = getSessionId(rawCookie);

        HttpGet getAuth = setupAuth(sessionId);
        CloseableHttpResponse authResponse = httpclient.execute(getAuth);

        InputStream inputStream = authResponse.getEntity().getContent();
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    private static String getSessionId(String rawCookie) {
        return rawCookie.split(";")[0];
    }

    private static HttpPost setupLogin(String body) throws UnsupportedEncodingException {
        HttpPost httpPost = new HttpPost(LOGIN_URL);

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        httpPost.setEntity(new StringEntity(body));

        return httpPost;
    }

    private static HttpGet setupAuth(String sessionId) {
        HttpGet httpGet = new HttpGet(AUTH_URL);
        httpGet.addHeader("Cookie", sessionId);

        return httpGet;
    }

    private static String getStringBody(String username, String password) {
        Map<String,String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        return JSON.toJSONString(map);
    }
}

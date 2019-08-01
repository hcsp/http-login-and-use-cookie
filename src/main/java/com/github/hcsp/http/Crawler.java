package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        final String loginUrl = "http://47.91.156.35:8000/auth/login";
        final String authUrl = "http://47.91.156.35:8000/auth";
        final String sessionKey = "JSESSIONID";
        CookieStore cookieStore = new BasicCookieStore();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        CloseableHttpClient httpclient = HttpClients.createDefault();

        executeLogin(context, httpclient, username, password, loginUrl);
        Cookie cookie = getCookie(cookieStore, sessionKey);
        return getLoginInfo(httpclient, cookie, authUrl);
    }

    private static void executeLogin(HttpClientContext context, CloseableHttpClient httpclient, String username, String password, String loginUrl) throws IOException {
        HttpPost loginPost = new HttpPost(loginUrl);
        loginPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        loginPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36");
        loginPost.setEntity(new StringEntity(GenerateLoginEntity(username, password)));
        httpclient.execute(loginPost, context);
    }

    private static Cookie getCookie(CookieStore cookieStore, String key) {
        return cookieStore.getCookies()
                .stream()
                .filter(x -> x.getName().equals(key))
                .findFirst()
                .get();
    }

    private static String getLoginInfo(CloseableHttpClient httpclient, Cookie cookie, String authUrl) throws IOException {
        HttpGet authGet = new HttpGet(authUrl);
        authGet.setHeader("Cookie", cookie.getName() + "=" + cookie.getValue());
        CloseableHttpResponse response = httpclient.execute(authGet);
        return EntityUtils.toString(response.getEntity());
    }

    private static String GenerateLoginEntity(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }
}

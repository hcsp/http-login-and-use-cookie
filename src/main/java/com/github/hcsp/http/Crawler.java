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
import java.util.List;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) {
        String result = "";
        try {
            CookieStore cookieStore = new BasicCookieStore();
            sendLogin(username, password, cookieStore);
            List<Cookie> cookieList = cookieStore.getCookies();
            for (Cookie cookie : cookieList) {
                if ("JSESSIONID".equals(cookie.getName())) {
                    String loginCookieValue = cookie.getValue();
                    result = getLoginInfo("JSESSIONID=" + loginCookieValue);
                }
            }
        } catch (IOException e) {
            System.out.println("登录失败");
            e.printStackTrace();
        }
        return result;
    }

    public static void sendLogin(String username, String password, CookieStore cookieStore) throws IOException {
        final String url = "http://47.91.156.35:8000/auth/login";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        StringEntity stringEntity = new StringEntity(createPostString(username, password));
        httpPost.setEntity(stringEntity);
        httpClient.execute(httpPost, context);
    }

    public static String createPostString(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }

    public static String getLoginInfo(String cookie) throws IOException {
        final String url = "http://47.91.156.35:8000/auth";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", cookie);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        response.getStatusLine();
        String userInfo = EntityUtils.toString(response.getEntity());
        return userInfo;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
        System.out.println(loginAndGetResponse("xdml", "xdml"));
    }
}

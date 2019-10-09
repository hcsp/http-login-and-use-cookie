package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
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
        String jsonData = dataToJson(username, password);
        StringEntity data = new StringEntity(jsonData);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String cookie = loginToGetCookie(httpClient, data);
        String content = getResponseContent(httpClient, cookie);
        return content;
    }

    private static String dataToJson(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }

    private static String loginToGetCookie(CloseableHttpClient httpClient, StringEntity data) throws IOException {
        HttpClientContext context = HttpClientContext.create();
        CookieStore cookieStore = new BasicCookieStore();
        context.setCookieStore(cookieStore);
        HttpPost post = new HttpPost("http://47.91.156.35:8000/auth/login");
        post.setHeader("Content-Type", "application/json");
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        post.setEntity(data);
        httpClient.execute(post, context);
        return cookieStore.getCookies().get(0).getValue();
    }

    private static String getResponseContent(CloseableHttpClient httpClient, String cookie) throws IOException {
        HttpGet get = new HttpGet("http://47.91.156.35:8000/auth");
        get.setHeader("Cookie", "JSESSIONID=" + cookie);
        CloseableHttpResponse response = httpClient.execute(get);
        return EntityUtils.toString(response.getEntity());
    }
}

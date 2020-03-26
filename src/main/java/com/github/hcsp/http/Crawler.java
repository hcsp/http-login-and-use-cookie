package com.github.hcsp.http;
import com.alibaba.fastjson.JSON;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {

    private static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36";

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom()
                                                    .setUserAgent(USER_AGENT)
                                                    .setDefaultCookieStore(cookieStore)
                                                    .build();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("Content-Type", "application/json");
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(params), ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            List<Cookie> cookies = cookieStore.getCookies();
            for (Cookie cookie : cookies) {
                System.out.println("cookie.getValue() = " + cookie.getValue());
            }
            CloseableHttpResponse resp = httpClient.execute(new HttpGet("http://47.91.156.35:8000/auth"));
            String text = EntityUtils.toString(resp.getEntity(), StandardCharsets.UTF_8);
            System.out.println("Auth Response = " + text);
            return text;
        } finally {
            response.close();
            cookieStore.clear();
        }
    }
}

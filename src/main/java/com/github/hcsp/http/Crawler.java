package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    private static String name;
    private static String value;
    private static String result;

    public static void main(String[] args) {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) {
        CookieStore cookieStore = new BasicCookieStore();
        HttpClient httpClient = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        context.setCookieStore(cookieStore);
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String params = JSON.toJSONString(map);
        System.out.println(params);
        httpPost.setEntity(new StringEntity(params, ContentType.APPLICATION_JSON));
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost, context);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                List<Cookie> cookies = cookieStore.getCookies();
                for (Cookie cookie : cookies) {
                    name = cookie.getName();
                    value = cookie.getValue();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.addHeader("content-type", "application/json");
            httpGet.addHeader("cookie", name + "=" + value);
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
            try {
                HttpResponse httpResponse = httpClient.execute(httpGet);
                String json = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
                result = json;
                System.out.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;

    }
}

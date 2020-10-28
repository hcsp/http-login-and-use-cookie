package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = postAndReturnCookie(username, password);
        return getWithCookies(cookie);
    }

    public static String postAndReturnCookie(String username, String password) throws IOException {
        // 创建Httpclient对象
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpclient1 = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        // 创建http POST请求
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        // 伪装浏览器请求
        httpPost.setHeader("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:81.0) Gecko/20100101 Firefox/81.0");
        httpPost.setHeader("Content-Type", "application/json");

        String Encoding = "UTF-8";
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String bodyData = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(bodyData, Encoding));

        CloseableHttpResponse response = null;
        String cookie = null;
        try {
            // 执行请求
            response = httpclient1.execute(httpPost);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity(),
                        "UTF-8");
                List<Cookie> cookies = cookieStore.getCookies();
                if (cookies.get(0).getName().equals("JSESSIONID")) {
                    cookie = cookies.get(0).getValue();
                }
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpclient1.close();
        }
        return cookie;
    }


    public static String getWithCookies(String cookie) throws IOException {
        CloseableHttpClient httpclient2 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("Cookie", "JSESSIONID=" + cookie);
        CloseableHttpResponse response2 = null;
        String answer = null;
        try {
            // 执行请求
            response2 = httpclient2.execute(httpGet);
            // 判断返回状态是否为200
            if (response2.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response2.getEntity(), "UTF-8");
                System.out.println(content);
                answer = content;
            }
        } finally {
            if (response2 != null) {
                response2.close();
            }
            httpclient2.close();
        }
        return answer;
    }
}

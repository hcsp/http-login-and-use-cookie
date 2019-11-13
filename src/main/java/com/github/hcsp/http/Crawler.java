package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String resultJson = null;
        CloseableHttpClient httpclient = null;
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpclient = builder.build();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        // 序列化参数
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String requestBody = JSON.toJSONString(map);

        // 设置登录请求的Header与body
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36");
        httpPost.setEntity(new StringEntity(requestBody));

        CloseableHttpResponse responseBylogin = httpclient.execute(httpPost);
        CloseableHttpResponse responseByAutoLogin = null;
        try {
            // 拿取登录后的cookie
            List<Cookie> cookies = httpCookieStore.getCookies();
            String jsessionid = cookies.get(0).getValue();

            // 发起自动登录的get请求
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.addHeader("Cookie", "JSESSIONID=" + jsessionid);
            responseByAutoLogin = httpclient.execute(httpGet);
            resultJson = IOUtils.toString(responseByAutoLogin.getEntity().getContent(), "UTF-8");
        } finally {
            responseBylogin.close();
            if (responseByAutoLogin != null) {
                responseByAutoLogin.close();
            }
        }
        return resultJson;
    }
}

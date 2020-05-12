package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Crawler {
    static String cookie;
    static <T extends HttpRequestBase> T normalizedDefaultHeader(T http) {
        http.setHeader("Content-Type", "application/json");
        http.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");
        if (Crawler.cookie != null) {
            http.setHeader("cookie", "JSESSIONID=" + Crawler.cookie);
        }
        return http;
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost headerAction = normalizedDefaultHeader(new HttpPost("http://47.91.156.35:8000/auth/login"));
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        String json = JSON.toJSONString(body);
        headerAction.setEntity(new StringEntity(json));
        try (CloseableHttpResponse loginResponse = httpClient.execute(headerAction)) {
            int statusCode = loginResponse.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                Header cookieBuffer = loginResponse.getFirstHeader("Set-Cookie");
                String pattern = "^JSESSIONID=(.*?);";
                // 创建 Pattern 对象
                Pattern r = Pattern.compile(pattern);
                // 现在创建 matcher 对象
                Matcher matcher = r.matcher(cookieBuffer.getValue());
                System.out.println(cookieBuffer.getValue());

                if (matcher.find()) {
                    Crawler.cookie = matcher.group(1);
                    System.out.println(Crawler.cookie);
                }
            }
        }

        CloseableHttpClient httpClient2 = HttpClients.createDefault();
        HttpGet authGet = normalizedDefaultHeader(new HttpGet("http://47.91.156.35:8000/auth"));
        authGet.setHeader("Cookie", "JSESSIONID=" + Crawler.cookie);
        try (CloseableHttpResponse authResponse = httpClient2.execute(authGet)) {
            int statusCode = authResponse.getStatusLine().getStatusCode();
            if (statusCode >= 200 && statusCode < 300) {
                HttpEntity entity = authResponse.getEntity();
                result = EntityUtils.toString(entity);
                EntityUtils.consume(entity);
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

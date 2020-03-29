package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String, String> userMap = new HashMap<>();
        userMap.put("username", username);
        userMap.put("password", password);
        String userJson = JSON.toJSONString(userMap);

        String authLogin = "http://47.91.156.35:8000/auth/login";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(authLogin);
        post.setHeader("content-type", "application/json");
        post.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        post.setEntity(new StringEntity(userJson, ContentType.APPLICATION_FORM_URLENCODED));
        CloseableHttpResponse loginResponse = httpClient.execute(post);
        try {
            int statusCode = loginResponse.getStatusLine().getStatusCode();
            if (statusCode < 200 || statusCode >= 300) {
                throw new HttpResponseException(statusCode, loginResponse.getStatusLine().toString() + "获取数据失败！请重试");
            }
            InputStream is = loginResponse.getEntity().getContent();
            String body = IOUtils.toString(is, "UTF-8");

            String s = JSON.toJSONString(body);
            if (!s.contains("登录成功")) {
                return s;
            }

            Header[] headers = loginResponse.getAllHeaders();
            String cookie = "";
            for (Header header : headers) {
                if (header.getName().equalsIgnoreCase("set-cookie")) {
                    cookie = header.getValue().split(";")[0];
                    break;
                }
            }

            // 利用cookie登录
            String login = "http://47.91.156.35:8000/auth/";
            HttpGet get = new HttpGet(login);
            get.setHeader("content-type", "application/json");
            get.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
            get.setHeader("cookie", cookie);
            loginResponse = httpClient.execute(get);
            String body1 = IOUtils.toString(loginResponse.getEntity().getContent(), "UTF-8");

            return body1;

        } finally {
            loginResponse.close();
        }

    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String BASE_URL = "http://47.91.156.35:8000";
    private static final String LOGIN_URL = BASE_URL + "/auth/login";
    private static final String AUTH_URL = BASE_URL + "/auth";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.102 Safari/537.36";
    private static final String CONTENT_TYPE = "application/json";

    public static String loginAndGetResponse(String username, String password) {
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultCookieStore(httpCookieStore);
        CloseableHttpClient httpClient = httpClientBuilder.build();

        HttpPost httpLoginPost = new HttpPost(LOGIN_URL);
        httpLoginPost.setHeader("Content-Type", CONTENT_TYPE);
        httpLoginPost.setHeader("User-Agent", USER_AGENT);

        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("username", username);
        bodyMap.put("password", password);
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(bodyMap), StandardCharsets.UTF_8);
        httpLoginPost.setEntity(stringEntity);
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(httpLoginPost);
            String entityString = EntityUtils.toString(httpResponse.getEntity());
            System.out.println("entityString = " + entityString);
            httpClient.close();
            return getAuthInfo(httpCookieStore);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取认证信息
     *
     * @param cookieStore cookieStore
     * @return 认证信息JSON字符串
     */
    private static String getAuthInfo(CookieStore cookieStore) {
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        try {
            CloseableHttpResponse httpResponse = httpClient.execute(new HttpGet(AUTH_URL));
            InputStream inputStream = httpResponse.getEntity().getContent();
            return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
}

package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    private static final String COOKIE_URL = "http://47.91.156.35:8000/auth/login";
    private static final String MESSAGE_URL = "http://47.91.156.35:8000/auth";
    private static final CloseableHttpClient httpclient = HttpClients.createDefault();

    public static String loginAndGetResponse(String username, String password) throws IOException {

        HttpPost httpPost = new HttpPost(COOKIE_URL);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36");

        Map<String, String> map = new HashMap<>(16);
        map.put("username", username);
        map.put("password", password);

        HttpEntity entity = new StringEntity(JSON.toJSONString(map));
        httpPost.setEntity(entity);
        CloseableHttpResponse loginResponse = httpclient.execute(httpPost);

        String result = getMessageByCookie(loginResponse.getFirstHeader("Set-Cookie").getValue());
        loginResponse.close();

        return result;
    }

    public static String getMessageByCookie(String cookie) throws IOException {
        HttpGet httpGet = new HttpGet(MESSAGE_URL);
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        HttpEntity entity1 = response.getEntity();
        InputStream is = entity1.getContent();
        // 设置解析编码为UTF-8
        String html = IOUtils.toString(is, "UTF-8");
        EntityUtils.consume(entity1);
        response.close();
        return html;
    }
}

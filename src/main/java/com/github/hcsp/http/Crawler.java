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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient aDefault = HttpClients.createDefault();
        Map<String, String> map = new HashMap<>();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", " application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        map.put("username", "xdml");
        map.put("password", "xdml");
        httpPost.setEntity(new StringEntity(JSON.toJSONString(map)));

        CloseableHttpResponse response = aDefault.execute(httpPost);
        String cookie = response.getFirstHeader("Set-Cookie").getValue().split(";")[0];
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse resp = aDefault.execute(httpGet);
        HttpEntity entity = resp.getEntity();
        InputStream content = entity.getContent();
        String body = IOUtils.toString(content, StandardCharsets.UTF_8);
        return body;
    }
}

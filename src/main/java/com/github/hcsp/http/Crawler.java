package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";
        HttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", USER_AGENT);
        httpPost.setEntity(new StringEntity(JSON.toJSONString(new HashMap<String, String>() {{
            put("username", username);
            put("password", password);
        }})));

        String cookie = client.execute(httpPost).getFirstHeader("Set-Cookie").getValue().split(";")[0];

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("User-Agent", USER_AGENT);
        httpGet.addHeader("Cookie", cookie);

        return EntityUtils.toString(client.execute(httpGet).getEntity(), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

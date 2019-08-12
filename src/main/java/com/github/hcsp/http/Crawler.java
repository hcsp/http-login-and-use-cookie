package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String LOGIN_URL = "http://47.91.156.35:8000/auth/login";
    private static final String GET_AUTH_URL = "http://47.91.156.35:8000/auth";
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient clt = HttpClients.createDefault();
        HttpPost post = new HttpPost(LOGIN_URL);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        HttpEntity e = new StringEntity(json);
        post.setEntity(e);
        post.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        post.setHeader("Content-Type", "application/json; charset=utf-8");
        HttpResponse loginRes = clt.execute(post);
        String cookie = loginRes.getHeaders("Set-Cookie")[0].getValue().split("[;]")[0];
        System.out.println("cookie = " + cookie);
        HttpGet get = new HttpGet(GET_AUTH_URL);
        get.setHeader("Cookie", cookie);
        HttpResponse authRes = clt.execute(get);
        InputStream res = authRes.getEntity().getContent();
        return IOUtils.toString(res, "utf-8");
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

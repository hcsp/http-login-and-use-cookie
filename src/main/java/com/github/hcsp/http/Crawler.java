package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Crawler {
    private static final String LOGIN = "http://47.91.156.35:8000/auth/login";
    private static final String AUTH = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String jsessionid = getCookie(username, password);
        HttpGet httpGet = new HttpGet(AUTH);
        httpGet.addHeader("Cookie", jsessionid);
        CloseableHttpResponse httpResponse1 = httpClient.execute(httpGet);
        String resource = new Scanner(httpResponse1.getEntity().getContent()).useDelimiter("\\Z").next();
        httpClient.close();
        return resource;

    }

    public static String getJsessionid(Header header) {
        String[] jsessionids = header.getValue().split(";");
        String jsessionid = jsessionids[0];
        return jsessionid;
    }

    public static Map<String, String> JsonMap(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return map;
    }

    public static String getCookie(String username, String password) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(LOGIN);
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Mobile Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");
        String json = JSONObject.toJSONString(JsonMap(username, password));
        httpPost.setEntity(new StringEntity(json));
        CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
        String jsessionid = getJsessionid(httpResponse.getFirstHeader("Set-Cookie"));
        return jsessionid;
    }
}

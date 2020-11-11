package com.github.hcsp.http;

import org.apache.http.Header;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static String login(String website, String username, String password) throws IOException {
//      用post方法模拟登录,得到cookie
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(website);
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");
        // 登录body
        Map<String, String> auth_info = new HashMap<>();
        auth_info.put("username", username);
        auth_info.put("password", password);
        String json_string = JSON.toJSONString(auth_info);
        httpPost.setEntity(new StringEntity(json_string));
        CloseableHttpResponse response = httpClient.execute(httpPost);
        // 得到Set_Cookie，并转为Cookie
        System.out.println(response.getStatusLine());
        Header[] headers = response.getHeaders("Set-Cookie");
        String cookie = headers[0].toString();
        cookie = cookie.substring(cookie.indexOf("JSESSIONID"), cookie.indexOf(";"));
        response.close();

        return cookie;
    }

    private static String getResponse(String website, String cookie) throws IOException {
        // 3.用get方法发送request，得到json
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(website);
        httpGet.setHeader("Cookie", cookie);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        response.getEntity();
        String result = EntityUtils.toString(response.getEntity());
        response.close();

        return result;
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String website = "http://47.91.156.35:8000/auth/login";
        String website_auth = website.substring(0, website.lastIndexOf("/"));

        String cookie = login(website, username, password);
        return getResponse(website_auth, cookie);
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

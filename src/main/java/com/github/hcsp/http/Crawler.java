package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String url = "http://47.91.156.35:8000/auth";
        HttpPost login = new HttpPost(url + "/login");
        //设置header
        login.setHeader("Content-Type", "application/json");
        login.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3719.400 QQBrowser/10.5.3715.400");
        //设置body
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        login.setEntity(new StringEntity(json, "UTF-8"));
        //获取cookie
        CloseableHttpResponse response = httpclient.execute(login);
        Header[] headers = response.getHeaders("Set-Cookie");
        String cookie = headers[0].getValue();

        return getRquest(httpclient, url, cookie);
    }


    private static String getRquest(CloseableHttpClient httpClient, String url, String cookie) throws IOException {
        HttpGet get = new HttpGet(url);
        String[] cookieParse = cookie.split(";");
        cookie = cookieParse[0];
        get.setHeader("Content-Type", "application/json");
        get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.25 Safari/537.36 Core/1.70.3719.400 QQBrowser/10.5.3715.400");
        get.setHeader("Cookie", cookie);
        CloseableHttpResponse response = httpClient.execute(get);
        return EntityUtils.toString(response.getEntity());
    }

}

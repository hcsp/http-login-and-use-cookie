package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.*;
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

    static final String WEB_SITE = "http://47.91.156.35:8000/auth";
    static final String ROUTER = "/login";
    static HttpPost httpPost = new HttpPost(WEB_SITE + ROUTER);
    static HttpGet httpGet = new HttpGet(WEB_SITE);
    static CloseableHttpClient  httpClient = HttpClients.createDefault();

    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String,String> user_info = new HashMap<>();

        user_info.put("username",username);
        user_info.put("password",password);

        String cookie = getCookie(user_info);

        httpGet.setHeader("Set-cookie",cookie);

        //获取结果集
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //结果集转换JSON格式
        String INFO = EntityUtils.toString(response.getEntity());

        return INFO;

    }

    public static String getCookie(Map<String,String> user) throws IOException {

        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.183 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");
        //格式化参数
        String params = JSON.toJSONString(user);

        httpPost.setEntity(new StringEntity(params));

        CloseableHttpResponse response = httpClient.execute(httpPost);

        Header[] headers = response.getHeaders("Set-cookie");

        String value = headers[0].toString();
        String COOKIE = value.substring(value.indexOf("JSESSIONID"),value.indexOf(";"));

        response.close();

        return COOKIE;
    }

}

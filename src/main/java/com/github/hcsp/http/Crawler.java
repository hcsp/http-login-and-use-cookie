package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        // 设置头部
        httpPost.addHeader("content-type", "application/json;charset=UTF-8");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.87 Safari/537.36");
        // 设置请求体
        httpPost.setEntity(getParams());
        CloseableHttpResponse responsePost = httpclient.execute(httpPost);
        // 拿到cookie
        Header[] getCookie = responsePost.getHeaders("set-cookie");
        String cookie = getCookie[0].getValue().substring(0, getCookie[0].getValue().indexOf("; Path=/; HttpOnly"));
        responsePost.close();

        // 发送get请求
        CloseableHttpClient httpclient2 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("cookie", cookie);
        CloseableHttpResponse responseGet = httpclient.execute(httpGet);
        HttpEntity getEntity = responseGet.getEntity();
        String result;
        result = IOUtils.toString(getEntity.getContent(), StandardCharsets.UTF_8);
        responseGet.close();
        return result;
    }

    public static HttpEntity getParams() throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");
        StringEntity json = new StringEntity(JSON.toJSONString(map));
        return json;
    }
}

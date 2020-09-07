package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
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
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie;
        String backjson;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");
// 然后使用你喜欢的JSON序列化库把这个map序列化成一个JSON字符串
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            Header[] mycookie = response.getHeaders("set-cookie");
            HttpEntity entity = response.getEntity();
            String s = mycookie[0].toString();
            String[] split = s.split(";");
            String[] cookieList = split[0].split(" ");
            cookie = cookieList[1];
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
        CloseableHttpClient httpclient1 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("cookie", cookie);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.122 Safari/537.36");
        httpGet.addHeader("Content-Type", "application/json");
        CloseableHttpResponse getresponse = httpclient1.execute(httpGet);
        try {
            HttpEntity httpEntity = getresponse.getEntity();
            InputStream content = httpEntity.getContent();
            backjson = IOUtils.toString(content, "utf-8");
            EntityUtils.consume(httpEntity);
        }finally {
            getresponse.close();
        }
        return backjson;

    }
}

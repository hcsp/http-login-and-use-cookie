package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //增加Header信息
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");

        //设置body信息
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        JSONObject jsonObject = new JSONObject(map);
        httpPost.setEntity(new StringEntity(jsonObject.toString()));

        CloseableHttpResponse response = httpclient.execute(httpPost);

        //获取cookie内容
        Header[] cookie = response.getHeaders("set-cookie");
        int startIndex = cookie[0].toString().indexOf(":") + 2;
        int endIndex = cookie[0].toString().indexOf(";") - 1;
        String cookieName = cookie[0].toString().substring(startIndex, endIndex);

        try {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        //发送GET请求
        CloseableHttpClient httpclient1 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
        httpGet.addHeader("Cookie", cookieName);

        CloseableHttpResponse response1 = httpclient.execute(httpGet);

        try {
            HttpEntity entity1 = response1.getEntity();
            //InputStream转String
            String jsonStr = IOUtils.toString(entity1.getContent(), StandardCharsets.UTF_8);
            return JSON.parseObject(jsonStr).toJSONString();
        } finally {
            response1.close();
        }
    }
}

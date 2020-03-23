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
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        Map paramMap = new HashMap<String, String>();
        paramMap.put("username", username);
        paramMap.put("password", password);
        String param = JSON.toJSONString(paramMap);
        try {
            HttpEntity httpEntity = new StringEntity(param);
            HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(httpEntity);
            CloseableHttpResponse response1 = httpclient.execute(httpPost);
            String cookie = response1.getFirstHeader("Set-Cookie").getValue().split(";")[0];
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.setHeader("Cookie", cookie);
            CloseableHttpResponse getRq = httpclient.execute(httpGet);
            return IOUtils.toString(getRq.getEntity().getContent(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

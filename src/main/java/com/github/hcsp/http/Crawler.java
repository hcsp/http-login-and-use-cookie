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
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String result = "";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //POST
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/76.0.3809.100 Chrome/76.0.3809.100 Safari/537.36");
        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("username", "xdml");
        userInfo.put("password", "xdml");
        StringEntity requestBody = new StringEntity(JSON.toJSONString(userInfo));
        httpPost.setEntity(requestBody);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String cookie = "";
        try {
            HttpEntity entity = response.getEntity();
            Header[] cookies = response.getHeaders("Set-Cookie");
            cookie = cookies[0].toString().split(": |; ")[1];
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }

        //GET
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Ubuntu Chromium/76.0.3809.100 Chrome/76.0.3809.100 Safari/537.36");
        httpGet.setHeader("Cookie", cookie);
        CloseableHttpResponse getResponse = httpclient.execute(httpGet);
        try {
            HttpEntity entity = getResponse.getEntity();
            result = IOUtils.toString(entity.getContent(), "UTF-8");
            EntityUtils.consume(entity);
        } finally {
            getResponse.close();
        }

        return result;
    }
}

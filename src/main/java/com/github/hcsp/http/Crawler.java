package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
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
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        //伪装成浏览器
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");
        //怎么给body塞值受阻过
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map); //map转json string
        httpPost.setEntity(new StringEntity(json, "UTF-8"));
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        System.out.println("response2******" + response2);

        String cookie = response2.getFirstHeader("Set-cookie").getValue().split(";")[0];
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse responseGet = httpclient.execute(httpGet);
        HttpEntity entityGet = responseGet.getEntity();
        String body = EntityUtils.toString(entityGet, "utf-8");
        return body;
    }
}



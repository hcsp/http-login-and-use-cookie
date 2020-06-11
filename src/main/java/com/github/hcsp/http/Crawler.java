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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String url = "http://47.91.156.35:8000/auth/login";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.121 Safari/537.36");
        Map<String, String> map = new HashMap<>();

        map.put("username", username);
        map.put("password", password);
        String jsonString = JSON.toJSONString(map);
        HttpEntity httpEntity = new StringEntity(jsonString, "UTF-8");

        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = httpclient.execute(httpPost);
        Header header = response.getFirstHeader("Set-Cookie");
        String cookie = header.getValue().split(";")[0];

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);
        response = httpclient.execute(httpGet);

        HttpEntity entity = response.getEntity();
        InputStream content = entity.getContent();
        String res = IOUtils.toString(content, "UTF-8");
        return res;
    }
}

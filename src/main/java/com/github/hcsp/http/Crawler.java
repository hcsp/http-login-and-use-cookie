package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String url = "http://47.91.156.35:8000/auth/login";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");
        String jsonLogin = JSON.toJSONString(map);
        HttpEntity httpEntity = new StringEntity(jsonLogin);
        httpPost.setEntity(httpEntity);

        CloseableHttpResponse response = httpclient.execute(httpPost);

        Header header = response.getFirstHeader("Set-Cookie");
        String cookie = header.getValue().split(";")[0];

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);

        response = httpclient.execute(httpGet);

        HttpEntity entity = response.getEntity();
        InputStream inputStream = entity.getContent();
        String isString = IOUtils.toString(inputStream, "UTF-8");

        return isString;
    }
}

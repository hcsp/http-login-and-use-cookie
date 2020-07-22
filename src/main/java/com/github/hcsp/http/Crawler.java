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

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws Exception {
        String cookie;
        String resJson;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");
        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");
        String json = JSON.toJSONString(map);
        httpPost.setEntity(new StringEntity(json));
        CloseableHttpResponse postResponse = httpclient.execute(httpPost);
        try {
            System.out.println(postResponse.getStatusLine());
            Header[] headers = postResponse.getHeaders("Set-Cookie");
            HttpEntity entity = postResponse.getEntity();
            String s = headers[0].toString();
            String[] split = s.split(";");
            String[] cookieList = split[0].split(" ");
            cookie = cookieList[1];
            System.out.println(cookie);
//            InputStream content = entity.getContent();
//            String html = IOUtils.toString(content, "utf-8");
            EntityUtils.consume(entity);
        } finally {
            postResponse.close();
        }


        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("cookie", cookie);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.89 Safari/537.36");
        CloseableHttpResponse getResponse = httpClient.execute(httpGet);
        try {
            System.out.println(getResponse.getStatusLine());
            HttpEntity entity = getResponse.getEntity();
            InputStream content = entity.getContent();
            String html = IOUtils.toString(content, "utf-8");
            resJson = html;
//            System.out.println(html);
            EntityUtils.consume(entity);
        } finally {
            getResponse.close();
        }
        return resJson;
    }
}

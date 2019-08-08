package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //创建HttpClient客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //创建请求方式  post  get  http://localhost:8888/demo/test/
        String uri = "http://47.91.156.35:8000/auth/login";
        HttpPost httpPost = new HttpPost(uri);
        //设置请求头
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        //将参数存在hashmap中，并转化为json字符串放在请求体重
        HashMap map = new HashMap();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        StringEntity stringEntity = new StringEntity(json, "UTF-8");
        httpPost.setEntity(stringEntity);

        //发送请求 获取响应数据
        CloseableHttpResponse response = httpClient.execute(httpPost);
        //将响应头存入hashmap中
        Header headers[] = response.getAllHeaders();
        String cookie = null;
        HashMap<String, String> headerMap = new HashMap<>();
        for (Header header : headers) {
            headerMap.put(header.getName(), header.getValue());
        }
        //获取服务端返回的cookie
        cookie = headerMap.get("Set-Cookie");
        //携带cookie发送另一个请求
        String content = connectWithCookie("http://47.91.156.35:8000/auth", cookie);

        //相应结果
//        int statusCode = response.getStatusLine().getStatusCode();
//        System.out.println(statusCode);

//        HttpEntity entity = response.getEntity();
//        InputStream stream = entity.getContent();
//        String html = IOUtils.toString(stream, "UTF-8");
//
//        EntityUtils.consume(entity);
        response.close();
        httpClient.close();
        return content;
    }

    private static String connectWithCookie(String url, String cookie) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Cookie", cookie);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        String content = EntityUtils.toString(httpEntity, "UTF-8");
        System.out.println(content);
        response.close();
        httpClient.close();
        return content;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

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
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        //1.1、创建一个客户端 【httpcomponents maven得来】
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //1.2、设置请求方式，这里是Get请求【httpcomponents maven得来】
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36\\n\"");

        //1.3、建立一个map集合
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        //1.4、将map转化为json
        String text = JSON.toJSONString(map);
        // 对一个响应体做一些有用的处理。Entity是实体
        StringEntity entity = new StringEntity(text);

        httpPost.setEntity(entity);

        CloseableHttpResponse responsePost = httpclient.execute(httpPost);
        System.out.println(responsePost.getStatusLine());

        String cookie = responsePost.getFirstHeader("Set-Cookie").getValue();
        System.out.println(cookie);
        responsePost.close();


        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", cookie);
        CloseableHttpResponse responseGet = httpclient.execute(httpGet);

        // 对一个响应体做一些有用的处理。HttpEntity是实体
        HttpEntity httpEntity = responseGet.getEntity();
        // 拿到请求的数据流 commons-io
        InputStream is = httpEntity.getContent();
        // 设置解析编码为UTF-8
        String html = (IOUtils.toString(is, "UTF-8"));
        // 关闭请求
        responseGet.close();
        return html;
    }
}

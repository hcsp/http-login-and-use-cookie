package com.github.hcsp.http;


import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;


public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        //构建一个Client
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        //伪装user-agent  header
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.87 Safari/537.36");


        // 构建body，然后使用你喜欢的JSON序列化库把这个map序列化成一个JSON字符串
        // 序列化
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);


        StringEntity stringEntity = new StringEntity(JSON.toJSONString(map));
        httpPost.setEntity(stringEntity);
        // 从这个东西里面拿出header
        CloseableHttpResponse response = httpClient.execute(httpPost);
        Header[] Header = response.getHeaders("Set-Cookie");
        // 出来的是数组，要用打印数组的方式来打印
        System.out.println(Arrays.toString(Header));
        // 拿出body
        String Body = EntityUtils.toString(response.getEntity());
        return Body;
    }
}

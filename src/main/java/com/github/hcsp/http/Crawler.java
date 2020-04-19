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
        //将参数放入map
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        //map序列化成一个JSON字符串
        String RequestJson = JSON.toJSONString(map);
        String cookie = sendHttpPost("http://47.91.156.35:8000/auth/login", RequestJson);
        return sendHttpGet("http://47.91.156.35:8000/auth", cookie);
    }

    //发送POST请求，传入url和json，返回cookie
    public static String sendHttpPost(String url, String json) throws IOException {
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            //请求地址
            HttpPost httpPost = new HttpPost(url);
            //添加请求头
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");

            //创建POST实体
            httpPost.setEntity(new StringEntity(json));

            try (CloseableHttpResponse response1 = httpclient.execute(httpPost)) {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                String cookie = response1.getFirstHeader("Set-Cookie").getValue();
                System.out.println(cookie);

                EntityUtils.consume(entity1);
                return cookie.replaceAll(";.*", "");
            }
        }
    }

    //发送GET请求，传入url和cookie，返回实体
    public static String sendHttpGet(String url, String cookie) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        httpGet.addHeader("Cookie", cookie);

        try (CloseableHttpResponse response2 = httpclient.execute(httpGet)) {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            //将entity实体存入字符串
            String result = EntityUtils.toString(entity2, "UTF-8");

            EntityUtils.consume(entity2);
            return result;
        }
    }

    public static void main(String[] args) throws IOException {
        String responseResult = loginAndGetResponse("xdml", "xdml");
        System.out.println(responseResult);
    }

}

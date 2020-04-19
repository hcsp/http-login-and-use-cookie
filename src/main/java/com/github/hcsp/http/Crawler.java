package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
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
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response1 = null;
        String result = "";
        String cookie = "";

        //请求地址
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //添加请求头
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        //将参数放入map
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        //map序列化成一个JSON字符串
        String RequestJson = JSON.toJSONString(map);

        //创建POST实体
        httpPost.setEntity(new StringEntity(RequestJson));
        response1 = httpclient.execute(httpPost);

        try {
            System.out.println(response1.getStatusLine());
//            System.out.println(response1);
            //将响应头放进数组
            Header[] headers = new Header[response1.getAllHeaders().length];
            headers = response1.getAllHeaders();
            //获取并截出JSESSIONID的值
            cookie = headers[6].toString();
            cookie = cookie.substring(cookie.indexOf("JSESSIONID="), cookie.indexOf("; Path"));
            cookie = cookie.substring(11);
            System.out.println(cookie);

            HttpEntity entity1 = response1.getEntity();
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36");
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse response2 = httpclient.execute(httpGet);
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();
            //将entity实体存入字符串
            result = EntityUtils.toString(entity2, "UTF-8");

            EntityUtils.consume(entity2);
        } finally {
            response2.close();
        }

        return result;
    }

    public static void main(String[] args) throws IOException {
        String responseResult = loginAndGetResponse("xdml", "xdml");
        System.out.println(responseResult);
    }

}

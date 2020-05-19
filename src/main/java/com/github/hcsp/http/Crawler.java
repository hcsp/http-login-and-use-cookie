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
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String loginUrl = "http://47.91.156.35:8000/auth/login";
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        //JSON.toJSONString是将对象转化为Json字符串//body
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);
        //HttpEntity其实相当于一个消息实体，内容是http传送的报文
        HttpEntity entity = new StringEntity(json);
        //使用HttpPost方法提交HTTP POST请求，则需要使用HttpPost类的setEntity方法设置请求参数
        //这里就是将用户名以及密码设为请求参数
        httpPost.setEntity(entity);
        String JSESSIONID = null;
        //得到post请求的响应
        CloseableHttpResponse response = httpclient.execute(httpPost);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity responseEntity = response.getEntity();
            String responseStr = IOUtils.toString(responseEntity.getContent());
            System.out.println(responseStr);

            Header[] headers = response.getAllHeaders();
            for (Header header : headers) {
                System.out.println(header.toString());
            }
            Header setCookieHeader = response.getFirstHeader("Set-Cookie");
            JSESSIONID = setCookieHeader.getValue().split("; ")[0];

        } finally {
            response.close();
        }
        //返回使用COOKIE发送请求的响应
        return getResponseWithJSESSIONID(JSESSIONID);
    }

    //使用cookie发送请求
    private static String getResponseWithJSESSIONID(String JSESSIONID) throws IOException {
        CloseableHttpClient httpClient2 = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", JSESSIONID);

        CloseableHttpResponse response2 = httpClient2.execute(httpGet);
        try {
            System.out.println(response2.getStatusLine());
            HttpEntity entity1 = response2.getEntity();

            String responseStr2 = IOUtils.toString(entity1.getContent(), "UTF-8");
            System.out.println(responseStr2);
            return responseStr2;
        } finally {
            response2.close();
        }
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}




package com.github.hcsp.http;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static void main(String[] args) throws Exception {
        Crawler.loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = null;
        //设置登录名字和密码
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        //JSONObject类的toJSONString():将一个实体对象转换成Json字符串
        String login_info = JSONObject.toJSONString(map);

        //创建HttpPost对象
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        //设置请求头
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.130 Safari/537.36");

        //设置请求参数
        StringEntity se = new StringEntity(login_info);
        httpPost.setEntity(se);

        CloseableHttpResponse response_post = httpclient.execute(httpPost);
        try {
            //获取cookie
            Header[] headers = response_post.getHeaders("Set-Cookie");
            StringBuilder cookie = new StringBuilder();
            for (Header h : headers) {
                cookie.append(h.getValue());
            }

            //创建httpget对象
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            CloseableHttpResponse response_get = httpclient.execute(httpGet);

            //设置cookie
            String JSESSIONID = headers[0].getValue().split(" ")[0];
            httpGet.addHeader("Cookie", JSESSIONID);
//            httpGet.setHeader("Cookie", cookie.toString().substring(0, cookie.indexOf(";")));
            try {
                HttpEntity entity2 = response_get.getEntity();
                String result = IOUtils.toString(entity2.getContent(), "utf-8");
                return result;
            } finally {
                response_get.close();
            }

        } finally {
            response_post.close();
        }
    }
}

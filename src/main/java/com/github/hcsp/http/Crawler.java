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
    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String getCookie(Header[] allHeaders) {
        for (Header allHeader : allHeaders) {
            if (allHeader.toString().startsWith("Set-Cookie")) {
                String[] split = allHeader.toString().split("\\s");
                return (split[1].substring(0, split[1].length() - 1));
            }
        }
        return "";
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        //得到一个Http客户
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //构造一个Post请求
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        Map<String, String> map = new HashMap<>();

        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);

        // 将请求实体设置到httpPost对象中
        StringEntity requestEntity = new StringEntity(json, "utf-8");
        requestEntity.setContentEncoding("UTF-8");
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(requestEntity);

        //模拟浏览器
        httpPost.addHeader("User-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        //用Http客户执行一次Post操作
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            //显示是否联通成功
            System.out.println(response.getStatusLine());
            //从返回的结果中得到头中的

            Header[] allHeaders = response.getAllHeaders();
            String cookie = getCookie(allHeaders);


            //得到一个Http客户
            CloseableHttpClient httpClient2 = HttpClients.createDefault();
            //构造一个Get请求
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");

            httpGet.addHeader("Cookie", cookie);

            CloseableHttpResponse response2 = httpClient2.execute(httpGet);

            HttpEntity httpEntity2 = response2.getEntity();
            InputStream is = httpEntity2.getContent();

            String html = IOUtils.toString(is, "UTF-8");
            System.out.println(html);

            return html;

        } finally {
            response.close();
        }

    }
}

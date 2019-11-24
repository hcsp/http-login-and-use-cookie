package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String Cookie1;

        Map<String, String> map = new HashMap<>();
        map.put("username", "xdml");
        map.put("password", "xdml");

        String json = JSON.toJSONString(map);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");

        httpPost.setEntity(new StringEntity(json));
        httpPost.addHeader("Content-Type", "application/json;charset=UTF-8");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36");
        CloseableHttpResponse response1 = httpclient.execute(httpPost);

        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();

            InputStream is = entity1.getContent();

            String html = IOUtils.toString(is, "UTF-8");
            System.out.println(response1.getAllHeaders()[6].toString().split(";")[0].split(" ")[1]);

            Cookie1 = response1.getAllHeaders()[6].toString().split(";")[0].split(" ")[1];


        } finally {
            response1.close();
        }

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth/");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36");
        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("Cookie", Cookie1);

        CloseableHttpResponse response2 = httpclient.execute(httpGet);
        try {

            System.out.println(response2.getStatusLine());
            HttpEntity entity2 = response2.getEntity();

            InputStream is = entity2.getContent();

            String html2 = IOUtils.toString(is, "UTF-8");

            System.out.println(html2);


            return html2;
        } finally {
            response1.close();
        }

    }
}

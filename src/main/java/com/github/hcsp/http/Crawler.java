package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) {
        String loginAndGetResponse = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        httpPost.setHeader("Content-Type", " application/json");

        Map<String,String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonParams = JSON.toJSONString(map);
        CloseableHttpResponse response1 = null;
        CloseableHttpResponse response2 = null;
        try {
            httpPost.setEntity(new StringEntity(jsonParams));
            response1 = httpclient.execute(httpPost);
            Header[] headers = response1.getHeaders("Set-Cookie");
            for(Header header : headers) {
                String setCookie = header.getValue().split(";")[0];
                HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
                httpGet.setHeader("Cookie", setCookie);
                response2 = httpclient.execute(httpGet);
                loginAndGetResponse = IOUtils.toString(response2.getEntity().getContent(), Charset.defaultCharset());
            }
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response1.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return loginAndGetResponse;
    }
}

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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent:", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36");
        Map<String, String> reqBody = new HashMap<>();
        reqBody.put("username", username);
        reqBody.put("password", password);
        String json = JSON.toJSONString(reqBody);
        StringEntity requestEntity = new StringEntity(json);
        httpPost.setEntity(requestEntity);
        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            Header[] headers = response.getHeaders("Set-Cookie");
            String cookie = headers[0].getValue().split(";")[0];
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.setHeader("Cookie", cookie);
            try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                HttpEntity entity = response1.getEntity();
                InputStream inStream = entity.getContent();
                return IOUtils.toString(inStream, StandardCharsets.UTF_8);
            }
        }
    }
}

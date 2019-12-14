package com.github.hcsp.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
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
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
        ObjectMapper mapper = new ObjectMapper();
        String json = "";
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", username);
        map.put("password", password);
        json = mapper.writeValueAsString(map);
        StringEntity requestEntity = new StringEntity(
                json,
                ContentType.APPLICATION_JSON);
        httpPost.setEntity(requestEntity);
        CloseableHttpResponse response2 = httpclient.execute(httpPost);

        try {
            Header[] cookie = response2.getHeaders("Set-Cookie");
            String JSESSIONID = cookie[0].getValue().split(" ")[0];
            HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
            httpGet.addHeader("cookie", JSESSIONID);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                System.out.println(response1.getEntity().toString());
                HttpEntity entity1 = response1.getEntity();
                String result = EntityUtils.toString(entity1);
                return result;
            } finally {
                response1.close();
            }
        } finally {
            response2.close();
        }
    }
}

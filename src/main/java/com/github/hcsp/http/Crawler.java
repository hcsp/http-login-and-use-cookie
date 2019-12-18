package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws UnsupportedEncodingException {
        CloseableHttpClient client = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.79 Safari/537.36");
        httpPost.setHeader("content-type", "application/json");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        httpPost.setEntity(new StringEntity(JSON.toJSONString(map)));

        String session = "";
        try {
            HttpResponse response = client.execute(httpPost);

            for (Header h : response.getHeaders("set-cookie")) {
                session = h.getValue().toString().split(";")[0];
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("cookie", session);

        String ret = "";
        try {
            HttpResponse response2 = client.execute(httpGet);
            try {
                ret = EntityUtils.toString(response2.getEntity());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
}

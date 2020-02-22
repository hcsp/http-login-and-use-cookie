package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) throws Exception {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost("http://47.91.156.35:8000/auth/login");
            post.addHeader("Content-Type", " application/json");
            post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            Map<String, String> map = new HashMap<>();
            map.put("username", "xdml");
            map.put("password", "xdml");
            String entity = JSON.toJSONString(map);
            post.setEntity(new StringEntity(entity));
            HttpResponse response = client.execute(post);
            Header[] headers = response.getHeaders("Set-Cookie");
            String cookies = null;
            for (Header header : headers) {
                if ("Set-Cookie".equals(header.getName())) {
                    cookies = header.getValue();
                }
            }
            String JSESSIONID = cookies.split(";")[0];
            HttpGet get = new HttpGet("http://47.91.156.35:8000/auth");
            get.addHeader("Content-Type", " application/json");
            get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36");
            get.addHeader("Cookie", JSESSIONID);
            String result = "";
            HttpResponse content = client.execute(get);
            InputStream isNew = content.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(isNew));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                result += s;
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

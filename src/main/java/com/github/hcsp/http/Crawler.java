package com.github.hcsp.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import com.alibaba.fastjson.JSONObject;

public class Crawler {

    public static String loginAndGetResponse(String username, String password) {
        String postUrl = "http://47.91.156.35:8000/auth/login";
        String getUrl = "http://47.91.156.35:8000/auth";
        String jSessionID = null;
        String result = " ";
        JSONObject userInfo = new JSONObject();
        userInfo.put("username", username);
        userInfo.put("password", password);
        HttpPost post = new HttpPost(postUrl);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            post.setHeader("Content-Type", "application/json;charset=utf-8");
            post.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
            StringEntity postingString = new StringEntity(userInfo.toString(), "utf-8");
            post.setEntity(postingString);
            HttpResponse response = httpClient.execute(post);
            Header header = response.getFirstHeader("Set-Cookie");
            HeaderElement[] headerElements = header.getElements();
            for (HeaderElement headerElement : headerElements) {
                if (headerElement.getName().equalsIgnoreCase("jsessionid")) {
                    jSessionID = headerElement.getValue();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            post.abort();
        }

        HttpGet get = new HttpGet(getUrl);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            get.setHeader("Content-Type", "application/json;charset=utf-8");
            get.addHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.116 Safari/537.36");
            get.addHeader("Cookie", "JSESSIONID=" + jSessionID);
            HttpResponse response = httpClient.execute(get);
            InputStream content = response.getEntity().getContent();
            BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                result += s;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            get.abort();
        }
        return result;
    }
}

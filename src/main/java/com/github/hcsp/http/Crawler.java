package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
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

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.122 Safari/537.36");

        HashMap<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);

        StringEntity stringEntity = new StringEntity(JSON.toJSONString(user));
        httpPost.setEntity(stringEntity);

        CloseableHttpResponse response = client.execute(httpPost);

        Header[] headers = response.getHeaders("set-cookie");

        String sessionCookieValue = headers[0].getValue();

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("cookie", sessionCookieValue);
        CloseableHttpResponse response1 = client.execute(httpGet);

        InputStream inputStream = response1.getEntity().getContent();

        String responseString = "";
        try (

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bf = new BufferedReader(inputStreamReader)

        ) {
            String buff = "";
            while ((buff = bf.readLine()) != null) {
                responseString += buff;
            }
        }

        return responseString;


    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");
    }
}

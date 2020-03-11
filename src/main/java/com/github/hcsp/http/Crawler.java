package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.util.HashMap;

public class Crawler {

    static class GitHubPullRequest {
        // Pull request的编号
        int number;
        // Pull request的标题
        String title;
        // Pull request的作者的 GitHub 用户名
        String author;

        GitHubPullRequest(int number, String title, String author) {
            this.number = number;
            this.title = title;
            this.author = author;
        }
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
//        System.out.println(httpPost);
        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("User-Agent:", " Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36");
        HashMap<String, String> user = new HashMap<>();
        user.put("username", username);
        user.put("password", password);
        StringEntity stringEntity = new StringEntity(JSON.toJSONString(user));
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response2 = httpclient.execute(httpPost);
        System.out.println(response2);
        Header[] headers = response2.getHeaders("set-cookie");
        String CookieValue = headers[0].getValue();
//        System.out.println(CookieValue);

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("cookie", CookieValue);
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        InputStream inputStream = response1.getEntity().getContent();
        String responseString = "";
        try (
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bf = new BufferedReader(inputStreamReader);

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



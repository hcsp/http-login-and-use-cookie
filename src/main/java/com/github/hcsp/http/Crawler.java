package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static String getCookie(CloseableHttpClient httpclient, String host, String path, String username, String password) throws IOException {
        HttpPost httpPost = new HttpPost(host + path);
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, "application/json");
        httpPost.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        httpPost.setEntity(new StringEntity(JSON.toJSONString(map)));

        CloseableHttpResponse response = httpclient.execute(httpPost);
        String cookie = response.getFirstHeader("Set-Cookie").getValue();
        response.close();
        return cookie;
    }

    private static String getUserInfo(CloseableHttpClient httpclient, String host, String getUserInfoPath, String cookie) throws IOException {
        HttpGet httpGet = new HttpGet(host + getUserInfoPath);
        httpGet.addHeader("Cookie", cookie);

        CloseableHttpResponse responseGet = httpclient.execute(httpGet);
        HttpEntity httpEntity = responseGet.getEntity();
        InputStream is = httpEntity.getContent();
        String responseJson = (IOUtils.toString(is, "UTF-8"));

        EntityUtils.consume(httpEntity);
        responseGet.close();
        return responseJson;
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String host = "http://47.91.156.35:8000";
        String loginPath = "/auth/login";
        String getUserInfoPath = "/auth";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        String cookie = getCookie(httpclient, host, loginPath, username, password);
        return getUserInfo(httpclient, host, getUserInfoPath, cookie);
    }

    public static void main(String[] args) throws IOException {
        String json = Crawler.loginAndGetResponse("xdml", "xdml");
        System.out.println(json);
    }
}

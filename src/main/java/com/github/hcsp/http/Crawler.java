package com.github.hcsp.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");

        httpGet.addHeader("Content-Type", "application/json");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:72.0) Gecko/20100101 Firefox/72.0");
        //Cookie 的获取我先在 postman 那里完成了获取
        httpGet.addHeader("Cookie", "JSESSIONID=A1A3E14A8BD78C979ACB1ECF50B15CCF");

        CloseableHttpResponse response = httpclient.execute(httpGet);

        HttpEntity entity = response.getEntity();

        return IOUtils.toString(entity.getContent(), StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws IOException {
        System.out.println(loginAndGetResponse("xdml", "xdml"));
    }
}

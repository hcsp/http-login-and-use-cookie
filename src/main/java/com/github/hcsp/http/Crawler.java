package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String LOGIN = "http://47.91.156.35:8000/auth/login";
    private static final String AUTH = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(LOGIN);
        // add Headers
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 Safari/537.36");
        httpPost.addHeader("Content-Type", "application/json");

        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String jsonString = JSON.toJSONString(map);

        InputStream stream = IOUtils.toInputStream(jsonString, StandardCharsets.UTF_8);
        basicHttpEntity.setContent(stream);

        httpPost.setEntity(basicHttpEntity);

        CloseableHttpResponse postResponse = null;
        CloseableHttpResponse getResponse = null;
        String result = "";
        try {
            postResponse = httpclient.execute(httpPost);
            Header[] cookieHeaders = postResponse.getHeaders("Set-Cookie");
            if (cookieHeaders != null && cookieHeaders.length > 0) {
                String JSESSIONID = cookieHeaders[0].getValue();
                HttpGet httpGet = new HttpGet(AUTH);
                httpGet.setHeader("Cookie", JSESSIONID);
                getResponse = httpclient.execute(httpGet);
                InputStream content = getResponse.getEntity().getContent();
                result = IOUtils.toString(content, StandardCharsets.UTF_8);
            }
        } finally {
            if (postResponse != null) {
                postResponse.close();
            }
            if (getResponse != null) {
                getResponse.close();
            }
        }
        return result;
    }
}

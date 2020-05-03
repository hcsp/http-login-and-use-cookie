package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Crawler {

    private static final String LOGIN_API = "http://47.91.156.35:8000/auth/login";
    private static final String AUTH_API = "http://47.91.156.35:8000/auth";
    private static final Header FAKE_USER_AGENT_HEADER = new BasicHeader("User-Agent",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36");

    public static String loginAndGetResponse(String username, String password) {
        try {
            String cookieKeyValue = login(username, password);
            return auth(cookieKeyValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String login(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(LOGIN_API);
        JSONObject requestBody = new JSONObject();
        requestBody.put("username", username);
        requestBody.put("password", password);
        StringEntity entity = new StringEntity(requestBody.toString(), ContentType.APPLICATION_JSON);
        httpPost.setEntity(entity);
        httpPost.setHeader(FAKE_USER_AGENT_HEADER);
        CloseableHttpResponse response = httpclient.execute(httpPost);
        String cookieKeyValue = "";
        try {
            int httpCode = response.getStatusLine().getStatusCode();
            if (httpCode == 200) {
                Header cookieHeader = response.getFirstHeader("Set-Cookie");
                if (cookieHeader != null) {
                    cookieKeyValue = cookieHeader.getValue().split(";")[0];
                }
            }
            return cookieKeyValue;
        } finally {
            response.close();
        }
    }

    public static String auth(String cookieKeyValue) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(AUTH_API);
        httpGet.setHeader(FAKE_USER_AGENT_HEADER);
        httpGet.setHeader(new BasicHeader("Cookie", cookieKeyValue));
        CloseableHttpResponse response = httpclient.execute(httpGet);
        try {
            return IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
        } finally {
            response.close();
        }
    }
}


package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.129 Safari/537.36";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String cookie = loginAndGetCookie(username, password);
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("User-Agent", USER_AGENT);
        httpGet.setHeader("Cookie", cookie);
        CloseableHttpResponse getResponse = httpclient.execute(httpGet);

        return EntityUtils.toString(getResponse.getEntity(), StandardCharsets.UTF_8);
    }

    public static String loginAndGetCookie(String username, String password) throws IOException {

        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("User-Agent", USER_AGENT);
        Map<String, String> requestParam = new HashMap<>();
        requestParam.put("username", username);
        requestParam.put("password", password);
        StringEntity requestParamStr = new StringEntity(JSON.toJSONString(requestParam));
        httpPost.setEntity(requestParamStr);
        CloseableHttpResponse postResponse = httpclient.execute(httpPost);
        Header[] cookie = postResponse.getHeaders("Set-Cookie");
        String SetCookie = cookie[0].getValue();
        int jsessionidIndex = SetCookie.indexOf("JSESSIONID");
        int semicolonIndex = SetCookie.indexOf(";");
        if (jsessionidIndex > -1) {
            return SetCookie.substring(jsessionidIndex, semicolonIndex);
        } else {
            return "";
        }
    }
}

package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
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
    private static final String LOGIN_URL = "http://47.91.156.35:8000/auth/login";
    private static final String AUTH_URL = "http://47.91.156.35:8000/auth";
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
    private static CloseableHttpClient httpclient = HttpClients.createDefault();
    private static String cookie;

    public static String loginAndGetResponse(String username, String password) throws IOException {
        httpPost(username, password);
        return httpGet();
    }

    public static String httpGet() throws IOException {
        HttpGet httpGet = new HttpGet(AUTH_URL);
        httpGet.addHeader("User-Agent", USER_AGENT);
        httpGet.addHeader("Set-Cookie", cookie);
        CloseableHttpResponse response = httpclient.execute(httpGet);

        System.out.println("cookie = " + cookie);
        try {
            HttpEntity entity = response.getEntity();

            InputStream content = entity.getContent();
            String result = IOUtils.toString(content, "utf-8");

            EntityUtils.consume(entity);
            return result;
        } finally {
            response.close();
        }
    }

    public static void httpPost(String username, String password) throws IOException {
        HttpPost httpPost = new HttpPost(LOGIN_URL);
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", USER_AGENT);

        String bodyString = stringify(username, password);

        httpPost.setEntity(new StringEntity(bodyString));
        CloseableHttpResponse response = httpclient.execute(httpPost);

        try {
            String statusLine = response.getStatusLine().toString();
            if (statusLine.contains("200")) {
                String firstHeader = response.getFirstHeader("Set-Cookie").toString();
                getCookieValue(firstHeader);
            }

            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } finally {
            response.close();
        }
    }

    /**
     * 将username和password组成的map转为JSON字符串
     *
     * @param username 用户名
     * @param password 密码
     * @return 转换后的JSON字符串 {username: xxx, password, yyy}
     */
    private static String stringify(String username, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        return JSON.toJSONString(body);
    }

    /**
     * 从set-cookie中截取出需要的cookie值，并设置
     * @param cookieUrl 包含cookie的header行
     */
    private static void getCookieValue(String cookieUrl) {
        String target = "JSESSIONID=";
        int start = cookieUrl.indexOf(target);
        int end = cookieUrl.indexOf(";");
        cookie = cookieUrl.substring(start, end);
    }
}

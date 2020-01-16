package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
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
    private static final String GET_LOGIN_INFO_URL = "http://47.91.156.35:8000/auth";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36";
    private static final String CONTENT_TYPE_JSON = "application/json";
    private static final CloseableHttpClient HTTP_CLIENT = createHttpClient();

    public static CloseableHttpClient createHttpClient() {
        return createHttpClient("DEFAULT");
    }

    public static CloseableHttpClient createHttpClient(String Type) {
        CloseableHttpClient httpClient = null;
        switch (Type) {
            case "DEFAULT":
                httpClient = HttpClients.createDefault();
                break;
            case "PROXY":
                HttpHost proxy = new HttpHost("127.0.0.1", 1080, "http");
                //把代理设置到请求配置
                RequestConfig defaultRequestConfig = RequestConfig.custom()
                        .setProxy(proxy)
                        .build();
                httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
                break;
        }
        return httpClient;
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {

        HttpPost httpPost = new HttpPost(LOGIN_URL);

        //伪造header
        httpPost.addHeader("user-agent", USER_AGENT);
        httpPost.addHeader("Content-Type", CONTENT_TYPE_JSON);

        //伪造body
        Map<String, String> loginInfoMap = new HashMap<>();

        loginInfoMap.put("username", username);
        loginInfoMap.put("password", password);

        String jsonString = JSON.toJSONString(loginInfoMap);

        StringEntity httpEntity = new StringEntity(jsonString);

        httpPost.setEntity(httpEntity);

        //发送请求
        CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);

        try {
            //解析登录response
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            String html = IOUtils.toString(inputStream, "UTF-8");
            String setCookie = response.getFirstHeader("Set-Cookie").getValue();

            //伪造登录setCookie
            CloseableHttpResponse loginUseSetCookieResponse = loginUseSetCookie(setCookie);

            //解析Cookie登录response
            HttpEntity loginUseSetCookieEntity = loginUseSetCookieResponse.getEntity();
            InputStream loginUseSetCookieInputStream = loginUseSetCookieEntity.getContent();
            String loginUseSetCookieHtml = IOUtils.toString(loginUseSetCookieInputStream, "UTF-8");

            EntityUtils.consume(entity);
            EntityUtils.consume(loginUseSetCookieEntity);
            System.out.println(loginUseSetCookieHtml);
            return loginUseSetCookieHtml;
        } finally {
            response.close();
        }
    }

    public static CloseableHttpResponse loginUseSetCookie(String setCookie) throws IOException {
        HttpGet httpGet = new HttpGet(GET_LOGIN_INFO_URL);
        httpGet.addHeader("Set-Cookie", setCookie);
        CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
        return response;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("bodanb", "bodanb");
    }
}

package com.github.hcsp.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        String res = null;
        CookieStore cookieStore = new BasicCookieStore();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
        HttpPost httpPost = new HttpPost("http://47.91.156.35:8000/auth/login");
        httpPost.addHeader("Content-Type", "application/json");
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.90 Safari/537.36");
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        JSONObject jsonObject = new JSONObject(map);
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        httpPost.setEntity(stringEntity);
        httpClient.execute(httpPost);
        String JSESSIONID = null;
        List<Cookie> cookies = cookieStore.getCookies();
        for (int i = 0; i < cookies.size(); i++) {
            if (cookies.get(i).getName().equals("JSESSIONID")) {
                JSESSIONID = cookies.get(i).getValue();
            }
        }
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.setHeader("Cookie", "JSESSIONID=" + JSESSIONID);
        CloseableHttpResponse response1 = httpClient.execute(httpGet);

        try {
            HttpEntity entity1 = response1.getEntity();
            InputStream is = entity1.getContent();
            JSONParser jsonParser = new JSONParser();

            JSONObject jo = (JSONObject) jsonParser.parse(
                    new InputStreamReader(is, "UTF-8"));
            res = jo.toString();
            EntityUtils.consume(entity1);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            response1.close();
        }
        return res;
    }

    public static void main(String[] args) {
        try {
            loginAndGetResponse("xdml", "xdml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

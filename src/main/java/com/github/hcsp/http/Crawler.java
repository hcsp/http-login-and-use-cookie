package com.github.hcsp.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.Response;


public class Crawler {
    private static String UA = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
    private static String LOGIN_URL = "http://47.91.156.35:8000/auth/login";
    private static String AUTH_URL = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String loginJson = createLoginJson(username, password);
        List<String> cookies = loginAndGetCookie(loginJson);
        String sessionID = parseCookieSessionID(cookies);

        return getAuthResponseString(sessionID);
    }

    private static List<String> loginAndGetCookie(String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.Companion.create(json, JSON);

        Request request = new Request.Builder()
                .url(LOGIN_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", UA)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.headers("Set-Cookie");
        }
    }

    private static String getAuthResponseString(String sessionID) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(AUTH_URL)
                .addHeader("Cookie", sessionID)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private static String parseCookieSessionID(List<String> cookies) {
        String session = cookies.get(0);
        return session.substring(0, session.indexOf(";"));
    }

    private static String createLoginJson(String username, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        return JSON.toJSONString(map);
    }
}

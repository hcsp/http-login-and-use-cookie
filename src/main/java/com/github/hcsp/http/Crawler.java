package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Crawler {
    public static final MediaType APPLICATION_JSON = MediaType.get("application/json; charset=utf-8");
    public static final String LOGIN = "http://47.91.156.35:8000/auth/login";
    public static final String AUTH = "http://47.91.156.35:8000/auth";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.66 Safari/537.36";
    static OkHttpClient client = new OkHttpClient();

    public static String loginAndGetResponse(String username, String password) {
        String result = "";
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        try {
            Response response = post(LOGIN, JSON.toJSONString(map));
            String cookie = response.header("Set-Cookie");
            result = get(AUTH, cookie);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, APPLICATION_JSON);
        Request request = new Request.Builder()
                .addHeader("User-Agent", USER_AGENT)
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response;
        }
    }

    static String get(String url, String cookie) throws IOException {
        Request request = new Request.Builder()
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("Cookie", cookie)
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }
}

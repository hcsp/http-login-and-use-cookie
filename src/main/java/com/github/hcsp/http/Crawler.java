package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;

import java.io.IOException;
import java.util.Objects;

public class Crawler {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 11_0_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.67 Safari/537.36")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.header("Set-Cookie");
        }
    }

    public static String runGet(String url, String cookie) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .header("cookie", cookie)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return Objects.requireNonNull(response.body()).string();
        }
    }

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String url = "http://47.91.156.35:8000/auth/login";
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("password", password);
        String cookie = post(url, json.toJSONString());
        String[] cookies = cookie.split(";");
        return runGet("http://47.91.156.35:8000/auth", cookies[0]);
    }

    public static void main(String[] args) throws IOException {
        String body = loginAndGetResponse("xdml", "xdml");
        System.out.println(body);
    }
}

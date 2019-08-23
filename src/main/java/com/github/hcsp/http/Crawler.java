package com.github.hcsp.http;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class Crawler {
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public static String loginAndGetResponse(String username, String password) throws IOException {
        String JSESSIONID = getCookie(username, password);

        String url2 = "http://47.91.156.35:8000/auth";
        Request request = new Request.Builder()
                .url(url2)
                .addHeader("Cookie", JSESSIONID)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    private static String getCookie(String username, String password) throws IOException {
        String url = "http://47.91.156.35:8000/auth/login";
        String json = "{\"username\": \"xdml\", \"password\": \"xdml\"}";
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:68.0) Gecko/20100101 Firefox/68.0")
                .build();
        String setCookie = "";
        try (Response response = client.newCall(request).execute()) {
            setCookie = response.header("set-cookie");
            return setCookie.split(";")[0];
        }
    }
}

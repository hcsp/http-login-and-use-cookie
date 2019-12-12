package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {

    private static final String loginUrl = "http://47.91.156.35:8000/auth/login";

    private static final String authUrl = "http://47.91.156.35:8000/auth";


    public static String loginAndGetResponse(String username, String password) throws IOException {

        String cookie = login(loginUrl, username, password);

        String requestBody = parseBody(authUrl, cookie);

        return requestBody;
    }

    private static String parseBody(String authUrl, String cookie) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(authUrl)
                .addHeader("Cookie", cookie)
                .get()
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        return response.body().string();

    }

    private static String login(String loginUrl, String username, String password) throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");

        Map<String, String> content = new HashMap<>();
        content.put("username", username);
        content.put("password", password);


        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(content));

        Request request = new Request.Builder()
                .url(loginUrl)
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36")
                .post(body)
                .build();

        Call call = okHttpClient.newCall(request);
        Response response = call.execute();

        String cookie = response.header("Set-Cookie");

        return cookie;
    }

    public static void main(String[] args) throws IOException {
        loginAndGetResponse("xdml", "xdml");

    }
}

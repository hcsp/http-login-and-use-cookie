package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String postBody = JSON.toJSONString(map);
        RequestBody body = RequestBody.create(postBody, MediaType.parse("application/json"));

        Request request = new Request.Builder()
                .url("http://47.91.156.35:8000/auth/login")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.100 Safari/537.36")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        List<String> cookieList = response.headers("Set-Cookie");
        String cookie = cookieList.get(0);

        Request authRequest = new Request.Builder()
                .url("http://47.91.156.35:8000/auth")
                .addHeader("Cookie", cookie)
                .build();
        Response authResponse = client.newCall(authRequest).execute();
        String responseBodyString = Objects.requireNonNull(authResponse.body()).string();

        return responseBodyString;
    }
}

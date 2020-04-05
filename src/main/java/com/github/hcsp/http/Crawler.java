package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    public static final MediaType JSONTYPE = MediaType.get("application/json; charset=utf-8");

    public static String loginAndGetResponse(String username, String password) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        String json = JSON.toJSONString(map);

        RequestBody body = RequestBody.create(JSONTYPE, json);
        Request request = new Request.Builder()
                .url("http://47.91.156.35:8000/auth/login")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            Headers responseHeaders = response.headers();
            String cookie = responseHeaders.get("Set-Cookie");

            Request newRequest = new Request.Builder()
                    .url("http://47.91.156.35:8000/auth")
                    .header("cookie", cookie)
                    .build();
            try (Response newResponse = client.newCall(request).execute()) {
                return newResponse.body().string();
            }
        }
    }
}

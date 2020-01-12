package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import okhttp3.*;
import java.io.IOException;
import java.util.*;

public class Crawler {
    public static final MediaType JSON_TYPE = MediaType.get("application/json; charset=utf-8");
    public static final String HTTP_HOST = "http://47.91.156.35:8000";

    static OkHttpClient client = new OkHttpClient();

    public static String loginAndGetResponse(String username, String password) throws IOException {


        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);

        String json = JSON.toJSONString(map);
        RequestBody body = RequestBody.create(json, JSON_TYPE);

        Request request = new Request.Builder()
                .url(HTTP_HOST + "/auth/login")
                .header("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String sessionID = Objects.requireNonNull(response.header("Set-Cookie")).split(";", 2)[0];
            return authAndGetResponse(sessionID);
        }
    }

    public static String authAndGetResponse(String sessionID) throws IOException {
        Request request = new Request.Builder()
                .url(HTTP_HOST + "/auth")
                .header("cookie", sessionID)
                .header("user-agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1")
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}

package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Crawler {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String loginAndGetResponse(String username, String password) {
        String header = "";
        String result = "";
        Map<String, String> login = new HashMap<>();
        login.put("username", "xdml");
        login.put("password", "xdml");
        String logInfo = JSONObject.toJSONString(login);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(logInfo, JSON);
        Request loginTry = new Request.Builder().post(requestBody)
                .url("http://47.91.156.35:8000/auth/login")
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.88 Safari/537.36")
                .build();
        try (Response execute = okHttpClient.newCall(loginTry).execute()) {
            header = execute.header("Set-Cookie");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Request secondlogin = new Request.Builder().get().url("http://47.91.156.35:8000/auth").addHeader("Cookie", header).build();
        try (Response execute = okHttpClient.newCall(secondlogin).execute()) {
            InputStream inputStream = execute.body().byteStream();
            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

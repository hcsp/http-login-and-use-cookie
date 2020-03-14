package com.github.hcsp.http;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    private static String url = "http://47.91.156.35:8000/auth/";
    private static String agent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36";

    public static String loginAndGetResponse(String username, String password) throws IOException {
        Map<String, List<Cookie>> cookieStore = new HashMap<>();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                cookieStore.put(httpUrl.host(), list);
            }

            @NotNull
            @Override
            public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                List<Cookie> cookies = cookieStore.get(httpUrl.host());
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);
        MediaType mediaType = MediaType.Companion.get("application/json; charset=utf-8");

        signUp(okHttpClient, jsonObject, mediaType);
        return logIn(okHttpClient);
    }

    private static void signUp(OkHttpClient okHttpClient, JSONObject jsonObject, MediaType mediaType) throws IOException {
        RequestBody requestBody = RequestBody.create(jsonObject.toJSONString(), mediaType);
        Request request = new Request.Builder().url(url + "login").header("User-Agent", agent).post(requestBody).build();
        Response response = okHttpClient.newCall(request).execute();
        response.close();
    }

    private static String logIn(OkHttpClient okHttpClient) throws IOException {
        Request request = new Request.Builder().url(url).build();
        Response response = okHttpClient.newCall(request).execute();
        String msg = IOUtils.toString(response.body().byteStream(), "UTF-8");
        response.close();
        return msg;
    }
}

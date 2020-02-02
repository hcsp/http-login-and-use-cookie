package com.github.hcsp.http;
import com.alibaba.fastjson.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.MediaType;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Crawler {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    public static final OkHttpClient client = new OkHttpClient();

    static String loginAndGetResponse(String username, String password) throws IOException {
        Crawler crawler = new Crawler();
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        JSONObject json = new JSONObject(map);
        String cookie = postMethod("http://47.91.156.35:8000/auth/login", json.toJSONString());
        return crawler.getMethod("http://47.91.156.35:8000/auth", cookie);
    }

    static String postMethod(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                .header("Content-Type", "application/json")
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        List<String> Cookielist = response.headers().values("Set-Cookie");
        String cookie = (Cookielist.get(0).split(";"))[0];
        return cookie;
    }

    String getMethod(String url, String cookie) throws IOException {
        System.out.println(cookie);
        Request request = new Request.Builder()
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.100 Safari/537.36")
                .header("Content-Type", "application/json")
                .header("Cookie", cookie)
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

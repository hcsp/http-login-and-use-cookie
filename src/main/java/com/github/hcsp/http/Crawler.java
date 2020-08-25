package com.github.hcsp.http;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    private static final String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36";
    private static final String contentType = "application/json";
    private static final String loginUrl = "http://47.91.156.35:8000/auth/login";
    private static final String getDataUrl = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) {
        String credentialJsonString = createCredentialJsonString(username, password);

        HttpRequest loginRequest = HttpRequest.post(loginUrl)
                .userAgent(userAgent)
                .contentType(contentType)
                .send(credentialJsonString);

        Map<String, List<String>> responseHeaders = loginRequest.headers();

        HttpRequest getRequest = HttpRequest.get(getDataUrl)
                .userAgent(userAgent)
                .header("Cookie", getJsessionID(responseHeaders));

        return getRequest.body();
    }

    public static String createCredentialJsonString(String username, String password) {
        Map<String, String> credentialMap = new HashMap<>();
        credentialMap.put("username", username);
        credentialMap.put("password", password);
        return JSON.toJSONString(credentialMap);
    }

    public static String getJsessionID(Map<String, List<String>> responseHeaders) {
        return responseHeaders.get("Set-Cookie").get(0).split(";")[0];
    }

    public static void main(String[] args) {
        loginAndGetResponse("xdml", "xdml");
    }
}

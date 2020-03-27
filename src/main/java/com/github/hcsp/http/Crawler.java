package com.github.hcsp.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler {
    private static final String LOGIN_URL = "http://47.91.156" + ".35:8000/auth/login";
    private static final String AUTH_URL = "http://47.91.156.35:8000/auth";
    private static final String USER_AGENT = "Mozilla/5.0 Firefox/26.0";

    public static String loginAndGetResponse(String username, String password) throws IOException {

        CookieStore basicCookieStore = new BasicCookieStore();

        CloseableHttpClient client = createCloseableClient(basicCookieStore);

        CloseableHttpResponse loginResponse = loginWith(client, username, password);

        if (isLoginSuccess(loginResponse)) {
            return getAuthStatus(AUTH_URL, client, basicCookieStore);
        }

        return "Status: " + loginResponse.getStatusLine().getStatusCode() + "; Login failed, please try again";
    }

    public static void main(String[] args) throws IOException {
        System.out.println(loginAndGetResponse("xdml", "xdml"));
    }

    private static CloseableHttpClient createCloseableClient(CookieStore cookieStore) {
        return HttpClients.custom()
                    .setUserAgent(USER_AGENT)
                    .setDefaultCookieStore(cookieStore)
                    .build();
    }

    private static CloseableHttpResponse loginWith(
            final CloseableHttpClient client,
            final String username,
            final String password) throws IOException {

        Map<String, String> payload = new HashMap<>();
        payload.put("username", username);
        payload.put("password", password);

        String credentialInJson = new ObjectMapper().writeValueAsString(payload);
        StringEntity entity = new StringEntity(credentialInJson);

        HttpPost httpPost = new HttpPost(LOGIN_URL);
        httpPost.addHeader("Content-type", "application/json");
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            return response;
        }
    }

    private static boolean isLoginSuccess(final CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    private static String getAuthStatus(final String url,
                                        final CloseableHttpClient client,
                                        final CookieStore cookieStore) throws IOException {
        String cookie = extractCookie(cookieStore);
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Cookie", cookie);
        try (CloseableHttpResponse secondRequest = client.execute(httpGet)) {
            return EntityUtils.toString(secondRequest.getEntity(), StandardCharsets.UTF_8);
        }
    }

    private static String extractCookie(final CookieStore cookieStore) {
        final List<Cookie> cookies = cookieStore.getCookies();
        final Cookie cookie = cookies.stream()
                .filter(c -> "JSESSIONID".equals(c.getName()))
                .findFirst()
                .get();

        return String.join("=", Arrays.asList(cookie.getName(), cookie.getValue()));
    }
}

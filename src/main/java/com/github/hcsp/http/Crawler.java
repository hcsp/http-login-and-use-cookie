package com.github.hcsp.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class Crawler {
    private static final String LOGIN_URL = "http://47.91.156" + ".35:8000/auth/login";
    private static final String REDIRECT_URL = "http://47.91.156.35:8000/auth";

    public static String loginAndGetResponse(String username, String password) throws IOException {

        CloseableHttpClient client = createCloseableClient();

        CloseableHttpResponse loginResponse = loginWith(client, username, password);

        if (isLoginSuccess(loginResponse)) {
            return redirectTo(REDIRECT_URL, client, loginResponse);
        }

        return "Status: " + loginResponse.getStatusLine().getStatusCode() + "; Login failed, please try again";
    }

    /**
     * simulate a browser using HttpClient api
     *
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient createCloseableClient() {
        return HttpClients.custom()
                    .setUserAgent("Mozilla/5.0 Firefox/26.0")
                    .build();
    }

    /**
     * try login with credentials
     *
     * @param client
     * @param username      given username
     * @param password      given password
     * @return              response to login request
     * @throws IOException
     */
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

    /**
     * Returns true, if login is successful
     * Returns false, should login fail
     *
     * @param response  response from login request
     * @return          true if login success
     *                  false otherwise
     */
    private static boolean isLoginSuccess(final CloseableHttpResponse response) {
        return response.getStatusLine().getStatusCode() == 200;
    }

    /**
     * Redirect when login success
     *
     * @param urlToRedirect     redirection URL
     * @param client            CloseableHttpClient
     * @param response          server response to login request
     * @return                  Response body of redirection request
     * @throws IOException
     */
    private static String redirectTo(final String urlToRedirect,
                                     final CloseableHttpClient client,
                                     final CloseableHttpResponse response) throws IOException {
        String cookie = extractCookie(response);
        HttpGet httpGet = new HttpGet(urlToRedirect);
        httpGet.setHeader("Cookie", cookie);
        try (CloseableHttpResponse redirectResponse = client.execute(httpGet)) {
            System.out.println("redirectResponse: " + redirectResponse.getStatusLine().getStatusCode());
            String redirectResponseBody = EntityUtils.toString(redirectResponse.getEntity(), StandardCharsets.UTF_8);
            return redirectResponseBody;
        }
    }

    /**
     * Extract cookie from login response for redirection request
     *
     * @param response  server response to login request
     * @return          cookie String from server response to login request
     */
    private static String extractCookie(final CloseableHttpResponse response) {
        String cookie;

        final String firstHeader = response.getFirstHeader("Set-Cookie").getValue();
        final String[] strArray = firstHeader.split(";");
        cookie = strArray[0];

        return cookie;
    }
}

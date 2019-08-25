package com.github.hcsp.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Crawler {
    public static String loginAndGetResponse(String username, String password) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("http://47.91.156.35:8000/auth");
        httpGet.addHeader("Cookie", "JSESSIONID=3761B320618328FCE0A494648ED3BF75; Path=/; HttpOnly");
        CloseableHttpResponse response1 = httpclient.execute(httpGet);
        String response = "";
        try {
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            response = EntityUtils.toString(entity1);
            EntityUtils.consume(entity1);
        } finally {
            response1.close();
        }
        return response;
    }
}

package com.github.hcsp.http;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CrawlerTest {
    @Test
    public void test() throws Exception {
        String json = Crawler.loginAndGetResponse("xdml", "xdml");
        System.out.println(json);
        Response response = com.alibaba.fastjson.JSON.parseObject(json, Response.class);
        Assertions.assertEquals("ok", response.status);
        Assertions.assertEquals("xdml", response.data.username);
    }

    static class Response {
        String status;
        User data;
        boolean isLogin;

        public void setStatus(String status) {
            this.status = status;
        }

        public void setData(User data) {
            this.data = data;
        }

        public void setLogin(boolean login) {
            isLogin = login;
        }
    }

    static class User {
        int id;
        String username;
        String avatar;
        String createdAt;
        String updatedAt;

        public void setId(int id) {
            this.id = id;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }
}

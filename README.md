# HTTP实战: 使用Java代码进行模拟登录

请完成[`Crawler`](https://github.com/hcsp/http-login-and-use-cookie/blob/master/src/main/java/com/github/hcsp/http/Crawler.java)中的程序，实现对指定的网站的模拟登录。这个过程分为两个步骤：

> 注意：你可以使用浏览器或者Postman先对这些接口进行测试。测试用户名/密码是`xdml`/`xdml`。

> 我们并没有列出完成这个需求的所有细节，因为我们希望能锻炼你自己搜索、阅读文档的能力——这对一个工程师是必不可少的技能。但是，我们仍然希望在你遇到解决不了的问题时能多思考、多问。祝你好运！

## 第一步，使用给定的用户名username和密码password进行模拟登录

登录接口：`http://47.91.156.35:8000/auth/login`

发送一个HTTP POST请求到该接口，其Header包括：

- Content-Type: application/json
- User-Agent: 伪装成浏览器的User-Agent，否则可能会触发反爬虫系统

Body是：

```
{"username": "<给定的用户名>", "password": "<给定的密码>"}
```

> 提示：这样的JSON字符串可以通过以下方式生成：

```
Map<String,String> map = new HashMap<>();
map.put("username", <给定的用户名>);
map.put("password", <给定的密码>);
// 然后使用你喜欢的JSON序列化库把这个map序列化成一个JSON字符串
String json = ...
```

服务器若返回成功的响应，则会在HTTP响应中包含一个形如`Set-Cookie: XXXXXXXXX; Path=/; HttpOnly`的Header。

### 第二步，解析Cookie并使用该Cookie发送请求

在拿到第一步中的`Set-Cookie: JSESSIONID=XXXXXXXXX; Path=/; HttpOnly`后，请使用Java代码获取其中的`JSESSIONID=XXXXXXXXX`值。

向`http://47.91.156.35:8000/auth`发送一个HTTP GET请求，其Header包括：

`Cookie: JSESSIONID=XXXXXXXXX`

返回得到的HTTP响应的Body字符串，它是一个JSON字符串。

祝你好运！

在提交Pull Request之前，你应当在本地确保所有代码已经编译通过，并且通过了测试(`mvn clean test`)

-----
注意！我们只允许你修改以下文件，对其他文件的修改会被拒绝：
- [src/main/java/com/github/hcsp/http/Crawler.java](https://github.com/hcsp/http-login-and-use-cookie/blob/master/src/main/java/com/github/hcsp/http/Crawler.java)
- [pom.xml](https://github.com/hcsp/http-login-and-use-cookie/blob/master/pom.xml)
-----


完成题目有困难？不妨来看看[写代码啦的相应课程](https://xiedaimala.com/tasks/661cd7ab-7fea-47d0-8e11-555d6fca751d)吧！

回到[写代码啦的题目](https://xiedaimala.com/tasks/661cd7ab-7fea-47d0-8e11-555d6fca751d/quizzes/6c87ef57-7f06-4af2-9112-86dd27ff099d)，继续挑战！

package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redirect 重定向.
 * 重定向是指当浏览器请求一个URL时，服务器返回一个重定向指令，告诉浏览器地址已经变了，麻烦使用新的URL再重新发送新请求。
 *
 * 1. 我们已经编写了一个能处理/hello的servlet，如果收到的路径为/hi，希望能重定向到/hello，{@link A_7_Redirect_1}
 *
 * 2. 如果浏览器发送GET /hi请求，hiServlet将处理此请求。由于hiServlet在内部又返回了"重定向响应"，因此，浏览器会收到如下响应：
 * Status Code: 302
 * Location: /hello
 *
 * 当浏览器收到302响应后，它会立刻根据Location的指示发送一个新的GET /hello请求，这个过程就是重定向：
 * ┌───────┐   GET /hi     ┌───────────────┐
 * │Browser│ ────────────> │RedirectServlet│
 * │       │ <──────────── │               │
 * └───────┘   302         └───────────────┘
 *
 *
 * ┌───────┐  GET /hello   ┌───────────────┐
 * │Browser│ ────────────> │ HelloServlet  │
 * │       │ <──────────── │               │
 * └───────┘   200 <html>  └───────────────┘
 * 观察Chrome浏览器的网络请求，可以看到两次HTTP请求：
 *
 * 3. 重定向有两种：
 * 一种是302响应，称为临时重定向，一种是301响应，称为永久重定向。
 * 两者的区别是，如果服务器发送301永久重定向响应，浏览器会缓存/hi到/hello这个重定向的关联，下次请求/hi的时候，浏览器就直接发送/hello请求了。
 * (见代码 {@link A_7_Redirect_1} )
 *
 * 3.1 定向有什么作用？
 * 目的是当Web应用升级后，如果请求路径发生了变化，可以将原来的路径重定向到新路径，从而避免浏览器请求原路径找不到资源。
 *
 *
 * 4. Forward
 * Forward是指内部转发。当一个Servlet处理请求的时候，它可以决定自己不继续处理，而是转发给另一个Servlet处理。
 *
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/hello")
public class A_7_Redirect extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 设置响应类型:
        resp.setContentType("text/html");
        // 如果需要写入中文，需要设置编码。
        resp.setCharacterEncoding("UTF-8");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, web-world!  hello页面 </h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();

    }
}


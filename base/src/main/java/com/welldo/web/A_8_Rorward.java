package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 4. Forward
 * Forward是指内部转发。当一个Servlet处理请求的时候，它可以决定自己不继续处理，而是转发给另一个Servlet处理。
 *
 * 我们已经编写了一个能处理/hello的 Servlet，继续编写一个能处理/sayhi 的 Servlet：
 * 在收到请求后，它并不自己发送响应，而是把请求和响应都转发给路径为/hello的Servlet，即下面的代码：
 *
 * 后续请求的处理实际上是由HelloServlet完成的。这种处理方式称为转发（Forward），我们用流程图画出来如下：
 *
 *                           ┌────────────────────────┐
 *                           │      ┌───────────────┐ │
 *                           │ ────>│ForwardServlet │ │
 * ┌───────┐  GET /morning   │      └───────────────┘ │
 * │Browser│ ──────────────> │              │         │
 * │       │ <────────────── │              ▼         │
 * └───────┘    200 <html>   │      ┌───────────────┐ │
 *                           │ <────│ HelloServlet  │ │
 *                           │      └───────────────┘ │
 *                           │       Web Server       │
 *                           └────────────────────────┘
 *
 * 转发和重定向的区别在于，转发是在Web服务器内部完成的，对浏览器来说，它只发出了一个HTTP请求：
 *
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/8sayhi")
public class A_8_Rorward extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 在收到请求后，它并不自己发送响应，而是把请求和响应都转发给路径为/hello的Servlet，即下面的代码：
        req.getRequestDispatcher("/7hello").forward(req, resp);
    }
}


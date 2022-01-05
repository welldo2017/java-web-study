package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet进阶
 *
 * 我们通过HttpServletRequest提供的接口方法可以拿到HTTP请求的几乎全部信息，常用的方法有：
 *
 * getMethod()：返回请求方法，例如，"GET"，"POST"；
 * getRequestURI()：返回请求路径，但不包括请求参数，例如，"/hello"；
 * getQueryString()：返回请求参数，例如，"name=Bob&a=1&b=2"；
 * getParameter(name)：返回请求参数，GET请求从URL读取参数，POST请求从Body中读取参数；
 * getContentType()：获取请求Body的类型，例如，"application/x-www-form-urlencoded"；
 * getContextPath()：获取当前Webapp挂载的路径，对于ROOT来说，总是返回空字符串""；
 * getCookies()：返回请求携带的所有Cookie；
 * getHeader(name)：获取指定的Header，对Header名称不区分大小写；
 * getHeaderNames()：返回所有Header名称；
 * getInputStream()：如果该请求带有HTTP Body，该方法将打开一个输入流用于读取Body；
 * getReader()：和getInputStream()类似，但打开的是Reader；
 * getRemoteAddr()：返回客户端的IP地址；
 * getScheme()：返回协议类型，例如，"http"，"https"；
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */
@WebServlet(urlPatterns = "/6b")
public class A_6_Servlet_1 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, web-world!  \n 6b页面 </h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }
}


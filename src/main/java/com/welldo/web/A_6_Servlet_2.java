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
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */
@WebServlet(urlPatterns = "/")
public class A_6_Servlet_2 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // 设置响应类型:
        resp.setContentType("text/html");
        // 如果需要写入中文，需要设置编码。
        resp.setCharacterEncoding("UTF-8");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, web-world!  \n 拦截所有请求 </h1>");
        pw.write("<h1>from-62 </h1>");
        // 最后不要忘记flush强制输出:
        pw.flush();
    }
}


package com.welldo.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * author:welldo
 * date: 2021-09-12 16:17
 */
@WebServlet(urlPatterns = "/93home")
public class A_9_session_cookie_3 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        // 从HttpSession获取当前用户名:
        String user = (String) req.getSession().getAttribute("user");

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.setHeader("X-Powered-By", "JavaEE Servlet");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Welcome, " + (user != null ? user : "访客") + "</h1>");
        if (user == null) {
            // 未登录，显示登录链接:
            pw.write("<p><a href=\"/9signin\">Sign In 去登录页面</a></p>");
        } else {
            // 已登录，显示登出链接:
            pw.write("<p><a href=\"/92signout\">Sign Out</a></p>");
        }

        //获取cookie
        String s = parseLanguageFromCookie(req);
        pw.write("<p>当前您的语言为: "+s+",30s过期</p>");

        pw.write("<h2>from 93</h2>");
        pw.flush();
    }


    //  如果我们要读取Cookie，读取名为lang的Cookie以获取用户设置的语言，
    private String parseLanguageFromCookie(HttpServletRequest req) {
        // 获取请求附带的所有Cookie:
        Cookie[] cookies = req.getCookies();
        // 如果获取到Cookie:
        if (cookies != null) {
            // 循环每个Cookie:
            for (Cookie cookie : cookies) {
                // 如果Cookie名称为lang:
                if (cookie.getName().equals("lang")) {
                    // 返回Cookie的值:
                    return cookie.getValue();
                }
            }
        }
        // 返回默认值:
        return "not set Lang";
    }


}


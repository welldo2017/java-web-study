package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 0.
 * 因为HTTP协议是一个无状态协议，即Web应用程序无法区分收到的两个HTTP请求是否是同一个浏览器发出的。
 * 为了跟踪用户状态，服务器可以向浏览器分配一个唯一ID，并以Cookie的形式发送到浏览器，浏览器在后续访问时总是附带此Cookie，
 * 这样，服务器就可以识别用户身份。
 *
 * 1. Session
 * 基于唯一ID识别用户身份的机制称为Session:
 * 每个用户第一次访问服务器后，会自动获得一个Session ID。如果用户在一段时间内没有访问服务器，那么Session会自动失效，
 * 下次即使带着上次分配的Session ID访问，服务器也会分配新的Session ID。
 *
 * JavaEE的Servlet机制内建了对Session的支持。
 * 我们以登录为例，当一个用户登录成功后，我们就可以把这个用户的名字放入一个HttpSession对象，
 * 以便后续访问其他页面的时候，能直接从HttpSession取出用户名：
 * (code)
 *
 *
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/signout")
public class A_9_session_cookie_2 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 从HttpSession移除用户名:
        HttpSession session = req.getSession();
        session.removeAttribute("user");

        resp.sendRedirect("/");
    }


    
}


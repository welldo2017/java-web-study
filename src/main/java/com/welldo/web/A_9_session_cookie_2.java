package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 用户登出
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/92signout")
public class A_9_session_cookie_2 extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        // 从HttpSession移除用户名:
        HttpSession session = req.getSession();
        session.removeAttribute("user");

        resp.sendRedirect("/93home");
    }


    
}


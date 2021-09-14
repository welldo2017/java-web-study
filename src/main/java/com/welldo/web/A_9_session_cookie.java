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
 * 2. 如果用户已登录，可以通过访问 /signout 登出。登出逻辑就是从HttpSession中移除用户相关信息：
 * {@link A_9_session_cookie_2}
 *
 *
 * 3. 对于Web应用程序来说，我们总是通过HttpSession这个高级接口访问当前Session。
 * 如果要深入理解Session原理，可以认为Web服务器在内存中自动维护了一个ID到HttpSession的映射表，我们可以用下图表示：
 *
 *            ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *
 *            │      ┌───────────────┐                │
 *              ┌───>│ IndexServlet  │<──────────┐
 *            │ │    └───────────────┘           ▼    │
 * ┌───────┐    │    ┌───────────────┐      ┌────────┐
 * │Browser│──┼─┼───>│ SignInServlet │<────>│Sessions││
 * └───────┘    │    └───────────────┘      └────────┘
 *            │ │    ┌───────────────┐           ▲    │
 *              └───>│SignOutServlet │<──────────┘
 *            │      └───────────────┘                │
 *
 *            └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 * 而服务器识别Session的关键就是依靠一个名为JSESSIONID的Cookie。
 * 在Servlet中第一次调用req.getSession()时，Servlet容器自动创建一个Session ID，然后通过一个名为JSESSIONID的Cookie发送给浏览器：
 *
 * 这里要注意的几点是：
 *      JSESSIONID是由Servlet容器自动创建的，目的是维护一个浏览器会话，它和我们的登录逻辑没有关系；
 *      登录和登出的业务逻辑是我们自己根据HttpSession是否存在一个"user"的Key判断的，登出后，Session ID并不会改变；
 *      即使没有登录功能，仍然可以使用HttpSession追踪用户，例如，放入一些用户配置信息等。
 *
 *
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/signin")
public class A_9_session_cookie extends HttpServlet {

    // 模拟一个数据库:
    Map<String, String> map ;
    {
        map = new HashMap<>();
        map.put("bob", "bob123");
        map.put("alice", "alice123");
        map.put("tom", "tomcat");
        map.put("wd", "wd");
    }

    //1. GET请求时显示登录页:
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Sign In</h1>");
        pw.write("<form action=\"/signin\" method=\"post\">");
        pw.write("<p>Username: <input name=\"username\"></p>");
        pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
        //提交按钮,
        pw.write("<p><button type=\"submit\">Sign In</button> </p>");
        //取消按钮
        pw.write("<p><a href=\"/\">Cancel</a></p>");
        pw.write("</form>");
        pw.flush();

    }


    /**
     * 1. POST请求时处理用户登录:
     * 登录成功后, 重定向到主页     * {@link A_9_session_cookie_3}
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("password");

        //从数据库中 get 密码
        String expectedPassword = map.get(name.toLowerCase());
        if (expectedPassword.equals(password)) {
            // 登录成功, 立刻将用户名放入当前HttpSession中：
            //在Servlet中第一次调用req.getSession()时，Servlet容器自动创建一个Session ID，然后通过一个名为JSESSIONID的Cookie发送给浏览器：
            HttpSession session = req.getSession();
            session.setAttribute("user", name);

            resp.sendRedirect("/home");
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);//403 Forbidden：表示服务器因为权限问题拒绝了客户端的请求；
        }
    }


}


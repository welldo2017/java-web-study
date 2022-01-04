package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redirect 重定向.
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/7hi")
public class A_7_Redirect_1 extends HttpServlet {

    //如果收到的路径为/7hi，希望能重定向到/7hello，
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //默认302重定向
        resp.sendRedirect("/7hello");

        //带有参数的302
        // redirect302(req,resp);

        //永久301
        // redirect301(req,resp);

    }

    private void redirect302(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        //默认302重定向
        //如果带有参数，我们需要构造 重定向的路径
        String name = req.getParameter("name");
        String redirectToUrl = "/7hello" + (name == null ? "" : "?name=" + name);
        resp.sendRedirect(redirectToUrl);
    }

    /**
     * 如果要实现301永久重定向，可以这么写.
     * 观察/hi 请求的返回,
     * Status Code: 301  (from disk cache)
     * Location: /hello
     * 表示浏览器会缓存/hi到/hello这个重定向的关联，再次请求/hi时,直接从磁盘拿取/hello,并发送.
     */
    private void redirect301(HttpServletRequest req,HttpServletResponse resp) throws IOException {
        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301
        resp.setHeader("Location", "/7hello");
    }


}


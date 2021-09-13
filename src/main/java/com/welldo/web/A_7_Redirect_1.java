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

@WebServlet(urlPatterns = "/hi")
public class A_7_Redirect_1 extends HttpServlet {

    //如果收到的路径为/hi，希望能重定向到/hello，
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //默认302重定向
        // resp.sendRedirect("/hello");

        /**
         * 如果要实现301永久重定向，可以这么写.
         * 观察/hi 请求的返回,
         * Status Code: 301  (from disk cache)
         * Location: /hello
         * 表示浏览器会缓存/hi到/hello这个重定向的关联，再次请求/hi时,直接从磁盘拿取/hello,并发送.
         */
        resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY); // 301
        resp.setHeader("Location", "/hello");
    }
}


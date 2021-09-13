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
 * Servlet进阶
 * 1. 一个Web App就是由 N个Servlet组成的，分别映射不同的路径。每个Servlet通过注解说明自己能处理的请求。
 *
 * 1.1 浏览器发出的HTTP请求总是由Web Server先接收，然后，根据映射，不同的请求转发到不同的Servlet：
 * 这种根据路径转发的功能我们一般称为Dispatch。
 * 映射到/的IndexServlet比较特殊，它实际上会接收所有未匹配的路径，相当于/*
 *                ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *                │            /hello    ┌───────────────┐│
 *                           ┌──────────>│ HelloServlet  │
 *                │          │           └───────────────┘│
 * ┌───────┐    ┌──────────┐ │ /signin   ┌───────────────┐
 * │Browser│───>│Dispatcher│─┼──────────>│ SignInServlet ││
 * └───────┘    └──────────┘ │           └───────────────┘
 *                │          │ /         ┌───────────────┐│
 *                           └──────────>│ IndexServlet  │
 *                │                      └───────────────┘│
 *                               Web Server
 *                └ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┘
 *
 * 2. HttpServletRequest
 * HttpServletRequest 封装了一个HTTP请求，它 继承了ServletRequest。
 * (设计时，设计者希望Servlet不仅能处理HTTP，也能处理类似SMTP等其他协议，因此，单独抽出了ServletRequest接口，
 *  但实际上除了HTTP外，并没有其他协议会用Servlet处理，所以这是一个过度设计。)
 *
 *  2.1 通过HttpServletRequest提供的接口方法可以拿到HTTP请求的几乎全部信息
 *  2.2 ServletRequest 还有两个方法：setAttribute()和getAttribute()，
 *  可以给当前HttpServletRequest对象附加多个Key-Value，相当于把HttpServletRequest当作一个Map<String, Object>使用。
 *
 *
 * 3. HttpServletResponse
 * HttpServletResponse封装了一个HTTP响应。
 * (看代码)
 *
 * 4. 有了HttpServletRequest和HttpServletResponse这两个高级接口，我们就不需要直接处理HTTP协议。
 *
 * 5. 这3个类,分别处理 /a, /b ,/ 三个请求
 * {@link A_6_Servlet_1}
 * {@link A_6_Servlet_2}
 * 一个Servlet类在服务器中只有一个实例，但对于每个HTTP请求，Web服务器会使用多线程执行请求
 * 因此，一个Servlet的doGet()、doPost()等方法,是多线程执行的。如果Servlet中定义了字段，要注意多线程并发访问的问题：
 *
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

//每个Servlet通过注解说明自己能处理的请求。(早期的Servlet需要在web.xml中配置映射路径，现在只需要通过注解就可以完成映射。)
@WebServlet(urlPatterns = "/a")
public class A_6_Servlet extends HttpServlet {

    //如果Servlet中定义了字段，要注意多线程并发访问的问题：
    private Map<String, String> map = new ConcurrentHashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        /*
        3.1
        由于HTTP响应必须先发送Header，再发送Body，所以，操作HttpServletResponse对象时，必须先调用设置Header的方法，最后调用发送Body的方法。

        常用的设置Header的方法有：
        setStatus(sc)：设置响应代码，默认是200；
        setContentType(type)：设置Body的类型，例如，"text/html"；
        setCharacterEncoding(charset)：设置字符编码，例如，"UTF-8"；
        setHeader(name, value)：设置一个Header的值；
        addHeader(name, value)：给响应添加一个Header，因为HTTP协议允许有多个相同的Header；
        addCookie(cookie)：给响应添加一个Cookie；
         */
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        /**
         *  3.2
         * 写入响应时，需要通过getOutputStream()获取写入流，或者通过getWriter()获取字符流，二者只能获取其中一个。
         *
         * 写入响应前，无需设置setContentLength()，因为底层服务器会根据写入的字节数自动设置，
         *      如果写入的数据量很小，实际上会先写入缓冲区，
         *      如果写入的数据量很大，服务器会自动采用Chunked编码让浏览器能识别数据结束符而不需要设置Content-Length头。
         */
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello, web-world!   a页面 </h1>");

        /*
         * 写入完毕后调用flush()却是必须的，因为大部分Web服务器都基于HTTP/1.1协议，会复用TCP连接。
         * 如果没有调用flush()，将导致缓冲区的内容无法及时发送到客户端。
         *
         * 写入完毕后千万不要调用close()，原因同样是因为会复用TCP连接，如果关闭写入流，将关闭TCP连接，使得Web服务器无法复用此TCP连接。
         */
        pw.flush();
    }
}


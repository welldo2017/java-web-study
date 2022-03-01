package com.welldo.web;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 *
 * 1.
 * 为了把一些公用逻辑从各个Servlet中抽离出来，JavaEE的Servlet规范还提供了一种Filter组件，即过滤器，
 * 它的作用是，在HTTP请求到达Servlet之前，可以被一个或多个Filter预处理，类似打印日志、登录检查等逻辑，完全可以放到Filter中。
 *
 * 2. 没有加filter之前
 *             ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *                /             ┌──────────────┐
 *             │ ┌─────────────>│ IndexServlet │ │ 浏览帖子；
 *               │              └──────────────┘
 *             │ │/signin       ┌──────────────┐ │
 *               ├─────────────>│SignInServlet │   登录；
 *             │ │              └──────────────┘ │
 *               │/signout      ┌──────────────┐
 * ┌───────┐   │ ├─────────────>│SignOutServlet│ │ 退出登录(需要用户登录后)
 * │Browser├─────┤              └──────────────┘
 * └───────┘   │ │/user/profile ┌──────────────┐ │
 *               ├─────────────>│ProfileServlet│   修改用户资料(需要用户登录后)
 *             │ │              └──────────────┘ │
 *               │/user/post    ┌──────────────┐
 *             │ ├─────────────>│ PostServlet  │ │ 发帖子(需要用户登录后才能操)
 *               │              └──────────────┘
 *             │ │/user/reply   ┌──────────────┐ │
 *               └─────────────>│ ReplyServlet │   回复(需要用户登录后才能操)
 *             │                └──────────────┘ │
 *              ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
 *
 * 我们编写一个最简单的EncodingFilter * 强制把输入和输出的编码设置为UTF-8：
 * 加filter之后 流程图如下：
 *             ┌ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ┐
 *                                     /            ┌──────────────┐
 *             │                     ┌─────────────>│ IndexServlet │ │
 *                                   │              └──────────────┘
 *             │                     │/signin       ┌──────────────┐ │
 *                                   ├─────────────>│SignInServlet │
 *             │                     │              └──────────────┘ │
 *                                   │/signout      ┌──────────────┐
 * ┌───────┐   │   ┌──────────────┐  ├─────────────>│SignOutServlet│ │
 * │Browser│──────>│EncodingFilter├──┤              └──────────────┘
 * └───────┘   │   └──────────────┘  │/user/profile ┌──────────────┐ │
 *                                   ├─────────────>│ProfileServlet│
 *             │                     │              └──────────────┘ │
 *                                   │/user/post    ┌──────────────┐
 *             │                     ├─────────────>│ PostServlet  │ │
 *                                   │              └──────────────┘
 *             │                     │/user/reply   ┌──────────────┐ │
 *                                   └─────────────>│ ReplyServlet │
 *             │                                    └──────────────┘ │
 *              ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─
 *
 * 3.
 * 编写Filter时，必须实现Filter接口，在doFilter()方法内部，要继续处理请求，必须调用chain.doFilter()。
 * 最后，用@WebFilter注解标注该Filter需要过滤的URL。这里的/*表示所有路径。
 *
 * 3.5
 * 多个Filter会组成一个链，每个请求都被链上的Filter依次处理：
 *                                         ┌────────┐
 *                                      ┌─>│ServletA│
 *           设置编码       打印日志      │  └────────┘
 *     ┌──────────────┐    ┌─────────┐  │  ┌────────┐
 * ───>│EncodingFilter│───>│LogFilter│──┼─>│ServletB│
 *     └──────────────┘    └─────────┘  │  └────────┘
 *                                      │  ┌────────┐
 *                                      └─>│ServletC│
 *                                         └────────┘
 * Servlet规范并没有对@WebFilter注解标注的Filter规定顺序。
 * 如果一定要给每个Filter指定顺序，就必须在 web.xml文件中对这些Filter再配置一遍。
 *
 *
 * 4.总结。
 * Filter可以对请求进行预处理，因此，我们可以把很多公共预处理逻辑放到Filter中完成。
 *
 *
 * author:welldo
 * date: 2022-03-01 16:33
 */


// @WebFilter(urlPatterns = "/*")              //第一种写法，无法定义顺序
@WebFilter(filterName ="A_12_EncodingFilter")  //第二种写法,需要和 web.xml配合，可以定义顺序,
public class A_12_EncodingFilter implements Filter {

    // 编写一个最简单的EncodingFilter，它强制把输入和输出的编码设置为UTF-8：
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println(" --------- Filter3:encoding  ");
        System.out.println( ((HttpServletRequest) request).getRequestURI());

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }
}




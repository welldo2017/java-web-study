package com.welldo.web;

import javax.servlet.Filter;
import javax.servlet.ServletRequest;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Filter可以对请求进行预处理，因此，我们可以把很多公共预处理逻辑放到Filter中完成。
 *
 * 编写Filter时，必须实现Filter接口，在doFilter()方法内部，要继续处理请求，必须调用chain.doFilter()。
 * 最后，用@WebFilter注解标注该Filter需要过滤的URL。这里的/*表示所有路径。
 *
 *
 * 没有加filter之前
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
 * 加filter之后
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
 * author:welldo
 * date: 2022-01-04 16:33
 */
// @WebFilter(urlPatterns = "/*")
@WebFilter(filterName ="A_12_EncodingFilter")  //这是第二种写法,可以定义顺序,
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




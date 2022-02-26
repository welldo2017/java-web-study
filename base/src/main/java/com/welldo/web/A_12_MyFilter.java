package com.welldo.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 用户将看到一个空白页，因为请求没有继续处理，默认响应是200+空白输出。
 */
@WebFilter(urlPatterns = "/my/*")
public class A_12_MyFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {

        System.out.println("---------Filter4:my");
        //当用户没有登录时，请求到达 AuthFilter 后，不再继续处理，
        //也就是说, 没有调用chain.doFilter(), 直接调用resp.sendRedirect()发送重定向，
        //那么,这个请求,后续的Filter和任何Servlet都没有机会处理该请求了。

    }
}

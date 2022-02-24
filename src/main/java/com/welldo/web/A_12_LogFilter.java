package com.welldo.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * 还可以继续添加其他Filter，例如LogFilter：
 * 多个Filter会组成一个链，每个请求都被链上的Filter依次处理：
 */

// @WebFilter(urlPatterns = "/*")
@WebFilter(filterName ="A_12_LogFilter")  //这是第二种写法,可以定义顺序,
public class A_12_LogFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("--------- Filter2: log "  );
        System.out.println( ((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }
}

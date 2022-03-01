package com.welldo.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


// @WebFilter(urlPatterns = "/*")
@WebFilter(filterName ="A_12_LogFilter")  //第二种写法,需要和 web.xml配合，可以定义顺序,
public class A_12_LogFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("--------- Filter2: log "  );
        System.out.println( ((HttpServletRequest) request).getRequestURI());
        chain.doFilter(request, response);
    }
}

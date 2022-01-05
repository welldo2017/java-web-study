package com.welldo.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 也可以编写只对特定路径进行过滤的Filter，例如AuthFilter：
 *
 * 注意到AuthFilter只过滤以/user/开头的路径，因此：
 *  ==如果一个请求路径类似/user/profile，那么它会被上述3个Filter依次处理；
 *  ==如果一个请求路径类似/test，那么它会被上述2个Filter依次处理（不会被AuthFilter处理）。
 *
 */

// @WebFilter(urlPatterns = "/user/*")  //这是第1种写法,不可以定义顺序,
@WebFilter(filterName ="A_12_AuthFilter")  //这是第二种写法,可以定义顺序,
public class A_12_AuthFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        System.out.println("---------Filter1: authentication");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 未登录，自动跳转到登录页:
        if (req.getSession().getAttribute("user") == null) {
            System.out.println("AuthFilter: not signin!");

            /**
             * 当用户没有登录时，请求到达 AuthFilter 后，不再继续处理，
             * 也就是说, 没有调用chain.doFilter(), 直接调用resp.sendRedirect()发送重定向，
             * 那么,这个请求,后续的Filter和任何Servlet都没有机会处理该请求了。
             * {@link A_12_MyFilter}
             */
            resp.sendRedirect("/9signin");
        } else {
            // 已登录，继续处理:
            chain.doFilter(request, response);
        }
    }
}

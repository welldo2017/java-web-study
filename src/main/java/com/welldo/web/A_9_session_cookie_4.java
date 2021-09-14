package com.welldo.web;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * 0. 无论采用何种方案，使用Session机制，会使得Web Server的集群很难扩展，
 * 因此，Session适用于中小型Web应用程序。
 * 对于大型Web应用程序来说，通常需要避免使用Session机制。
 *
 * Cookie
 * 实际上，Servlet提供的HttpSession, 本质上就是通过一个名为JSESSIONID的Cookie来跟踪用户会话的。
 * 除了这个名称外，其他名称的Cookie我们可以任意使用。
 *
 * 1. 如果我们想要设置一个Cookie，例如，记录用户选择的语言，可以编写一个LanguageServlet：(见代码)
 *
 * 2. 因此，务必注意：浏览器在请求某个URL时，是否携带指定的Cookie，取决于Cookie是否满足以下所有要求：
 * URL前缀是设置Cookie时的Path；
 * Cookie在有效期内；
 * Cookie设置了secure时必须以https访问。
 *
 *
 *
 * author:welldo
 * date: 2021-09-14
 */

@WebServlet(urlPatterns = "/pref")
public class A_9_session_cookie_4 extends HttpServlet {

    private Set<String> LANGUAGES = new HashSet<>();
    {
        LANGUAGES.add("en");
        LANGUAGES.add("zh");
    }


    /**
     * 1. 创建一个新Cookie时，除了指定kv以外，通常需要设置setPath("/")，
     * 浏览器根据此前缀决定是否发送Cookie。
     * 如果一个Cookie调用了setPath("/user/")，那么浏览器只有在请求以/user/开头的路径时才会附加此Cookie。
     * 最后通过resp.addCookie()把它添加到响应。
     *
     * 3. 查看cookie 读取 {@link A_9_session_cookie_3}
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String lang = req.getParameter("lang");
        if (LANGUAGES.contains(lang)) {
            // 创建一个新的Cookie:
            Cookie cookie = new Cookie("lang", lang);
            // 该Cookie生效的路径范围:
            cookie.setPath("/home");
            // 该Cookie有效期:(单位:s)
            cookie.setMaxAge(30); // 30s

            //如果访问的是https网页，还需要调用setSecure(true)，否则浏览器不会发送该Cookie。
            // cookie.setSecure(true);

            // 将该Cookie添加到响应:
            //然后可以在请求的 response 中看到如下信息:
            //lang=en; Max-Age=86400; Expires=Wed, 15-Sep-2021 09:36:45 GMT; Path=/
            resp.addCookie(cookie);
        }
        resp.sendRedirect("/home");
    }


}


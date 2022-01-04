package com.welldo.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 0.
 * 因为HTTP协议是一个无状态协议，即Web应用程序无法区分收到的两个HTTP请求是否是同一个浏览器发出的。
 * 为了跟踪用户状态，服务器可以向浏览器分配一个唯一ID，并以Cookie的形式发送到浏览器，浏览器在后续访问时总是附带此Cookie，
 * 这样，服务器就可以识别用户身份。
 *
 * 1. Session
 * 基于唯一ID识别用户身份的机制称为Session:
 * 每个用户第一次访问服务器后，会自动获得一个Session ID。如果用户在一段时间内没有访问服务器，那么Session会自动失效，
 * 如果带着失效的Session ID访问，服务器会分配新的.
 *
 * JavaEE的Servlet机制内建了对Session的支持。
 * 我们以登录为例，当一个用户登录成功后，我们就可以把这个用户的名字放入一个HttpSession对象，
 * 以便后续访问其他页面的时候，能直接从HttpSession取出用户名：
 * (见代码)
 *
 * 2. 如果用户已登录，可以通过访问 /signout 登出。登出逻辑就是从HttpSession中移除用户相关信息：
 * {@link A_9_session_cookie_2}
 *
 *
 * 3. 对于Web应用程序来说，我们总是通过HttpSession这个高级接口访问当前Session。
 *
 * 服务器识别Session的关键，依靠一个名为JSESSIONID的Cookie。
 * 在Servlet中第一次调用req.getSession()时，Servlet容器自动创建一个Session ID，然后通过一个名为JSESSIONID的Cookie发送给浏览器
 *
 * 这里要注意的几点是：todo 没有理解
 *      JSESSIONID是由Servlet容器自动创建的，目的是维护一个浏览器会话，它和我们的登录逻辑没有关系；
 *      登录和登出的业务逻辑是我们自己根据HttpSession是否存在一个"user"的Key判断的，登出后，Session ID并不会改变；
 *      即使没有登录功能，仍然可以使用HttpSession追踪用户，例如，放入一些用户配置信息等。
 *
 * 3.1 使用Session时，由于服务器把所有用户的Session都存储在内存中，
 * 如果遇到内存不足的情况，就需要把部分不活动的Session序列化到磁盘上，这会大大降低服务器的运行效率，
 * 因此，放入Session的对象,最好比较小，通常我们放入一个简单的User对象就足够了：
 * public class User {
 *     public long id; // 唯一标识
 *     public String email;
 *     public String name;
 * }
 *
 * 3.2 在使用多台服务器构成集群时，使用Session会遇到一些额外的问题。
 * 通常，多台服务器集群使用反向代理作为网站入口：
 *
 *                                      ┌────────────┐
 *                                 ┌───>│Web Server 1│
 *                                 │    └────────────┘
 * ┌───────┐     ┌─────────────┐   │    ┌────────────┐
 * │Browser│────>│Reverse Proxy│───┼───>│Web Server 2│
 * └───────┘     └─────────────┘   │    └────────────┘
 *                                 │    ┌────────────┐
 *                                 └───>│Web Server 3│
 *                                      └────────────┘
 * 如果多台Web Server采用无状态集群，那么反向代理总是以"轮询方式",将请求依次转发给每台Web Server，
 * 这会造成一个用户在Web Server 1存储的Session信息，在Web Server 2和3上并不存在，
 * 即从Web Server 1登录后，如果后续请求被转发到Web Server 2或3，那么用户看到的仍然是未登录状态。
 * 要解决这个问题，
 * 方案1: 在所有Web Server之间进行Session复制，会有严重网络io，并且，每个Web Server的内存均存储所有用户的Session，浪费内存。
 * 方案2: 采用粘滞会话（Sticky Session）机制，
 * 即反向代理在转发请求的时候，总是根据JSESSIONID的值判断，相同的JSESSIONID总是转发到固定的Web Server，但这需要反向代理的支持。
 *
 *
 * 无论采用何种方案，使用Session机制，会使得Web Server的集群很难扩展，
 * 因此，Session适用于中小型Web应用程序。
 * 对于大型Web应用程序来说，通常需要避免使用Session机制。{@link A_10_cookie}
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

@WebServlet(urlPatterns = "/9signin")
public class A_9_session_cookie extends HttpServlet {

    // 模拟一个数据库:
    private Map<String, String> map ;
    {
        map = new HashMap<>();
        //name不区分大小写
        map.put("bob", "bob123");
        map.put("alice", "alice123");
        map.put("tom", "tom123");
        map.put("wd", "wd123");
    }

    //1. 访问登录页, GET请求
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {

        //先判断是否已经登录(从HttpSession获取当前用户名), 如果已经登录, 直接去主页
        String user = (String) req.getSession().getAttribute("user");
        if (user != null) {
            resp.sendRedirect("/93home");
            return;
        }

        //未登录,走下面逻辑
        resp.setContentType("text/html");
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Sign In</h1>");
        pw.write("<form action=\"/9signin\" method=\"post\">");//POST请求/signin
        pw.write("<p>Username: <input name=\"username\"></p>");
        pw.write("<p>Password: <input name=\"password\" type=\"password\"></p>");
        //提交按钮,
        pw.write("<p><button type=\"submit\">Sign In</button> </p>");
        //取消按钮
        pw.write("<p><a href=\"/\">Cancel</a></p>");//跳转到 / 页面
        pw.write("</form>");
        pw.write("<h3>from 9</h3>");
        pw.flush();

    }


    /**
     * 1. 处理用户登录, POST请求
     * 登录成功后, 重定向到主页  {@link A_9_session_cookie_3}
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        String name = req.getParameter("username");
        String password = req.getParameter("password");

        //从数据库中 get 密码
        String expectedPassword = map.get(name.toLowerCase());

        if (expectedPassword.equals(password)) {
            // 登录成功, 立刻将用户名放入当前HttpSession中：
            //在Servlet中第一次调用req.getSession()时，Servlet容器自动创建一个Session ID，然后通过一个名为JSESSIONID的Cookie发送给浏览器：
            HttpSession session = req.getSession();
            session.setAttribute("user", name);

            //登录成功后, 重定向到主页  {@link A_9_session_cookie_3}
            resp.sendRedirect("/93home");
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);//403 Forbidden：表示服务器因为权限问题拒绝了客户端的请求；
        }
    }


}


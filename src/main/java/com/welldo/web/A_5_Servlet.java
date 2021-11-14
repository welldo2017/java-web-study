package com.welldo.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet开发
 *
 * 0. 在上一节中，我们看到，一个完整的Web应用程序的开发流程如下：
 *      编写Servlet；
 *      打包为war文件；
 *      复制到Tomcat的webapps目录下；
 *      启动Tomcat。
 *  过程繁琐.
 *
 * 1. 我们需要一种简单可靠，能直接在IDE中启动并调试webapp的方法。
 * 因为Tomcat实际上也是一个Java程序，我们看看Tomcat的启动流程：
 *      启动JVM并执行Tomcat的main()方法；
 *      加载war并初始化Servlet；
 *      正常服务。
 * 所以，我们完全可以把Tomcat的jar包全部引入进来，然后自己编写一个main()方法，先启动Tomcat，然后让它加载我们的webapp。
 *
 * 2.
 * 修改pom文件。
 * 编写Servlet
 *
 * 3. 增加main（）方法 {@link A_5_Servlet_Main}
 * 直接运行main()方法，即可启动嵌入式Tomcat服务器
 *
 * 3.5 好处：
 * 通过main()方法启动Tomcat服务器并加载我们自己的webapp有如下好处：
 *      启动简单，无需下载Tomcat或安装任何IDE插件；
 *      调试方便，可在IDE中使用断点调试；
 *      使用Maven创建war包后，也可以正常部署到独立的Tomcat服务器中。（注意，这种情况下，maven不用依赖javax.servlet-api，依赖tomcat即可）
 *
 * 3.6 SpringBoot也支持在main()方法中一行代码直接启动Tomcat，并且还能方便地更换成Jetty等其他服务器。它的启动方式和这里是基本一样的
 *
 * 4. mian（）方法有干扰吗？没有。
 * 因为： 容器直接读web.xml找servlet，容器不会管哪个class里有main方法
 * 事实上你引用的很多第三方jar包里面也有main方法，那就是一个普通方法，不是说定义了一个main方法它就肯定是入口。
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */
@WebServlet(urlPatterns = "/5")
public class A_5_Servlet extends HttpServlet {
    //将第4节的代码复制过来。
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter pw = resp.getWriter();

        if (name == null || name.length() == 0) {
            pw.write("<h1>Hello, web-world!  by mytomcat 张三5 </h1>");
        } else {
            pw.write("<h1>Hello, web-world!  by mytomcat " + name + " </h1>");
        }
        pw.flush();
    }
}


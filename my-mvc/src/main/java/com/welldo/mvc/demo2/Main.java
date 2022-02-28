package com.welldo.mvc.demo2;


import com.welldo.mvc.demo2.framework.DispatcherServlet;
import com.welldo.mvc.demo2.framework.ModelAndView;
import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * 我们来手写一个 mvc 框架
 * 1. 整个工程的结构：
 * 其中，framework包是MVC的框架
 * 硬性规定模板必须放在webapp/WEB-INF/templates目录下，静态文件必须放在webapp/static目录下
 *
 * web-mvc
 * ├── pom.xml
 * └── src
 *     └── main
 *         ├── java
 *         │     └── com.welldo.mvc.demo2
 *         │               ├── Main.java
 *         │               ├── bean
 *         │               │   └── User.java
 *         │               ├── controller
 *         │               │   └── UserController.java
 *         │               └── framework
 *         │                   ├── DispatcherServlet.java
 *         │                   └── ViewEngine.java
 *         └── webapp
 *             ├── WEB-INF
 *             │   ├── templates
 *             │   │   ├── hello.html
 *             │   └── web.xml
 *             └── static
 *                 ├── css
 *                 │   └── bootstrap.css
 *                 └── js
 *                     ├── bootstrap.js
 *                     └── jquery.js
 *
 * 2.定义 modelAndView {@link ModelAndView}
 *
 * 3.定义 请求的分发者 dispatcherServlet {@link DispatcherServlet}
 *
 * 总结：
 * 一个MVC框架，是基于Servlet基础，抽象出更高级的接口，使得上层基于MVC框架的开发可以不涉及Servlet相关的HttpServletRequest等接口，
 * 处理多个请求更加灵活，并且可以使用任意模板引擎，不必使用JSP。
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        //启动嵌入式 tomcat
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8081));
        tomcat.getConnector();
        Context ctx = tomcat.addWebapp("", new File("my-mvc/src/main/webapp").getAbsolutePath());
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        new File("my-mvc/target/classes").getAbsolutePath(),
                        "/"
                )
        );
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}

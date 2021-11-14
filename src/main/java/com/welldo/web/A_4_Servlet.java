package com.welldo.web;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Servlet入门
 *
 * 0. 在上一节中，我们看到，编写HTTP服务器其实是非常简单的，只需要先编写多线程的socket(TCP服务)，然后在连接中读取HTTP请求，发送HTTP响应即可。
 * 但是，要编写一个完善的HTTP服务器，则需要考虑很多。
 * 这些基础工作需要耗费大量的时间，如果我们只需要输出一个简单的HTML页面，就不得不编写上千行底层代码，那就根本无法做到高效而可靠地开发。
 *
 * 0.1
 * 因此，在JavaEE平台上，处理socket连接,TCP连接，解析HTTP协议,这些底层工作统统扔给现成的Web服务器去做，
 * 我们只需要编写自己的应用程序, 并让它跑在Web服务器上即可.
 * 为了实现这一目的，JavaEE提供了Servlet API，我们使用Servlet API编写自己的Servlet来处理HTTP请求，
 * Web服务器实现Servlet API接口，实现底层功能：
 *
 *                  ┌───────────┐
 *                  │My Servlet │   程序员编写
 *                  ├───────────┤
 *                  │Servlet API│   javaEE提供
 * ┌───────┐  HTTP  ├───────────┤
 * │Browser│<──────>│Web Server │现成web服务器提供(处理socket,TCP连接，解析HTTP协议等, 也就是{@link A_3_HttpServer} 中代码要做的工作)
 * └───────┘        └───────────┘
 * 看代码.
 *
 *
 * 3.如何启动？？？
 * 普通的Java程序是通过启动JVM，然后通过java -jar 执行main()方法开始运行。
 * 但是Web应用程序有所不同，我们无法直接运行war文件，必须先启动Web服务器，
 * 再由Web服务器加载我们编写的HelloServlet，这样就可以让HelloServlet处理浏览器发送的请求。
 *
 *
 * 3.1 支持Servlet API的Web服务器。常用的服务器有：
 *      Tomcat：由Apache开发的开源免费服务器；
 *      等....
 *      收费,商用: Oracle的WebLogic，IBM的WebSphere。
 * 无论使用哪个服务器，只要它支持Servlet API 4.0（因为我们引入的Servlet版本是4.0），我们的war包都可以在上面运行。
 *
 * 3.2
 * 下载Tomcat服务器，解压后，把hello.war复制到Tomcat的webapps目录下，
 * 然后切换到bin目录，执行startup.sh或startup.bat启动Tomcat服务器：
 * 在浏览器输入  http://localhost:8080/hello/
 * 即可看到HelloServlet的输出
 *
 * 3.3 为啥路径是/hello/而不是/？
 * 因为一个Web服务器允许同时运行多个Web App， * 而我们的Web App叫hello，
 * 因此，第一级目录/hello表示Web App的名字，后面的/才是我们在HelloServlet中映射的路径。
 *
 * 4.
 * 启动Tomcat服务器，实际上是启动Java虚拟机，执行Tomcat的main()方法，然后由Tomcat负责加载我们的.war文件，
 * 并由tomcat创建war包中的Servlet 实例，最后以多线程的模式来处理HTTP请求。
 * 如果Tomcat服务器收到请求，就转发到HelloServlet并传入HttpServletRequest和HttpServletResponse两个对象。
 *
 * 因为我们编写的Servlet并不是直接运行，而是由Web服务器加载后创建实例运行，所以，类似Tomcat这样的Web服务器也称为Servlet容器。
 *
 * 5. 在Servlet容器中运行的Servlet具有如下特点：
 *      无法在代码中直接通过new创建Servlet实例，必须由Servlet容器自动创建Servlet实例；
 *      Servlet容器只会给每个Servlet类创建唯一实例；
 *      Servlet容器会使用多线程执行doGet()或doPost()方法。
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */

//1. 一个Servlet总是继承自 HttpServlet，然后覆写doGet()或doPost()方法。


//每个Servlet通过注解说明自己能处理的请求。
// (早期的Servlet需要在web.xml中配置映射路径，现在只需要通过注解就可以完成映射。)
// WebServlet注解表示这是一个Servlet，并映射到地址/:
@WebServlet(urlPatterns = "/4")
public class A_4_Servlet extends HttpServlet {

    /**
     * doGet()方法传入了 HttpServletRequest 和 HttpServletResponse 两个对象，分别代表HTTP请求和响应。
     * 我们使用Servlet API时，并不直接与底层TCP交互，也不需要解析HTTP协议，
     * 因为HttpServletRequest和HttpServletResponse就已经封装好了请求和响应。
     * 以发送响应为例，我们只需要设置正确的响应类型，然后获取PrintWriter，写入响应即可。
     *
     * 1.1 Servlet API是谁提供？
     * Servlet API是一个jar包，我们需要通过Maven来引入它，才能正常编译。
     * 详情见pom.xml文件
     *  <groupId>javax.servlet</groupId>
     *  <artifactId>javax.servlet-api</artifactId>
     *  <version>4.0.0</version>
     *
     * 2. 我们还需要在 src/main/webapp/WEB-INF 目录下创建一个web.xml描述文件
     * 文件内容固定(详见文件)
     * 执行maven打包操作
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //获取请求参数
        String name = req.getParameter("name");

        // 设置响应类型:
        resp.setContentType("text/html");
        // 如果需要写入中文，需要设置编码。
        resp.setCharacterEncoding("UTF-8");
        // 获取输出流:
        PrintWriter pw = resp.getWriter();

        // 写入响应,分别访问
        // http://localhost:8080/?name=Bob
        // http://localhost:8080
        if (name == null || name.length()== 0) {
            pw.write("<h1>Hello, web-world!  by 张三4 </h1>");
        }else {
            pw.write("<h1>Hello, web-world!  by "+ name+" </h1>");
        }

        // 最后不要忘记flush强制输出:
        pw.flush();
    }
}


package com.welldo.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 0.除了Servlet和Filter外，JavaEE的Servlet规范还提供了第三种组件：Listener。
 *
 * 1. Listener,监听器，
 * 有好几种Listener，其中最常用的是ServletContextListener，看代码
 *
 * 2. 除了ServletContextListener外，还有几种Listener：
 *  =HttpSessionListener：监听HttpSession的创建和销毁事件；
 *  =ServletRequestListener：监听ServletRequest请求的创建和销毁事件；
 *  =ServletRequestAttributeListener：监听ServletRequest请求的属性变化事件（即调用ServletRequest.setAttribute()方法）；
 *  =ServletContextAttributeListener：监听ServletContext的属性变化事件（即调用 ServletContext.setAttribute()方法）；
 *
 * author:welldo
 * date: 2022-03-04 21:42
 */

@WebListener
public class A_13_Listener implements ServletContextListener {

    /**
     * 在此方法中,做一些全局的初始化工作,例如打开数据库连接池等:
     * 因为 Web服务器保证在 contextInitialized() 执行后，才会接受用户的HTTP请求。
     *
     * 3.一个Web服务器可以运行一个或多个WebApp，
     * 对于每个WebApp，Web服务器都会为其创建一个全局唯一的 ServletContext 实例，
     * 在 Listener 里编写的 这两个回调方法,实际上对应的就是 ServletContext 实例的创建和销毁：
     */
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("----  ");
        System.out.println("---- WebApp initialized.");
        System.out.println("---- ");

        /*
         * 3.
         * ServletRequest、HttpSession等很多对象也提供 getServletContext() 方法获取到同一个ServletContext实例。
         * ServletContext实例最大的作用就是设置和共享全局信息。
         * 参考
         * https://blog.csdn.net/qq_36371449/article/details/80314024
         */
        System.out.println(sce.getServletContext());
    }

    // 在此清理WebApp,例如关闭数据库连接池等:
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("---- ");
        System.out.println("---- WebApp destroyed.");
        System.out.println("---- ");
    }
}

package com.welldo.web;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

/**
 * 如果报错
 * Caused by: java.lang.ClassNotFoundException: org.apache.catalina.WebResourceRoot
 *
 * 那是idea的问题，如果你把provided改成compile，生成的war包会很大，因为把tomcat打包进去了
 *
 * 解决方案
 * 打开idea的Run/Debug Configurations:
 * 选择Application - Main
 * 右侧Configuration：Use classpath of module
 * 钩上☑︎Include dependencies with "Provided" scope
 */
 public class A_5_Servlet_Main {

    // 启动Tomcat: 直接运行这个main()方法，即可启动嵌入式Tomcat服务器
    //访问时,和 上一节访问 http://localhost:8080/hello/ 不一样, 这里直接访问 http://localhost:8080/ 即可.
    public static void main(String[] args) throws LifecycleException {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.getInteger("port", 8080));
        tomcat.getConnector();
        // 通过这个方法，创建webapp，Tomcat会加载当前工程作为根webapp
        Context ctx = tomcat.addWebapp("", new File("base/src/main/webapp").getAbsolutePath());

        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(
                new DirResourceSet(
                        resources,
                        "/WEB-INF/classes",
                        new File("base/target/classes").getAbsolutePath(),
                        "/")
        );
        ctx.setResources(resources);
        tomcat.start();
        tomcat.getServer().await();
    }
}


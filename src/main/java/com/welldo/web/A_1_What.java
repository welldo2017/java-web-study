package com.welldo.web;

/**
 * 0.
 *  教程地址： https://www.liaoxuefeng.com/
 *
 * 1. JavaSE: 所有基于标准JDK的开发都是 JavaSE，即Standard Edition。(标准版)
 * 2. JavaEE: JavaEE是 Enterprise Edition的缩写，即Java企业平台。
 *  它完全基于JavaSE，只是多了一大堆服务器相关的库以及API接口。
 *  所有的JavaEE程序，仍然是运行在标准的JavaSE的虚拟机上的。
 *
 * 3.JavaEE并不是一个软件产品，它更多的是一种软件架构和设计思想。
 * 我们可以把JavaEE看作是在JavaSE的基础上，开发的一系列基于服务器的组件、API标准和通用架构。
 *
 * 4. JavaEE最核心的组件就是基于 Servlet 标准的Web服务器，开发者编写的应用程序是基于Servlet API并运行在Web服务器内部的：
 * ┌─────────────┐
 * │┌───────────┐│
 * ││ User App  ││
 * │├───────────┤│
 * ││Servlet API││
 * │└───────────┘│
 * │ Web Server  │
 * ├─────────────┤
 * │   JavaSE    │
 * └─────────────┘
 * 此外，JavaEE还有一系列技术标准：
 * EJB：Enterprise JavaBean，企业级JavaBean，现在基本被轻量级框架如Spring所取代；
 * JAAS：...
 * JCA：...
 * JMS：Java Message Service，用于消息服务；
 * JTA：Java Transaction API，用于分布式事务；
 * JAX-WS：...
 * ...
 *
 * 5. 目前流行的基于Spring的轻量级JavaEE开发架构，使用最广泛的是Servlet和JMS，以及一系列开源组件。
 *
 * author:welldo
 * date: 2021-09-12 16:17
 */
public class A_1_What {
}

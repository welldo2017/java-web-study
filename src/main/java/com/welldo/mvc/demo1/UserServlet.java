package com.welldo.mvc.demo1;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * MVC开发
 *
 * 0. 我们把UserServlet看作业务逻辑处理， * 把User看作模型， * 把user.jsp看作渲染，
 * 这种设计模式通常被称为MVC：Model-View-Controller，
 * 即UserServlet作为控制器（Controller），User作为模型（Model），user.jsp作为视图（View），
 * 整个MVC架构如下：
 *
 *                    ┌───────────────────────┐
 *              ┌────>│Controller: UserServlet│
 *              │     └───────────────────────┘
 *              │                 │
 * ┌───────┐    │           ┌─────┴─────┐
 * │Browser│────┘           │Model: User│
 * │       │<───┐           └─────┬─────┘
 * └───────┘    │                 │
 *              │                 ▼
 *              │     ┌───────────────────────┐
 *              └─────│    View: user.jsp     │
 *                    └───────────────────────┘
 *
 * 1. 使用MVC模式的好处是，Controller专注于业务处理，它的处理结果就是Model。
 * Model可以是一个JavaBean，也可以是一个包含多个对象的Map，
 * Controller只负责把Model传递给View，
 * View只负责把Model给“渲染”出来，
 * 这样，三者职责明确，且开发更简单，因为开发Controller时无需关注页面，开发View时无需关心如何创建Model。
 *
 * MVC模式广泛地应用在Web页面和传统的桌面程序中，
 * 我们在这里通过Servlet和JSP实现了一个简单的MVC模型，但它还不够简洁和灵活
 *
 *
 * author:welldo
 * date: 2021-09-14 18:15
 */
@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 假装从数据库读取:
        School school = new School("No.1 Middle School", "101 South Street");
        User user = new User(123, "Bob", school);
        // 放入Request中:
        req.setAttribute("user", user);
        // forward给user.jsp:
        req.getRequestDispatcher("/WEB-INF/user.jsp").forward(req, resp);
    }
}
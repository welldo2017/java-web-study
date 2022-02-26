package com.welldo.mvc.demo2.framework;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//关于抽象的知识   http://c.biancheng.net/view/6511.html
abstract class AbstractDispatcher {

    public abstract ModelAndView invoke(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ReflectiveOperationException;

}

package com.welldo.mvc.demo2.framework;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PostDispatcher extends AbstractDispatcher{

    final Object instance; // Controller实例
    final Method method; // Controller方法
    final Class<?>[] parameterClasses; // 方法参数类型
    final ObjectMapper objectMapper; // JSON映射


    //构造器
    public PostDispatcher(Object instance, Method method, Class<?>[] parameterClasses, ObjectMapper objectMapper) {
        super();

        this.instance = instance;
        this.method = method;
        this.parameterClasses = parameterClasses;
        this.objectMapper = objectMapper;
    }


    /**
     * 和GET请求不同，POST请求严格地来说不能有URL参数，
     * 所有数据都应当从Post Body中读取。
     * 这里我们为了简化处理，只支持JSON格式的POST请求，这样，把Post数据转化为JavaBean就非常容易。
     */
    @Override
    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response)
            throws IOException, InvocationTargetException, IllegalAccessException {

        Object[] arguments = new Object[parameterClasses.length];

        for (int i = 0; i < parameterClasses.length; i++) {
            Class<?> parameterClass = parameterClasses[i];
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;
            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = response;
            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request.getSession();
            } else {

                //获取请求体,请参考
                // https://blog.csdn.net/qq_36719449/article/details/82820760
                BufferedReader reader = request.getReader();

                // 读取JSON并解析为JavaBean:
                arguments[i] = this.objectMapper.readValue(reader, parameterClass);
            }
        }
        return (ModelAndView) this.method.invoke(instance, arguments);
    }
}

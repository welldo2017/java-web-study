package com.welldo.mvc.demo2.framework;


import com.welldo.mvc.demo2.reference.Reference1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//处理一个get 请求,是通过这个对象完成的

//反射的相关知识,请参考:
//https://github.com/welldo2017/java-base-study/blob/5b8c9e68b1df5e82faa1a4e1a1cd3da55ee3c939/src/main/java/com/welldo/zero/a7_reflection/What1.java
public class GetDispatcher  extends AbstractDispatcher{

    final Object instance;              //controller的实例
    final Method method;                //controller的某个方法,假定方法名为 x

    final String[] parameterNames;      // x方法的参数的名字 们
    final Class<?>[] parameterClasses;  // x方法的参数的类型 们


    //构造器
    public GetDispatcher(Object instance, Method method, String[] parameterNames, Class<?>[] parameterClasses) {
        super();

        this.instance = instance;
        this.method = method;
        this.parameterNames = parameterNames;
        this.parameterClasses = parameterClasses;
    }


    //处理请求
    //构造某个方法需要的所有参数列表，使用反射调用该方法后返回结果。
    @Override
    public ModelAndView invoke(HttpServletRequest request, HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {

        //创建一个参数列表,可以容纳多个类型的参数
        Object[] arguments = new Object[parameterClasses.length];

        for (int i = 0; i < parameterClasses.length; i++) {
            String parameterName = parameterNames[i];       //参数名
            Class<?> parameterClass = parameterClasses[i];  //参数类型

            //根据参数类型,给 arguments 数组 赋值;
            if (parameterClass == HttpServletRequest.class) {
                arguments[i] = request;

            } else if (parameterClass == HttpServletResponse.class) {
                arguments[i] = request;

            } else if (parameterClass == HttpSession.class) {
                arguments[i] = request;

            } else if (parameterClass == int.class) {
                arguments[i] = Integer.valueOf(getOrDefault(request, parameterName, "0"));

            } else if (parameterClass == long.class) {
                arguments[i] = Long.valueOf(getOrDefault(request, parameterName, "0"));

            } else if (parameterClass == boolean.class) {
                arguments[i] = Boolean.valueOf(getOrDefault(request, parameterName, "false"));

            } else if (parameterClass == String.class) {
                arguments[i] =  getOrDefault(request, parameterName, "");

            }else {
                throw  new RuntimeException("不能处理的类型:" +parameterClass);
            }

        }

        /**
         * 通过反射访问方法,请参考
         * https://github.com/welldo2017/java-base-study/blob/5b8c9e68b1df5e82faa1a4e1a1cd3da55ee3c939/src/main/java/com/welldo/zero/a7_reflection/AccessMethod4.java
         * 或者 {@link Reference1}
         */
        return (ModelAndView) this.method.invoke(this.instance, arguments);
    }



    //get 请求,从请求路径上获取参数
    //获取默认值
    private String getOrDefault(HttpServletRequest request, String name, String defaultValue) {
        String s = request.getParameter(name);
        return s == null ? defaultValue : s;
    }


}

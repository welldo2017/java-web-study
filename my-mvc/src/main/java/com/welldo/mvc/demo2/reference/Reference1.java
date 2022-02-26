package com.welldo.mvc.demo2.reference;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

//参考1.
public class Reference1 {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //利用反射调用方法
        String s = "Hello world";
        String world = s.substring(6); // "world"

        // 获取String substring(int)方法，参数为int:
        Method m1 = String.class.getMethod("substring", int.class);
        m1.setAccessible(true);//设置访问权限

        // 在s对象上,用该反射方法,获取结果:
        String r = (String) m1.invoke(s, 6);
        System.out.println("调用方法: " + r);

        //调用方法
        // 获取String substring(int)重载方法，参数为int,int
        Method m2 = String.class.getMethod("substring", int.class, int.class);
        m2.setAccessible(true);
        String r2 = (String) m2.invoke(s, 0, 5);
        System.out.println("调用重载方法: " + r2);
    }


}

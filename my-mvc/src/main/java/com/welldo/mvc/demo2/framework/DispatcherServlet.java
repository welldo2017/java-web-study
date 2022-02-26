package com.welldo.mvc.demo2.framework;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.welldo.mvc.demo2.controller.IndexController;
import com.welldo.mvc.demo2.controller.UserController;
import com.welldo.mvc.demo2.framework.annotation.GetMapping;
import com.welldo.mvc.demo2.framework.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;


/**
 * 3.
 * 在MVC框架中,创建一个接收所有请求的Servlet，(也就是映射到 / ), 通常我们把它命名为DispatcherServlet
 * 然后，根据 Controller的方法定义的@Get或@Post的 Path,决定调用哪个方法，
 * 最后，获得方法返回的ModelAndView后，渲染模板，写入HttpServletResponse，
 * 即完成了整个MVC的处理。
 *
 * 这个MVC的架构如下：
 *
 *    HTTP Request    ┌─────────────────┐
 * ──────────────────>│DispatcherServlet│               1.拦截所有的请求
 *                    └─────────────────┘
 *                             │                        2.根据paht,决定调用哪个方法
 *                ┌────────────┼────────────┐
 *                ▼            ▼            ▼
 *          ┌───────────┐┌───────────┐┌───────────┐
 *          │Controller1││Controller2││Controller3│     3.调用某个方法
 *          └───────────┘└───────────┘└───────────┘
 *                │            │            │
 *                └────────────┼────────────┘
 *                             ▼                        4.获得方法返回的ModelAndView
 *    HTTP Response ┌────────────────────┐
 * <────────────────│render(ModelAndView)│              5.渲染模板
 *                  └────────────────────┘
 *
 *
 * 4. 处理一个GET请求是通过 GetDispatcher 对象完成的  {@link com.welldo.mvc.demo2.framework.GetDispatcher}
 * 5. 处理一个post请求是通过 PostDispatcher 对象完成的  {@link com.welldo.mvc.demo2.framework.PostDispatcher}
 *
 * 6.实现整个DispatcherServlet的处理流程，以 doGet()为例：见代码
 *
 * 7.在 init()方法中初始化所有Get和Post的映射，以及用于渲染的模板引擎
 *
 *
 */

@WebServlet(urlPatterns = "/")  //urlPatterns 不是精准定位一个,而是按照规则匹配N个
public class DispatcherServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ViewEngine viewEngine;

    // "请求路径" 与某个 xxxDispatcher 的映射：
    private Map<String ,GetDispatcher> getMappings = new HashMap<>();//string是请求路径, xxDispatcher是处理这个请求的类
    private Map<String ,PostDispatcher> postMappings = new HashMap<>();

    /*
    将所有的controller,放到这个list中.
    TODO: 优化点,可指定package并自动扫描:
    如何扫描所有Controller以获取所有标记有@GetMapping和@PostMapping的方法？当然是使用反射.
     */
    private List<Class<?>> controllers = new ArrayList<>();
    {
        controllers.add(IndexController.class);
        controllers.add(UserController.class);
    }

    //列出get 请求支持的参数类型
    private static final Set<Class<?>> supportedGetParameterTypes = new HashSet<>();
    {
        supportedGetParameterTypes.add(int.class);
        supportedGetParameterTypes.add(long.class);
        supportedGetParameterTypes.add(boolean.class);
        supportedGetParameterTypes.add(String.class);

        supportedGetParameterTypes.add(HttpServletRequest.class);
        supportedGetParameterTypes.add(HttpServletResponse.class);
        supportedGetParameterTypes.add(HttpSession.class);
    }

    //列出post 请求支持的参数类型
    private static final Set<Class<?>> supportedPostParameterTypes = new HashSet<>();
    {
        supportedPostParameterTypes.add(HttpServletRequest.class);
        supportedPostParameterTypes.add(HttpServletResponse.class);
        supportedPostParameterTypes.add(HttpSession.class);
    }



    //入口方法
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request,response,this.getMappings);
    }

    //入口方法
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        process(request,response,this.postMappings);
    }

    private void process(HttpServletRequest request, HttpServletResponse response,
                         Map<String ,? extends AbstractDispatcher> dispatcherMap)
            throws IOException, ServletException {

        //统一设置响应
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        //http://localhost:8080/blue/test
        // request.getRequestURI()得到的就是  /blue/test
        // request.getContextPath()得到的就是  /blue
        String path = request.getRequestURI().substring(request.getContextPath().length()); //得到 test

        // 根据路径查找GetDispatcher:
        AbstractDispatcher dispatcher = dispatcherMap.get(path);
        if (dispatcher == null) {
            response.sendError(444);//为了展示效果,不设置成404,专门设置成409
            return;
        }
        ModelAndView mv = null;
        try {
            mv = dispatcher.invoke(request, response);//核心方法
        } catch (ReflectiveOperationException e) {
            throw new ServletException();
        }

        // 允许返回null:
        if (mv == null) {
            return;
        }

        // 允许返回`redirect:`开头的view表示重定向:
        String redirect = "redirect:";
        if (mv.view.startsWith(redirect)) {
            response.sendRedirect(mv.view.substring(redirect.length()));
            return;
        }
        //将模板引擎渲染的内容,写入响应.
        PrintWriter pw = response.getWriter();
        this.viewEngine.render(mv, pw);
        pw.flush();

    }


    /**
     * 7.
     * 当Servlet容器创建当前Servlet实例后，首先调用init()方法,此方法继承自 {@link GenericServlet} 类
     */
    @Override
    public void init() throws ServletException {
        logger.info("init {}...", getClass().getSimpleName());

        ObjectMapper objectMapper = new ObjectMapper();//解析json
        //反序列化的时候如果多了其他属性,不抛出异常
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 依次处理每个Controller,最终目的是将 路径与 dispatcher 组成kv,放到map中.
        for (Class<?> controllerClass : controllers) {
            try {
                Object controllerInstance = controllerClass.getConstructor().newInstance();//创建一个实例

                // 依次处理每个Method:
                for (Method method : controllerClass.getMethods()) {

                    //方法头上有 @GetMapping 的注解
                    if (method.getAnnotation(GetMapping.class) != null) {
                        //1.校验 返回值类型
                        if (method.getReturnType() != ModelAndView.class &&
                                method.getReturnType() != void.class) {

                            throw new UnsupportedOperationException(
                                    "[my]Unsupported return type: " + method.getReturnType() + " in method: " + method);
                        }

                        //2.校验 参数类型
                        for (Class<?> parameterType : method.getParameterTypes()) {
                            if (!supportedGetParameterTypes.contains(parameterType)) {
                                throw new UnsupportedOperationException(
                                        "[my]Unsupported parameter Type: " + method.getReturnType() + " in method: " + method);
                            }

                        }
                        //3.所有的参数
                        Parameter[] parameters = method.getParameters();
                        String[] parameterNames = Arrays.stream(parameters)
                                .map(p -> p.getName()).toArray(String[]::new);//把所有的参数名字,放到数组中.
                        //4.获取请求路径
                        String path = method.getAnnotation(GetMapping.class).value();

                        logger.info("[my] found GET: {} => {}",path,method);

                        //5.将路径,与 dispatcher 组成一个map.
                        this.getMappings.put(
                                path,
                                new GetDispatcher(controllerInstance,method,parameterNames,method.getParameterTypes()));


                        //方法头上有 @PostMapping 的注解
                    } else if (method.getAnnotation(PostMapping.class) != null) {
                        if (method.getReturnType() != ModelAndView.class && method.getReturnType() != void.class) {
                            throw new UnsupportedOperationException(
                                    "Unsupported return type: " + method.getReturnType() + " for method: " + method);
                        }
                        Class<?> requestBodyClass = null;
                        for (Class<?> parameterClass : method.getParameterTypes()) {
                            if (!supportedPostParameterTypes.contains(parameterClass)) {
                                if (requestBodyClass == null) {
                                    requestBodyClass = parameterClass;
                                } else {
                                    throw new UnsupportedOperationException("Unsupported duplicate request body type: "
                                            + parameterClass + " for method: " + method);
                                }
                            }
                        }
                        String path = method.getAnnotation(PostMapping.class).value();
                        logger.info("Found POST: {} => {}", path, method);
                        this.postMappings.put(
                                path,
                                new PostDispatcher(controllerInstance,method,method.getParameterTypes(),objectMapper));
                    }
                }
            } catch ( Exception e) {
                e.printStackTrace();
            }
        }

        // 创建ViewEngine:
        this.viewEngine = new ViewEngine(getServletContext());
    }




}




















package com.welldo.mvc.demo2.framework;

import java.util.HashMap;
import java.util.Map;


// 自定义的 Controller，不要求实现特定接口，只需返回ModelAndView对象
public class ModelAndView {

    public Map<String,Object> model;    //string是请求参数的名字, object是请求参数的值。
    public String view;                 //模板的路径

    //构造器
    public ModelAndView(String view) {
        this.view = view;
        this.model = new HashMap<>();
    }

    //构造器
    public ModelAndView(String view, String name, Object value) {
        this.view = view;
        this.model = new HashMap<>();
        this.model.put(name, value);
    }

    //构造器
    public ModelAndView(String view, Map<String, Object> model) {
        this.view = view;
        this.model = new HashMap<>(model);
    }


}

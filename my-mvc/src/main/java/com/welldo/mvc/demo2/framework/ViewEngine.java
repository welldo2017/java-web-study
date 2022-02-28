package com.welldo.mvc.demo2.framework;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 *
 * 推荐模板引擎Pebble(使用Jinja语法)，它的特点是语法简单，支持模板继承，编写出来的模板类似：
 * <html>
 * <body>
 *   <ul>
 *   {% for user in users %}
 *     <li><a href="{{ user.url }}">{{ user.username }}</a></li>
 *   {% endfor %}
 *   </ul>
 * </body>
 * </html>
 *
 *
 */
public class ViewEngine {

    private final PebbleEngine engine;

    //构造器
    public ViewEngine(ServletContext servletContext) {
        // 定义一个ServletLoader用于加载模板:
        ServletLoader loader = new ServletLoader(servletContext);
        // 模板编码:
        loader.setCharset("UTF-8");
        // 模板前缀，这里默认模板必须放在`/WEB-INF/templates`目录:
        loader.setPrefix("/WEB-INF/templates");
        // 模板后缀:
        loader.setSuffix("");
        // 创建Pebble实例:
        this.engine = new PebbleEngine
                .Builder()
                .autoEscaping(true) // 默认打开HTML字符转义，防止XSS攻击
                .cacheActive(false) // 禁用缓存使得每次修改模板可以立刻看到效果
                .loader(loader).build();
    }


    // 其实ViewEngine非常简单，只需要实现一个简单的render()方法：
    public void render(ModelAndView mv, Writer writer) throws IOException {
        String view = mv.view;
        Map<String, Object> model = mv.model;

        // 根据view找到模板文件:
        PebbleTemplate template = this.engine.getTemplate(view);//从 /WEB-INF/templates 这个目录下，找到文件
        // 渲染并写入Writer:
        template.evaluate(writer, mv.model);
    }
}

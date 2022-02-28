package com.welldo.mvc.demo2.controller;

import com.welldo.mvc.demo2.bean.User;
import com.welldo.mvc.demo2.framework.ModelAndView;
import com.welldo.mvc.demo2.framework.annotation.GetMapping;

import javax.servlet.http.HttpSession;

public class IndexController {

	@GetMapping("/")
	public ModelAndView index(HttpSession session) {
		User user = (User) session.getAttribute("user");
		return new ModelAndView("/index.html", "user", user);
	}

	/**
	 * 为了把方法参数的名称编译到class文件中，以便处理@GetMapping时使用，
	 * 我们需要打开编译器的一个参数，请参考 pom文件中的插件：	 * maven-compiler-plugin
	 */
	@GetMapping("/hello")
	public ModelAndView hello(String name) {
		if (name == "") {		//GetDispatcher 类的 getOrDefault（）方法，只可能返回空字符串。
			name = "World";
		}
		return new ModelAndView("/hello.html", "name", name);
	}
}

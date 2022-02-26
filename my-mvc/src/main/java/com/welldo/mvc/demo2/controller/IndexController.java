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

	@GetMapping("/hello")
	public ModelAndView hello(String name) {
		if (name == null) {
			name = "World";
		}
		return new ModelAndView("/hello.html", "name", name);
	}
}

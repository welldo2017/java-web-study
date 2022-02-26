package com.welldo.mvc.demo2.controller;


import com.welldo.mvc.demo2.bean.SignInBean;
import com.welldo.mvc.demo2.bean.User;
import com.welldo.mvc.demo2.framework.ModelAndView;
import com.welldo.mvc.demo2.framework.annotation.GetMapping;
import com.welldo.mvc.demo2.framework.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
	//继承一个普通类,成为匿名内部类,并在代码块中,给自己赋值
	private Map<String, User> userDatabase = new HashMap<String, User>() {
		{
			List<User> users = new ArrayList<>();
			users.add(new User("bob@example.com", "bob123", "Bob", "This is bob."));
			users.add(new User("tom@example.com", "tomcat", "Tom", "This is tom."));

			users.forEach(user -> {
				//这里的put,实际上隐藏了 最前面的this(指向 匿名内部类自己)
				put(user.email, user);
			});
		}
	};


	@GetMapping("/signin")
	public ModelAndView signin() {
		return new ModelAndView("/signin.html");
	}

	@PostMapping("/signin")
	public ModelAndView doSignin(SignInBean bean, HttpServletResponse response, HttpSession session)
			throws IOException {
		User user = userDatabase.get(bean.email);
		if (user == null || !user.password.equals(bean.password)) {
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"error\":\"Bad email or password\"}");
			pw.flush();
		} else {
			session.setAttribute("user", user);
			response.setContentType("application/json");
			PrintWriter pw = response.getWriter();
			pw.write("{\"result\":true}");
			pw.flush();
		}
		return null;
	}

	@GetMapping("/signout")
	public ModelAndView signout(HttpSession session) {
		session.removeAttribute("user");
		return new ModelAndView("redirect:/");
	}


	//显示用户资料
	@GetMapping("/user/profile")
	public ModelAndView profile(HttpSession session) {
		User user = (User) session.getAttribute("user");
		if (user == null) {
			return new ModelAndView("redirect:/signin");
		}
		return new ModelAndView("/profile.html", "user", user);
	}

}

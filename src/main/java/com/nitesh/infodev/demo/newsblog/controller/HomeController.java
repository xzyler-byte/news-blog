package com.nitesh.infodev.demo.newsblog.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.nitesh.infodev.demo.newsblog.model.News;
import com.nitesh.infodev.demo.newsblog.model.User;
import com.nitesh.infodev.demo.newsblog.model.security.Role;
import com.nitesh.infodev.demo.newsblog.model.security.UserRole;
import com.nitesh.infodev.demo.newsblog.service.impl.NewsServiceImpl;
import com.nitesh.infodev.demo.newsblog.service.impl.UserServiceImpl;

@Controller
public class HomeController {
	@Autowired
	UserServiceImpl userService;
	@Autowired
	NewsServiceImpl newsService;
	@Autowired
	PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String main(Model model, Principal principal) {
		List<News> newses = newsService.getNews();
		model.addAttribute("newses", newses);
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("user/add")
	public String register(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("user/add")
	public String newUser(@ModelAttribute("user") User user, Model model) throws Exception {
		model.addAttribute("user", user);
		if (userService.findByUsername(user.getUsername()) != null) {
			model.addAttribute("usernameExists", true);
			return "signup";
		}
		/*
		 * String password = SecurityUtility.randomPassword();
		 * 
		 * String encryptedPassword =
		 * SecurityUtility.passwordEncoder().encode(password);
		 * user.setPassword(encryptedPassword);
		 */
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, role));
		userService.createUser(user, userRoles);
		userService.save(user);
		return "redirect:/login";
	}

}

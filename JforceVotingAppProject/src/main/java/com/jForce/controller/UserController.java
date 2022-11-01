package com.jForce.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.jForce.model.User;
import com.jForce.service.UserService;

@Controller
@RequestMapping(path = "/voting")
public class UserController {

	@Autowired
	private UserService us;

	// HOME PAGE
	@GetMapping("")
	public String home() {
		return "home";
	}

	// REGISTER
	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "registration-form";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute User user) {
		this.us.register(user);
		return "redirect:/voting/login";
	}

	// LOGIN
	@GetMapping("/login")
	public String login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return "login";
		}
		return "redirect:/voting/candidate/list";
	}

	// ADD VOTE
	@GetMapping("/addVote")
	public String addVote(@RequestParam Integer id, Principal principal, Model model) {
		String username = principal.getName();
		User foundUser = this.us.findByEmail(username);
		this.us.addVote(id, foundUser);
		return "redirect:/voting/votedPage";

	}

	@GetMapping("/votedPage")
	public String votedPage() {
		return "votedPage";
	}

	// LOGOUT
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/login";
	}
}

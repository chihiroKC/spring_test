package jp.co.sss.spring.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.form.RegisterForm;
import jp.co.sss.spring.service.LoginService;



@Controller
public class SessionController {

	private final LoginService loginService;	
	private final PasswordEncoder passwordEncoder;
	

	
	public SessionController(PasswordEncoder passwordEncoder, LoginService loginService) {
		this.passwordEncoder = passwordEncoder;
		this.loginService = loginService;
	}
	//新期登録
	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public String doRegister(@ModelAttribute RegisterForm form) {
		return "session/register";
	}
	
	@PostMapping(path = "/doRegister")
	public String doRegister(@Valid @ModelAttribute RegisterForm form,
			                 BindingResult result
			                 ) {
		if (result.hasErrors()) {
			return "session/register";
			}
		
		if (!form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("confirmPassword", "error.confirmPassword");
			return "session/register";
		}
		
		Login user = new Login();
		user.setName(form.getName());
		user.setEmail(form.getEmail());
		user.setPassword(passwordEncoder.encode(form.getPassword()));

		user.setPhone("");
		
		loginService.registerUser(user);
		
		return "redirect:/login?registered";
	}

	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String showLoginPage() {
		return "session/login";
	}

	@GetMapping("/login-error")
	public String loginError(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		String errorType = "unknown";
		
		if(session != null) {
			AuthenticationException ex = (AuthenticationException)
					session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if(ex != null) {
			if(ex instanceof org.springframework.security.core.userdetails.UsernameNotFoundException) {
				errorType = "notfound";
			} else if (ex instanceof org.springframework.security.authentication.BadCredentialsException) {
				errorType = "badcred";
			}
		}
		}
		
		return "redirect:/login?error=" + errorType;
	}
}

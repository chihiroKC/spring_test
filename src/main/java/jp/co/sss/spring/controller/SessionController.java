package jp.co.sss.spring.controller;

import jakarta.validation.Valid;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
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
			result.rejectValue("confirmPassword", "error.confirmPassword", "パスワードが一致しません。");
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
	
	//ログイン
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String doLogin() {
		return "session/login";
	}
	

}

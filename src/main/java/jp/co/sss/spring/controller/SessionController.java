package jp.co.sss.spring.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // ← これを追加！
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.repository.LoginRepository;



@Controller
public class SessionController {

	private final LoginRepository loginRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public SessionController(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}
	//新期登録
	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public String doRegister() {
		return "/session/register";
	}
	
	@PostMapping(path = "/doRegister")
	public String doRegister(@RequestParam String name,
			                 @RequestParam String email,
			                 @RequestParam String password,
			                 @RequestParam String confirmPassword) {
		
		if (!password.equals(confirmPassword)) {
			return "redirect:/register?error";
		}
		
		Login user = new Login();
		user.setName(name);
		user.setEmail(email);
		user.setPassword(passwordEncoder.encode(password));

		
		loginRepository.save(user);
		
		return "redirect:/login?registered";
	}
	
	//ログイン
	@RequestMapping(path = "/login", method = RequestMethod.GET)
	public String doLogin() {
		return "session/login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}

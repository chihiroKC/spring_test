package jp.co.sss.spring.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.repository.LoginRepository;

@Service
public class LoginService {
	

	private final LoginRepository loginRepository;
	
	public LoginService(LoginRepository loginRepository) {
		this.loginRepository = loginRepository;
	}

	@Transactional
	public void registerUser(Login login) {
		String name = login.getName();
		
		String kana = toHiragana(name);
		
		login.setNameKana(kana);
		
		loginRepository.save(login);
	}
	
	private String toHiragana(String input) {
	    StringBuilder sb = new StringBuilder();
	    for (char c : input.toCharArray()) {
	        // カタカナ → ひらがな
	        if (c >= 'ァ' && c <= 'ン') {
	            sb.append((char)(c - 0x60));
	        }
	        // 全角英数字や漢字などはそのまま保持
	        else {
	            sb.append(c);
	        }
	    }
	    return sb.toString();
	}
}

package jp.co.sss.spring.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.repository.LoginRepository;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.SaleRepository;
import jp.co.sss.spring.security.UserDetailsImpl;

@Controller
public class TopController {
	
	private final LoginRepository loginRepository;
	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;
	
	public TopController(LoginRepository loginRepository, SaleRepository saleRepository, ProductRepository productRepository) {
		this.loginRepository = loginRepository;
		this.saleRepository = saleRepository;
		this.productRepository = productRepository;
	}

	@GetMapping("/top")
	public String top(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
	    model.addAttribute("user_name", principal.getUsername()); // email が入る
	    model.addAttribute("sales", saleRepository.findAll());
	    return "top";
	}
	 
	 @GetMapping(path = "/mypage")
	 public String mypage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		 
		 Login loginUser = userDetails.getLogin();
		 
		 model.addAttribute("user", loginUser);
		 return "mypage";
	 }
	 
	 @RequestMapping(path = "/product")
		public String product() {
			return "product";
		}
	 @GetMapping("/search")
	 public String search(@RequestParam(value = "keyword", required = false) String keyword, Model model) {

		 
		 if (keyword != null && !keyword.isEmpty()) {
			 List<Product> searchResults = productRepository.findByNameContaining(keyword);
			 model.addAttribute("searchResults", searchResults);
			 
		 } else {
			 List<Product> products = productRepository.findAll();
			 model.addAttribute("products", products);
		 }
		 
		 return "/product/product_list";
	 }

}

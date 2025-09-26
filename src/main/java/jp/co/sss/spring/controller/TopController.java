package jp.co.sss.spring.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Category;
import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.repository.CategoryRepository;
import jp.co.sss.spring.repository.LoginRepository;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.SaleRepository;
import jp.co.sss.spring.security.UserDetailsImpl;


@Controller
public class TopController {
	
	private final LoginRepository loginRepository;
	private final SaleRepository saleRepository;
	private final ProductRepository productRepository;
	private final CategoryRepository categoryRepository;
	
	public TopController(LoginRepository loginRepository, SaleRepository saleRepository, ProductRepository productRepository, CategoryRepository categoryRepository) {
		this.loginRepository = loginRepository;
		this.saleRepository = saleRepository;
		this.productRepository = productRepository;
		this.categoryRepository = categoryRepository;
	}

	@GetMapping("/top")
	public String top(Model model, @AuthenticationPrincipal UserDetailsImpl principal) {
		if (principal != null) {
			model.addAttribute("user_name", principal.getLogin().getName()); 
		}
	    model.addAttribute("sales", saleRepository.findAll());
	    model.addAttribute("categories", categoryRepository.findAll());
	    
	    return "top";
	}
	 
	 @GetMapping(path = "/mypage")
	 public String mypage(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
		 
		 Integer loginUserId = userDetails.getLogin().getUserId();
		 
		 Login freshLoginUser = loginRepository.findById(loginUserId).orElse(null);
		 
		 model.addAttribute("user", freshLoginUser);
		 return "mypage";
	 }
	 
	 
	 @RequestMapping(path = "/product")
		public String product() {
			return "product";
		}
	 
	 
	 @GetMapping("/search")
	 public String search(@RequestParam(value = "keyword", required = false) String keyword, 
			              @RequestParam(value = "categoryId", required = false) Integer categoryId,
			              Model model,
			              @AuthenticationPrincipal UserDetailsImpl principal) {

		 if(principal != null) {
			 model.addAttribute("user_name", principal.getLogin().getName());
		 }
		 
		 List<Product> searchResults;
		 
		 if (categoryId != null) {
			 if (keyword != null && !keyword.isEmpty()) {
				 searchResults = productRepository.findByCategoryCategoryIdAndNameContaining(categoryId, keyword);
			 } else {
				 searchResults = productRepository.findByCategoryCategoryId(categoryId);
			 }
		 }
		 else if (keyword != null && !keyword.isEmpty()) {
			 searchResults = productRepository.findByNameContaining(keyword);
		 }
		 else {
			 searchResults = productRepository.findAll();
		 }
		 
		 model.addAttribute("searchResults", searchResults);
		 model.addAttribute("categories", categoryRepository.findAll());
		 return "/product/product_list";
	 }
	 
	 @GetMapping("/search/category")
	 public String searchByCategory(@RequestParam("categoryId") Integer categoryId, 
			                        Model model, 
			                        @AuthenticationPrincipal UserDetailsImpl principal) {
		 if(principal != null) {
			 model.addAttribute("user_name", principal.getLogin().getName());
		 }
		 
		 List<Product> searchResults = productRepository.findByCategoryCategoryId(categoryId);
		 model.addAttribute("searchResults", searchResults);
		 
		 List<Category> categories = categoryRepository.findAll();
		 model.addAttribute("categories", categories);
		 
		 return "/product/product_list";
	 }

}

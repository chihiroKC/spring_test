package jp.co.sss.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.entity.Review;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.ReviewRepository;

@Controller
public class ProductController {

	@Autowired
	ProductRepository productRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@RequestMapping("/product/findAll")
	public String showProductList(Model model) {
		model.addAttribute("products", productRepository.findAll());
		
		return "product/product_list";
	}
	
	@RequestMapping("/product/detail/{id}")
	public String showProductDetail(@PathVariable Integer id, Model model) {
		Product product = productRepository.findById(id).orElse(null);
		
		if(product == null) {
			return "redirect:/product/list";
		}
		
		List<Review> reviews = reviewRepository.findByProductOrderByReviewIdDesc(product);
		
		model.addAttribute("product", product);
		model.addAttribute("reviews", reviews);
		
		return "product/product_list_id";
	}
	
	@GetMapping("/product/{id}")
	public String getProduct(@PathVariable Integer id, Model model) {
		Product product = productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
		model.addAttribute("product", product);
		
		return "product/product_list";
	}

}

package jp.co.sss.spring.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.entity.Review;
import jp.co.sss.spring.repository.LoginRepository;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.ReviewRepository;

@Controller
public class ReviewController {

	@Autowired
	private LoginRepository loginrepository;
	
	@Autowired
	private ReviewRepository reviewrepository;
	
	@Autowired
	private ProductRepository productrepository;
	
	
	@PostMapping("/reviews/add")
	public String addReview(@RequestParam("productId") Integer productId,
			                @RequestParam String dummyUserName,
			                @RequestParam int rating,
			                @RequestParam String email,
			                @RequestParam String comment,
			                @RequestParam(required = false) MultipartFile reviewImgPath,
			                Model model) {
		
		System.out.println("ProductID" + productId);
		
		Login login = loginrepository.findByEmail(email).orElseThrow();
		if (login == null) {
			model.addAttribute("error", "入力されたメールアドレスは登録されていません。");
			model.addAttribute("productId", productId);
			return "review";
		}
		
		Product product = productrepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("指定された商品が存在しません"));
		
		Review review = new Review();
		review.setDummyUserName((dummyUserName != null && !dummyUserName.isBlank()) ? dummyUserName : "匿名ユーザー");
		review.setRating(rating);
		review.setComment(comment);
		review.setLogin(login);
		review.setProduct(product);
		
		if (reviewImgPath != null && !reviewImgPath.isEmpty()) {
			String fileName = reviewImgPath.getOriginalFilename();
			Path path = Paths.get("uploads/" + fileName);
			try {
				Files.copy(reviewImgPath.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				review.setReviewImgPath("/uploads/" + fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		reviewrepository.save(review);
		
		return "redirect:/product/" + productId;
	}
	@PostMapping("/reviews/new/{productId}")
	public String showReviewForm(@PathVariable Integer productId, Model model) {
		
		System.out.println("productID:get" + productId);
		model.addAttribute("productId", productId);
		return "review";
	}
}

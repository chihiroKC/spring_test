package jp.co.sss.spring.controller;

import java.math.BigDecimal;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.entity.Review;
import jp.co.sss.spring.entity.Sale;
import jp.co.sss.spring.repository.ReviewRepository;
import jp.co.sss.spring.repository.SaleRepository;
import jp.co.sss.spring.service.SaleService;

@Controller
public class SaleController {

	@Autowired
	SaleRepository saleRepository;
	
	@Autowired
	ReviewRepository reviewRepository;
	
	@Autowired
	private SaleService saleService;
	
	@GetMapping("/sales/discount")
	public String showDiscountProducts(@RequestParam("saleItemId") Integer saleItemId, Model model) {
		Sale sale = saleRepository.findById(saleItemId).orElse(null);
		
		if (sale == null || sale.getDiscountRate() == null || sale.getDiscountRate() <= 0) {
			return "redirect:/sales";
		}
		
		Product product = sale.getProduct();
		
		List<Review> reviews = reviewRepository.findByProductOrderByReviewIdDesc(sale.getProduct());
		
		 BigDecimal discountedPrice = saleService.calculateDiscountedPrice(sale);
		 BigDecimal discountedTaxPrice = saleService.calculatePriceWithTax(discountedPrice);
				
		model.addAttribute("sale", sale);
		model.addAttribute("product", sale.getProduct());
		model.addAttribute("reviews", reviews);
		model.addAttribute("discountedPrice", discountedPrice);
		model.addAttribute("discountedTaxPrice", discountedTaxPrice);
		
		return "product/discount_list_id";
	}
	
	 @GetMapping("/sales")
	 public String showSalesItem(HttpSession session, Model model) {
		 List<Sale> sales_items = saleRepository.findAll();
		 
		 model.addAttribute("sales", sales_items);
		 
		 return "product/discount_list";
	 }
	 
	 @GetMapping("/sales/{id}")
	 public String showSaleDetail(@PathVariable("id") Integer saleItemId, Model model) {
		 Sale sale = saleRepository.findById(saleItemId).orElseThrow();
		 
		 List<Review> reviews = reviewRepository.findByProductOrderByReviewIdDesc(sale.getProduct());
		 
		 BigDecimal discountedPrice = saleService.calculateDiscountedPrice(sale);
		 BigDecimal discountedTaxPrice = saleService.calculatePriceWithTax(discountedPrice);
		 
		 model.addAttribute("sale", sale);
		 model.addAttribute("discountedPrice", discountedPrice);
		 model.addAttribute("discountedTaxPrice", discountedTaxPrice);
		 model.addAttribute("reviews", reviews);
		 
		 return "order/cart_add";
	 }
}

package jp.co.sss.spring.controller;

import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jp.co.sss.spring.entity.Sale;
import jp.co.sss.spring.repository.SaleRepository;

@Controller
public class SaleController {

	@Autowired
	SaleRepository saleRepository;
	
	@GetMapping("/sales/discount")
	public String showDiscountProducts(@RequestParam("saleItemId") Integer saleItemId, Model model) {
		Sale sale = saleRepository.findById(saleItemId).orElse(null);
		
		if (sale == null || sale.getDiscountRate() == null || sale.getDiscountRate() <= 0) {
			return "redirect:/sales";
		}
		
		model.addAttribute("sale", sale);
		model.addAttribute("product", sale.getProduct());
		return "product/discount_list_id";
	}
	
	 @GetMapping("/sales")
	 public String showSalesItem(HttpSession session, Model model) {
		 List<Sale> sales_items = saleRepository.findAll();
		 
		 model.addAttribute("sales", sales_items);
		 
		 return "product/discount_list";
	 }
}

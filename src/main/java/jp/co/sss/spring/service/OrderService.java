package jp.co.sss.spring.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.sss.spring.entity.Login;
import jp.co.sss.spring.entity.Order;
import jp.co.sss.spring.entity.Product;
import jp.co.sss.spring.entity.Sale;
import jp.co.sss.spring.repository.OrderRepository;
import jp.co.sss.spring.repository.ProductRepository;
import jp.co.sss.spring.repository.SaleRepository;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private SaleService saleService;
	
	
	@Transactional
	public void createSingleOrder(Integer productId, int quantity, Login login) {
		Product product = productRepository.findById(productId.intValue()).orElseThrow();
		
		Order order = new Order();
		order.setLogin(login);
		order.setProduct(product);
		order.setQuantity(quantity);
		
		order.setPrice(product.getPrice() * quantity);
		order.setTotalAmount(order.getPrice());
		order.setStatus("NEW");
		
		order.setDisplayName(product.getName());
		
		orderRepository.save(order);
	}
	
	@Transactional
	public void createSingleOrderWithSale(Integer productId, Integer saleItemId, int quantity, Login login) {
		Product product = productRepository.findById(productId).orElseThrow();
		Sale sale = saleRepository.findById(saleItemId).orElseThrow();
		
		Order order = new Order();
		order.setLogin(login);
		order.setProduct(product);
		order.setSale(sale);
		order.setQuantity(quantity);
		
		BigDecimal discountedPrice = saleService.calculateDiscountedPrice(sale);
		int totalAmount = discountedPrice.multiply(BigDecimal.valueOf(quantity)).intValue();
		
		order.setTotalAmount(totalAmount);
		order.setStatus("NEW");
		
		order.setDisplayName(sale.getName());
		
		orderRepository.save(order);
	}
	
	@Transactional(readOnly = true)
	public List<Order> getOrders() {
		return orderRepository.findAll();
	}
}

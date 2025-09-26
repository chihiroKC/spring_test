package jp.co.sss.spring.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;

import jp.co.sss.spring.entity.Sale;

@Service
public class SaleService {


	public BigDecimal calculateDiscountedPrice(Sale sale) {
		if (sale == null || sale.getProduct() == null || sale.getProduct().getPrice() == null) {
			return BigDecimal.ZERO;
		}
		
		if (sale.getDiscountRate() == null || sale.getDiscountRate() <= 0) {
			return BigDecimal.valueOf(sale.getProduct().getPrice());
		}
		
		BigDecimal price = BigDecimal.valueOf(sale.getProduct().getPrice());
		BigDecimal discount = BigDecimal.valueOf(sale.getDiscountRate())
				.divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
		BigDecimal discounted = price.multiply(BigDecimal.ONE.subtract(discount));
		
		return discounted.setScale(0, RoundingMode.FLOOR);
	}
	
	public BigDecimal calculatePriceWithTax(BigDecimal priceWithoutTax) {
		if (priceWithoutTax == null) {
			return BigDecimal.ZERO;
		}
		
		final BigDecimal TAX_RATE = new BigDecimal("0.10");
		
		BigDecimal priceWithTax = priceWithoutTax.multiply(BigDecimal.ONE.add(TAX_RATE));
		
		return priceWithTax.setScale(0, RoundingMode.FLOOR);
	}

}

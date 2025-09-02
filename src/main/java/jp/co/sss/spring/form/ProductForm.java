package jp.co.sss.spring.form;

import jakarta.validation.constraints.NotNull;

public class ProductForm {

	@NotNull
	private Integer productId;
	@NotNull
	private String name;
	@NotNull
	private Integer companyId;

	@NotNull
	private Integer price;
	
	private Integer taxPrice;
	@NotNull
	private Integer Stock;
	
	@NotNull
	private String imgPath;
	
	
	
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(Integer taxPrice) {
		this.taxPrice = taxPrice;
	}
	public Integer getStock() {
		return Stock;
	}
	public void setStock(Integer stock) {
		Stock = stock;
	}
	public String getImgPath() {
		return imgPath;
	}
	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}
	
}

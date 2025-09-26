package jp.co.sss.spring.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_items")
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_sales_gen")
	@SequenceGenerator(name = "seq_sales_gen", sequenceName = "seq_sales", allocationSize = 1)

	private Integer saleItemId;
	@Column(name = "sale_name")
	private String name;
	@Column
	private String description;
	@Column(name = "discount_rate")
	private Integer discountRate;
	@Column(name = "sales_img_path")
	private String saleImgPath;
	
	@ManyToOne
	@JoinColumn(name = "company_id", insertable = false, updatable = false)
	private Company company;
	
	@ManyToOne
	@JoinColumn(name = "product_id", insertable = false, updatable = false)
	private Product product;
	
	
	public Integer getSaleItemId() {
		return saleItemId;
	}
	public void setSaleItemId(Integer saleItemId) {
		this.saleItemId = saleItemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(Integer discountRate) {
		this.discountRate = discountRate;
	}
	public String getSaleImgPath() {
		return saleImgPath;
	}
	public void setSaleImgPath(String saleImgPath) {
		this.saleImgPath = saleImgPath;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}


}

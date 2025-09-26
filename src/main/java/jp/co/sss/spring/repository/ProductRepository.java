package jp.co.sss.spring.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer>{

	List<Product> findByNameContaining(String keyword);
	
	List<Product> findByCategoryCategoryId(Integer categoryId);
	
	List<Product> findByCategoryCategoryIdAndNameContaining(Integer categoryId, String keyword);
}

package jp.co.sss.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Integer>{

}

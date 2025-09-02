package jp.co.sss.spring.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jp.co.sss.spring.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{

	List<Order> findByUserId(Integer userId);
	
	Optional<Order> findTopByUserIdOrderByOrderIdDesc(Integer userId);
	
	List<Order> findByUserIdOrderByOrderIdDesc(Integer userId);

	@Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.userId = :userId")
	List<Order> findByUserIdWithProduct(@Param("userId") Integer userId);

	@Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.userId = :userId ORDER BY o.id DESC")
	List<Order> findAllByUserIdOrderByOrderIdDescWithProduct(@Param("userId") Integer userId);
	
	List<Order> findByUserIdAndStatus(Integer userId, String status);
	
	List<Order> findByUserIdAndStatusOrderByOrderIdDesc(Integer userId, String Status);
}

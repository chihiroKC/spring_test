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

	List<Order> findByLoginUserId(Integer userId);
	
	Optional<Order> findTopByLoginUserIdOrderByOrderIdDesc(Integer userId);
	
	List<Order> findByLoginUserIdOrderByOrderIdDesc(Integer userId);

	@Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.login.userId = :userId")
	List<Order> findByLoginUserIdWithProduct(@Param("userId") Integer userId);

	@Query("SELECT o FROM Order o JOIN FETCH o.product WHERE o.login.userId = :userId ORDER BY o.id DESC")
	List<Order> findAllByLoginUserIdOrderByOrderIdDescWithProduct(@Param("userId") Integer userId);
	
	List<Order> findByLoginUserIdAndStatus(Integer userId, String status);
	
	List<Order> findByLoginUserIdAndStatusOrderByOrderIdDesc(Integer userId, String Status);
}

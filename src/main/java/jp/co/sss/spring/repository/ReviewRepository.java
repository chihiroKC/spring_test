package jp.co.sss.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jp.co.sss.spring.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer>{


}

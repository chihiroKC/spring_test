package jp.co.sss.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring.entity.Card;

public interface CardRepository extends JpaRepository<Card, String> {

}
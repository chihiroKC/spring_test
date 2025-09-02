package jp.co.sss.spring.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.sss.spring.entity.Login;

public interface LoginRepository extends JpaRepository<Login, Integer>{

	Login findByEmailAndPassword(String email, String password);
	
	Optional<Login> findByEmail(String email);
	
}

package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;

// <User, Integer>
// 해당 JpaRepository는 User 테이블을 관리한다. 그리고 User 테이블의 PK(Primary Key)는 Integer다.
// JSP 체계에서 DAO와 같음
// 자동으로 bean등록이 된다. - @Repository가 생략가능하다.
public interface UserRepository extends JpaRepository<User, Integer>{
	
	// SELECT * FROM user WHERE username=1?;
	Optional<User> findByUsername(String username);
	
	
}


//JPA Naming 쿼리
	// 둘 중 하나를 사용하면 된다.
	// SELECT * FROM user WHERE username=?1 AND password=?2;
//1.	User findByUsernameAndPassword(String username, String password);
	
//2.	@Query(value="SELECT * FROM user WHERE username=?1 AND password=?2", nativeQuery=true)
//	User login(String username, String password)

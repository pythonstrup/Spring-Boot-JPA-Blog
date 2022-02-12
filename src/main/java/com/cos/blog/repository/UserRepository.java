package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// <User, Integer>
// 해당 JpaRepository는 User 테이블을 관리한다. 그리고 User 테이블의 PK(Primary Key)는 Integer다.
// JSP 체계에서 DAO와 같음
// 자동으로 bean등록이 된다. - @Repository가 생략가능하다.
public interface UserRepository extends JpaRepository<User, Integer>{

}

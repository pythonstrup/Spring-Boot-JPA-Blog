package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//ORM이란? Java(다른언어도 마찬가지) Object -> 테이블로 매핑해주는 기술
@Entity
//@DynamicInsert // insert할때 null 인 필드 제외
public class User {

	@Id // Primary key 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다. - identity
	private int id; // aoto_increment
	
	@Column(nullable=false, length=30, unique=true)
	private String username; // 아이디
	
	@Column(nullable=false, length=100)
	private String password;
	
	@Column(nullable=false, length=100)
	private String email;
	
	// @ColumnDefault("'user'")
	// DB는 RoleType이라는 게 없다. - 어노테이션으로 Enum타입이 String이라고 알려줘야함
	@Enumerated(EnumType.STRING)
	private RoleType role; // String보다 Enum을 쓰는 것이 좋다. (데이터의 도메인을 만들기 위해서) - ex) admin, user, manger
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}

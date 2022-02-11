package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
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
public class User {

	@Id // Primary key 지정
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다. - identity
	private int id; // aoto_increment
	
	@Column(nullable=false, length=30)
	private String username; // 아이디
	
	@Column(nullable=false, length=100)
	private String password;
	
	@Column(nullable=false, length=100)
	private String email;
	
	@ColumnDefault("'user'") // 디폴트값 'user'
	private String role; // 일단은 String을 쓰지만, Enum을 쓰는 것이 좋다. (데이터의 도메인을 만들기 위해서) - ex) admin, user, manger
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}

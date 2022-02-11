package com.cos.blog.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter - getter만 설정
//@Setter - setter만 설정
//@AllArgsConstructor - 모든 인자를 가진 생성자 만들기
//@Data - getter&setter 전부 설정
//@RequiredArgsConstructor - final이 붙은 필드나 @NonNull이 붙은 필드에 대해 생성자 생성

@Data
//@AllArgsConstructor
@NoArgsConstructor // 인자가 없는 생성자 만들기 - final필드가 있으면 불가
public class Member {
	private int id;
	private String username;
	private String password;
	private String email;
	
	@Builder
	public Member(int id, String username, String password, String email) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
	}
	
	
}

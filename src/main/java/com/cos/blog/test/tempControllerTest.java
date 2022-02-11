package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class tempControllerTest {

	// html은 정적 파일
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempHome()");
		
		// 파일리턴 기본경로: src/main/resources/static
		// 리턴명: /home.html
		// 풀경로: src/main/resources/static/home.html
		return "/home.html";
	}
	
	// 이미지는 정적파일
	@GetMapping("temp/img")
	public String tempImg() {
		return "/cookie.png";
	}
	
	
	// 파일리턴 기본 경로로는 못 찾는다. 왜나하면 동적인 파일이기 때문에
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		
		// yml파일로 경로 설정
		// prefix: /WEB-INF/views/
		// suffix: .jsp
		// 풀네임: /WEB-INF/views/test.jsp
		return "test";
	}
}

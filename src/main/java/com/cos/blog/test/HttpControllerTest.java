package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 사용자 요청 -> 응답(HTML 파일)
// @Controller

//사용자 요청 -> 응답(Data)
@RestController
public class HttpControllerTest {
	
	private static final String TAG = "HttpControllerTest";
	
	@GetMapping("/http/lombok")
	public String lombokTest() {
		Member m = Member.builder().username("cos").password("1234").email("cos@naver.com").build();
		System.out.println(TAG+": getter: " +m.getUsername());
		m.setUsername("sar");
		System.out.println(TAG+": getter: " +m.getUsername());
		
		return "lombok test 완료";
	}
	
	// 인터넷 브라우저 요청은 Get만 가능
	@GetMapping("/http/get")
	public String getTest(Member m) { // ?id=1&username=hello spider&password=1234&email=ssar@nate.com
		return "get 요청: " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member m) { // text파일은 @RequestBody String text로만 받을 수 있다.
		//  text/plain  vs  application/json   ==> MessageConverter(스프링부트)
		return "post 요청: "  + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청: "  + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() + ", " + m.getEmail();
	}
	
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
}

package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDTO;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	// http://localhost:8000/blog/api/user
	@PostMapping("/auth/joinProc")
	public ResponseDTO<Integer> save(@RequestBody User user) { // username, password, email
		System.out.println("UserApiController: save 호출됨.");
		
		//  DB에 insert하기
		user.setRole(RoleType.USER);
		userService.회원가입(user);
		return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
	
	
}


/*@Autowired
private HttpSession session;
 //전통적인 로그인 방식 (시큐리티 사용하지 않은 버전)
@PostMapping("/api/user/login")
public ResponseDTO<Integer> login(@RequestBody User user) {
	System.out.println("UserApiController: login 호출됨");
	User principal = userService.로그인(user); // principal (접근주체)
	
	if (principal != null) {
		this.session.setAttribute("principal", principal);
	}
	
	return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
}*/
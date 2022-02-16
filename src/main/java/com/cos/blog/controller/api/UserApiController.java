package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ResponseDTO;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// http://localhost:8000/blog/api/user
	@PostMapping("/auth/joinProc")
	public ResponseDTO<Integer> save(@RequestBody User user) { // username, password, email
		System.out.println("UserApiController: save 호출됨.");
		
		//  DB에 insert하기
		user.setRole(RoleType.USER);
		userService.회원가입(user);
		return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// key=value, x-www-form-urlencoded로 받고 싶으면 @RequestBody 안 적어도됨.
	@PutMapping("/user")
	public ResponseDTO<Integer> update(@RequestBody User user) { 
		userService.회원수정(user);
		
		// 이 시점에서 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됐지만, 세션 값은 변경되지 않은 상태이다.
		// 로그아웃을 하고 다시 들어가야지만, 변경된 것을 확인할 수 있다. 그렇기 때문에 직접 세션값을 변경해줘야함.

		// 세션 등록
		Authentication authentication = 
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
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
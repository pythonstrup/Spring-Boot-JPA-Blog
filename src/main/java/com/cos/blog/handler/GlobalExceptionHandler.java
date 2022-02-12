package com.cos.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

// 어디에서든 에러가 발생했을 때 이 클래스의 함수로 오게 만들려면 @ControllerAdvice와 @ExceptionHandler 를 적어주면 됨.
@ControllerAdvice
@RestController
public class GlobalExceptionHandler {

	@ExceptionHandler(value=IllegalArgumentException.class)
	public String handleArgumentException(IllegalArgumentException e) {
		
		return "<h1>"+e.getMessage()+"</h1>";
	}
}

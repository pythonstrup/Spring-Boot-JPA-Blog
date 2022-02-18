package com.cos.blog.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.ReplySaveRequestDTO;
import com.cos.blog.dto.ResponseDTO;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.service.BoardService;

@RestController
public class BoardApiController {
	
	@Autowired 
	private BoardService boardService;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	// http://localhost:8000/blog/api/user
	@PostMapping("/api/board")
	public ResponseDTO<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) { 
		
		boardService.글쓰기(board, principal.getUser());
		return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@PutMapping("/api/board/{id}")
	public ResponseDTO<Integer> update(@PathVariable int id, @RequestBody Board board) {
		
		boardService.글수정하기(id, board);
		return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
	}
	
	@DeleteMapping("/api/board/{id}")
	public ResponseDTO<Integer> deleteById(@PathVariable int id) {
		
		boardService.글삭제하기(id);
		return new ResponseDTO<Integer>(HttpStatus.OK.value(), 1);
	}
	
	// 데이터를 받을 때 컨트롤러에서 dto를 만들어서 받는 게 좋다.
	// 그렇다면 만약 dto 사용하지 않는다면 그 이유는??? - 프로그램이 커질까봐.
	@PostMapping("/api/board/{boardId}/reply")
	public ResponseDTO<Integer> replySave(@RequestBody ReplySaveRequestDTO replySaveRequestDTO) { 
		
		boardService.댓글쓰기(replySaveRequestDTO);
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
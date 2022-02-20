package com.cos.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.dto.ReplySaveRequestDTO;
import com.cos.blog.model.Board;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final ReplyRepository replyRepository;
	
//	public BoardService(BoardRepository bRepo, ReplyRepository rRepo) {
//		this.boardRepository = bRepo;
//		this.replyRepository = rRepo;
//	}
	

	@Transactional
	public void 글쓰기(Board board, User user) { // title, content
		
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly=true)
	public Page<Board> 글목록(Pageable pageable) {
		
		return boardRepository.findAll(pageable);
	}

	@Transactional(readOnly=true)
	public Board 글상세보기(int id) {
		
		return boardRepository.findById(id)
				.orElseThrow(()-> {
					return new IllegalArgumentException("글 상세보기 실패: 아이디를 찾을 수 없습니다.");
				});
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		
		System.out.println("글삭제하기: " +id);
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		
		Board board = boardRepository.findById(id)
				.orElseThrow(()-> {
					return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
				}); // 영속화 완료
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수 종료시(Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티체킹 - 자동 업데이트가 됨. db Flush
	}

	// DTO 사용 버전 - 노가다 버전
	
//	@Autowired
//	private UserRepository userRepository;
//	public void 댓글쓰기(ReplySaveRequestDTO replySaveRequestDTO) {
//
//		User user = userRepository.findById(replySaveRequestDTO.getUserId())
//				.orElseThrow(()-> {
//					return new IllegalArgumentException("댓글 쓰기 실패: 유저 id를 찾을 수 없습니다.");
//				}); // 영속화 완료
//		
//		Board board = boardRepository.findById(replySaveRequestDTO.getBoardId())
//				.orElseThrow(()-> {
//					return new IllegalArgumentException("댓글 쓰기 실패: 게시글 id를 찾을 수 없습니다.");
//				}); // 영속화 완료
//		
//		Reply reply = Reply.builder()
//							.user(user)
//							.board(board)
//							.content(replySaveRequestDTO.getContent())
//							.build();
//
//		replyRepository.save(reply);
//	}

	// DTO 간편 버전
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDTO replySaveRequestDTO) {
				
		replyRepository.mySave(replySaveRequestDTO.getUserId(), replySaveRequestDTO.getBoardId(), replySaveRequestDTO.getContent());
	}

	@Transactional
	public void 댓글삭제(int replyId) {
		
		replyRepository.deleteById(replyId);
	}
}

//전통적인 로그인 방식(시큐리티 사용하지 않는 방식)
//@Transactional(readOnly=true) // Select할 때 트랜잭션 시작, 서비스 종료 시에 트랜잭션 종료(정합성)
//public User 로그인(User user) {
//	
//	return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//}

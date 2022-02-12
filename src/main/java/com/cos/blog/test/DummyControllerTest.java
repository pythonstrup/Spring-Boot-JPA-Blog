package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// data를 리턴해주는 controller
@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입
	private UserRepository userRepository;
	
	@DeleteMapping("/dummy/user/{id}")
	public String deleteUser(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		return "삭제되었습니다. id: " + id;
	}
	
	// 1. save 함수는 id를 전달하지 않으면 insert를 해줌.
	// 2. save 함수는 id를 전달하면 해당 id에 대한 데이터가 있으면 update를 해줌.
	// 3. save 함수는 id를 전달했는데 해당 id에 대한 데이가 없으면 insert를 실행
	// email, password만 수정 가능
	
	@Transactional // 함수 종료 시에 자동 commit이 됨.
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		// json 데이터 요청 => Java Object(MessageConvertor의 Jackson라이브러리가 변환해서 받아줌.)(@RequestBody)
		
		System.out.println("id: " + id);
		System.out.println("password: " + requestUser.getPassword());
		System.out.println("email: " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패했습니다.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// userRepository.save(user);
		
		// 더티 체킹이란?
		// 영속성 컨텍스트의 개념을 알아야함.
		// Controller는 요청을 받아 Insert, Update, Delete, Select 등을 실행함.
		// 이제 Insert 명령을 통해 객체를 DB에 넣을 것이다. 이 객체를 User 객체라고 해보자.
		// JPA는 영속성 컨텍스트를 가지고 있는데, 이 안에 1차 캐시라는 공간을 가지고 있음. 이곳에 요청 시 필요한 객체(User)가 쌓임.
		// 1차 캐시에 user라는 객체가 들어가게 되면, 그 객체는 영속화되었다는 의미임. 그 객체를 DB에 넣는 동작은 flush라고 한다.
		// flush는 원래 버퍼를 싹 비워비워서 다른 곳에 옮기는 것인데, JPA 영속성 컨텍스트에서는 1차 캐시는 비우지 않고 flush함.
		// 이 기능을 통해 DB에 직접 접근하지 않고, 영속화된 객체를 통해 요청응답을 처리할 수 있게됨. 
		// select 등의 명령을 통해 새로운 객체가 필요해지면 1차 캐시 DB에서 정보를 가져와 객체를 영속화시킴.
		// 영속성을 사용하면 save()메소드를 사용하지 않고 flush를 통해 DB에 자동으로 저장할 수 있다.
		
		// 더티체킹(Dirty Checking)이란 상태 변경 검사이다. (찌꺼기같은 것, 더러운 것을 체크해서 날리는 것이라고 생각하자.)
		// JPA에서는 트랜잭션이 끝나는 시점에 변화가 있는 모든 엔티티 객체를 데이터베이스 반영한다.(commit)
		// 그렇기 때문에 값을 변경한 뒤, save 하지 않더라도 DB에 반영되는 것이다.
		return user;
	}
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		// 분기 처리도 가능
//		if(pagingUser.isFirst()) {
//			
//		}
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	// {id} 주소로 파라미터를 전달받을 수 있음
	// http://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		// 람다식 사용할 수 있다. - 더 간단하지만 어려운 편
//		User user = userRepository.findById(id).orElseThrow(()->{
//			return new IllegalArgumentException("해당 유저는 없습니다. - user ID: " + id);
//		})
		
		
		// 만약 잘못된 id로 검색해서 데이터베이스에서 못 찾아오게 되면 null이 된다.
		// 그럼 return null이 되기 때문에 프로그램에 문제가 생길 수 있다.
		// Optional로 User객체를 감싸서 가져와 null인지 판단해서 return하기
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				
				return new IllegalArgumentException("해당 유저는 없습니다. - user ID: " + id);
			}
		});
		// 요청: 웹 브라우저
		// user 객체 = 자바 오브젝트
		// 변환 (웹 브라우저가 이해할 수 잇는 데이터) -> json (원래 스프링에서는 Gson 라이브러리 사용함)
		// 반면 스프링부트는 MessageConvertor가 응답 시 자동 작동
		// 자바 오브젝트를 리턴하면 MessageConvertor가 Jackson 라이브러리를 호출.
		// user 오브젝트를 json으로 변환해 브라우저에게 던져줌.
		return user;
	}
	
	// http://localhost:8000/blog/dummy/join(요청)
	// http의 body에 username, password, email 데이터를 가지고 요청
	@PostMapping("/dummy/join")
	public String join(User user) { // key=value (약속된 규칙)
		System.out.println("id: " + user.getId());
		System.out.println("username: " + user.getUsername());
		System.out.println("password: " + user.getPassword());
		System.out.println("email: " + user.getEmail());
		System.out.println("role: " + user.getRole());
		System.out.println("createDate: " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}

package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// @Service는 왜 필요할까?
// 1. 트랜잭션을 관리하기 위해서 (전체적인 layer: Controller - Service - DAO)
// 2. 서비스의 의미
// 송금 서비스가 있다고 해보자. 송금이 발생 시 어떤 일이 일어날까?
// ① 송금자의 금액을 업데이트한다. - commit
// ② 수금자의 금액을 업데이트한다. - commit
// 데이터베이스 로직이 2개 이상이다. 그래서 서비스가 필요한 것이다.
// @Repository는 crud를 하나씩만 가지고 있어도 괜찮지만, @Service는 2개 이상의 업데이트가 필요한 상황.
// 2개의 과정 중 하나가 실패했다고 보자. 만약 송금자는 성공하고 수금자는 실패한다면, 금액이 날라가게 될 것이다.
// 이렇게 실패가 발생하면 두 과정 모두 Rollback을 시켜서 복구해야 문제가 해결된다.
// 2가지 트랜잭션을 하나로 묶어주는 것이 @Service이다.

// 스프링이 컴포넌트 스캔을 통해 Bean에 등록해줌.
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	// 트랜잭션 : 일이 처리되기 위한 가장 작은 단위
	// 여러 개의 트랜잭션을 묶어서 하나의 트랜잭션으로 만들 수도 있다.
	// DB 격리 수준: READ COMMIT
	
	// 부정합의 종류 3가지 (고립성과 무결성의 지표)
	// 더티리드: 하나의 트랜잭션에서 작업이 완료되지 않았는데도 다른 트랜잭션에서 볼 수 있게 되는 현상
	// NON-REPEATABLE READ: 트랜잭션을 시작해서 트랜잭션 끝까지 동일한 Select를 하면 계속 같은 값이 나와야하는데 다른 값이 나오는 것을 말함.
	// 팬텀리드: 하나의 트랜잭션에서 일정 범위으 레코드를 두 번 이상 읽을 때, 첫 번째 쿼리에서 없던 레코드(유령, Phantom)가 두 번째 쿼리에서 발생하는 현상
	
	// REPEATABLE READ 이상 방식을 사용하면 부정합이 발생하지 않는다.
	@Transactional
	public void 회원가입(User user) {

		String rawPassword = user.getPassword();
		String encPassword = encoder.encode(rawPassword);
		user.setPassword(encPassword);
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

	@Transactional
	public void 회원수정(User user) {
		// 수정 시에는 영속성 컨텍스트에 User 오브젝트를 영속화시키고, 영속화된 User 오브젝트를 수정.
		// Select를 해서 User 오브젝트를 DB로부터 가져오는 이유는 영속화를 하기 위해서.
		// 영속화된 오브젝트를 변경하면 자동으로 DB에 update문을 날려줌.
		User persistent = userRepository.findById(user.getId())
					.orElseThrow(()->{
						return new IllegalArgumentException("회원 찾기 실패");
					});
		
		// validation 체크 -  oauth값이 있으면 수정 불가 (OAuth에 대한 POST 공격 예방)
		if(persistent.getOauth() == null || persistent.getOauth().equals("")) {
			String rawPassword = user.getPassword();
			String encPassword = encoder.encode(rawPassword);
			persistent.setPassword(encPassword);
			persistent.setEmail(user.getEmail());
		}
		
		
		// 회원 수정 함수 종료 시 = 서비스 종료 = 트랜잭션 종료 = Commit이 자동으로 진행
		// 영속화된 persistent 객체의 변화가 감지되면 더티체킹을 하는데, 이를 통해 update문을 날려줌.
	}

	@Transactional(readOnly=true)
	public User 회원찾기(String username) {
		
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User();
		});
		return user;
	}
}

//전통적인 로그인 방식(시큐리티 사용하지 않는 방식)
//@Transactional(readOnly=true) // Select할 때 트랜잭션 시작, 서비스 종료 시에 트랜잭션 종료(정합성)
//public User 로그인(User user) {
//	
//	return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//}

package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;

// 빈 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것

@Configuration // 빈 등록(IoC관리)
@EnableWebSecurity // 시큐리티 필터가 등록이 된다. - 설정은 클래스 안에서..
@EnableGlobalMethodSecurity(prePostEnabled=true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		
		return super.authenticationManagerBean();
	}

	@Bean // IoC가 된다 = 스프링이 관리한다.
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인해줄 때 password를 가로챔. 해당 password가 어떤 해쉬가 외어 회원가입되었는지 알아야
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬와 비교할 수 있음.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
			.csrf().disable() // crsf 토큰 비활성화 (테스트 시 걸어두는 게 좋음.) 테스트가 끝난 후 활성화, ajax 요청 시 csrf 토큰을 전송해줘야함.  
			.authorizeRequests()
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**")
				.permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm")
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 오청오는 로그인을 가로채서 대신 로그인해줌.
				.defaultSuccessUrl("/"); 
	}


	
}

// xss 란? (Cross-Site Scripting) - 자바스크립트 공격(단순한 공격 - 반복문으로 alert 출력시키기 등)
// 웹 애플리케이션에서 일어나는 취약점으로 관리자가 아닌 권한이 없는 사용자가 웹 사이트에 스크립트를 삽입하는 공격 기법입니다.
// Lucy XSS Filter를 사용하면 이 공격을 막을 수 있다.

// CSRF ( Cross Site Request Forgery )이란?
// 사용자의 의도와 관계 없이 행해지는 공격 기법, 사용자가 그저 버튼을 눌렀을 뿐인데 
// 해커가 심어놓은 스크립트에 의해 내 계정의 인증과정을 거쳐서 나의 의도와 관계없이 내 블로그에 광고성 글이 올라간다는 것을 생각해보면 된다.
// 해결방안
// 1. POST 방식으로 중요업무를 처리한다.
// 2. Referer Check -백엔드에서 request의 referer를 확인하여 도메인이 일치하는지 검증하는 방법이다. 
//     일반적으로는 Referer 체크 만으로도 대부분의 CSRF 공격을 막아낼 수 있다
// 3. CSRF Token 사용 - CSRF Token은 임의의 난수를 생성하고 세션에 저장한다. 
//     백엔드에서는 요청을 받을 때 마다 세션에 저장된 토큰값과 요청 파라미터에 전달된 토큰 값이 같은지 검사한다.










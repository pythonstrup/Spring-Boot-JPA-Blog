let index = {
	init: function() {
		$("#btn-save").on("click", ()=>{ // 화살표 함수는 this를 바인딩하기 위해서 사용한다!!
			this.save();
		});
		$("#btn-update").on("click", ()=>{ 
			this.update();
		});
	},
	
	save: function() {
		//alert("user의 save함수 호출됨.");
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		//console.log(data);
		
		
		// 회원가입 시 ajax를 사용하는 2가지 이유
		// 1. 요청에 대한 응답을 html이 아닌 데이터(JSON)를 받기 위해서!!
		// 클라이언트는 브라우저를 통해 요청을 한다. 반면 서버는 html(브라우저가 이해할 수 있는 언어)로 응답한다.
		// 회원가입을 요청한다고 해보자. 서버에서는 회원가입을 수행하고 대체로 메인화면(html)을 리턴해준다.
		// ***만약 클라이언트가 브라우저가 아니라면???*** 
		// 앱에서 회원가입을 요청하면 서버는 html을 리턴하면 안된다. 앱에서는 html을 이해하지 못하기 때문이다.
		// 대신 data를 리턴해주면 된다. 이렇게 되면 html을 리턴해주는 방식과 data를 리턴해주는 방식, 서버를 2개나 만들어야한다. 
		// ***이럴 바에 data를 리턴해주는 서버로 합치는 것이다.***
		// 브라우저는 데이터를 받고, 메인페이지를 다시 request하는 방식으로, 앱은 데이터만 받고 내부에서 화면 변경을 하는 형식으로 하는 것이다.
		
		// 2. 비동기 통신을 하기 위해서!!
		// 동기 통신을 하면 다운로드, 통신 등 기다려야 하는 작업이 있으면 멈춰버린다.
		// 반면 비동기 처리를 한다면? 기다리는 동안 다른 작업을 수행할 수 있다(순서에 상관없이). 이를 통해 사용자 경험(UX)를 개선할 수 있다.
		// 만약 다운로드 작업이 완료됐다면, 다시 그 다음 작업으로 돌아가야하는데 이것을 callback이라고 한다.
		// 하던 일을 하던 도중에 callback을 할 수도 있고, 일을 쉬다가 callback을 할 수도 있다.
		
		// ajax 호출 시 default가 비동기 호출
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
		$.ajax({
			// 회원가입 수행 요청
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=uft-8", // body 데이터가 어떤 타입인지(MIME)
			dataType: "json" // 서버로부터 응답이 왔을 때 기본적으로 모든 데이터는 문자열임. 만약 생긴 게 json이라면 => javascript 오브젝트로 바꾸어줌.
			// 이제 버전이 업그레이드되어서 "json"이 defalut 값이 됨.
		}).done(function(response) {
			if (response.status === 500){
				// 아이디 중복 처리
				alert("회원가입이 실패했습니다.");
			} else {
				// 정상일 시
				alert("회원가입이 완료되었습니다.");
				location.href="/";	
			}
			
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		});
	},
	
	
	update: function() {
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		$.ajax({
			// 회원 수정 요청
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=uft-8", // body 데이터가 어떤 타입인지(MIME)
			dataType: "json" 
		}).done(function(response) {
			// 정상일 시
			alert("회원수정이 완료되었습니다.");
			console.log(response);
			location.href="/";
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		}); 
	},
}

index.init();


// 전통적인 로그인방식
	/*$("#btn-login").on("click", ()=>{ // 화살표 함수는 this를 바인딩하기 위해서 사용한다!!
			this.login();
		});*/

	/*login: function() {
		//alert("user의 save함수 호출됨.");
		let data = {
			username: $("#username").val(),
			password: $("#password").val()
		};

		$.ajax({
			type: "POST",
			url: "/api/user/login",
			data: JSON.stringify(data), // http body 데이터
			contentType: "application/json; charset=uft-8", // body 데이터가 어떤 타입인지(MIME)
			dataType: "json" // 서버로부터 응답이 왔을 때 기본적으로 모든 데이터는 문자열임. 만약 생긴 게 json이라면 => javascript 오브젝트로 바꾸어줌.
			// 이제 버전이 업그레이드되어서 "json"이 defalut 값이 됨.
		}).done(function(response) {
			// 정상일 시
			alert("로그인이 완료되었습니다.");
			location.href="/";
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		}); 
	}*/
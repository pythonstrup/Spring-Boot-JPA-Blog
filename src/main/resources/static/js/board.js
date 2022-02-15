let index = {
	init: function() {
		$("#btn-save").on("click", ()=>{
			this.save();
		});
		$("#btn-update").on("click", ()=>{
			this.update();
		});
		$("#btn-delete").on("click", ()=>{
			this.deleteById();
		});
	},
	
	save: function() {
		//alert("board의 save함수 호출됨.");
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		
		
		$.ajax({
			// 글쓰기 수행 요청
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data), 
			contentType: "application/json; charset=uft-8", 
			dataType: "json" 
		}).done(function(response) {
			// 정상일 시
			alert("글쓰기가 완료되었습니다.");
			location.href="/";
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		}); 
	},
	
	update: function() {
		let id = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val()
		};
		
		
		$.ajax({
			// 글쓰기 수행 요청
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data), 
			contentType: "application/json; charset=uft-8", 
			dataType: "json" 
		}).done(function(response) {
			// 정상일 시
			alert("글수정이 완료되었습니다.");
			location.href="/";
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		}); 
	},
	
	deleteById: function() {
		let id = $("#id").text();
		
		$.ajax({
			// 삭제 요청
			type: "DELETE",
			url: "/api/board/"+id,
			dataType: "json" 
		}).done(function(response) {
			// 정상일 시
			alert("삭제가 완료되었습니다.");
			location.href="/";
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		}); 
	},
}

index.init();

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
		
		$("#btn-reply-save").on("click", ()=>{
			this.replySave();
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
	
	replySave: function() {
		let data = {
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};
		
		$.ajax({
			// 댓글쓰기 수행 요청
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,
			data: JSON.stringify(data), 
			contentType: "application/json; charset=uft-8", 
			dataType: "json" 
		}).done(function(response) {
			// 정상일 시
			alert("댓글작성이 완료되었습니다.");
			location.href=`/board/${data.boardId}`;
		}).fail(function(error) {
			// 실패할 시
			alert(JSON.stringify(error));
		}); 
	},
	
	replyDelete: function(boardId, replyId) {
		$.ajax({
			// 댓글 삭제 수행 요청
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`, 
			dataType: "json" 
		}).done(function(response) {
			// 정상일 시
			alert("댓글삭제가 완료되었습니다.");
			location.href=`/board/${boardId}`;
		}).fail(function(error) {
			// 실패할 시
			console.log(JSON.stringify(error))
			alert(JSON.stringify(error));
		}); 
	},
}

index.init();

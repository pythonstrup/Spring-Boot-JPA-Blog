<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp" %>
	<div class="container">
	
		<form>
			<div class="form-group">
				<label for="title">Title</label> 
				<input type="text" name="title" class="form-control" placeholder="Enter title" id="title">
			</div>
			
			<div class="form-group">
			  <label for="comment">Content</label>
			  <textarea class="form-control summernote" rows="5" id="content"></textarea>
			</div>
		</form>
		
		<button id="btn-save" class="btn btn-primary">글쓰기완료</button>
	</div>
	
	<script>
      $('.summernote').summernote({
        tabsize: 2,
        height: 300
      });
    </script>
	
<script src="/js/board.js"></script>
<script src="https://code.jquery.com/jquery-3.4.1.js"></script>
<%@ include file="../layout/footer.jsp" %>


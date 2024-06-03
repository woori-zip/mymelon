<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>updateForm.jsp</title>
	<style>
		*{ font-family: gulim; font-size: 24px; }
	</style> 
	<link href="../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="title">미디어 그룹 수정</div>
	.
	<form name="frm" method="post" action="update.do">
	<input type="hidden" name="mediagroupno" value="${mediagroupDto.mediagroupno}">
		<table class='table'>
		    <tr>
		        <th>미디어 그룹 제목</th>
		        <td><input type='text' name='title' size='50' value="${mediagroupDto.title}"></td>
		    </tr>
	    </table>
	    
   	    <div class='bottom'>
	      	<input type='submit' value='등록'>
	        <input type='button' value='취소' onclick="location.href='list.do'">
	    </div>
	</form>
</body>
</html>
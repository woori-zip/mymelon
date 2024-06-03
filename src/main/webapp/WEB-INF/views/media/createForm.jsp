<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>createForm.jsp</title>
	<link href="../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>
	<div class="title">음원 등록</div>
	<form name="mediafrm" method="post" action="create.do" enctype="multipart/form-data">
	<input type="hidden" name="mediagroupno" value="${requestScope.mediagroupno}"> <!-- 부모글 번호 -->
		<table>
		    <tr>
		        <th>제목</th>
		        <td><input type='text' name='title' size='50'></td>
		    </tr>
		    <tr>
		        <th>포스터</th>
		        <td><input type='file' name='posterMF' size='50'></td>
		    </tr>
		    <tr>
		        <th>미디어 파일</th>
		        <td><input type='file' name='filenameMF' size='50'></td>
		    </tr>
		  
	    </table>
	    
   	    <div class='bottom'>
	      	<input type='submit' value='음원등록'>
	        <input type='button' value='음원목록' onclick="location.href='list.do?mediagroupno=${mediagroupno}'">
	        <input type='button' value='HOME'  onclick="location.href='/home.do'">
	    </div>
	</form>
</body>
</html>
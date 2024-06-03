<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri ="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>list.jsp</title>
<style>
   *{font-family: gulim; font-size: 24px; }
</style>
<link href="../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>

	<div class="title">음원 목록</div>
	<div class="content">
		<p>																<%-- mav.addObject("mediagroupno",mediagroupno); --%>							
			<input type="button" value="음원등록" onclick="location.href='create.do?mediagroupno=${mediagroupno}'">
			<input type="button" value="HOME" onclick="location.href='/home.do'"> <!-- 절대경로 -->
		</p>
	</div>	
	
	<table class="table">
	<tr>
	   <th>번호</th>
	   <th>제목</th>
	   <th>등록일</th>
	   <th>음원파일명</th>
	   <th>파일크기</th>
	   <th>수정/삭제</th>
	</tr>
	
	<c:forEach var="mediaDto" items="${list}">
		<tr>
			<td>${mediaDto.mediano}</td>
			<td><a href="read.do?mediano=${mediaDto.mediano}">${mediaDto.title}</a></td>
			<td>${fn:substringBefore(mediaDto.rdate," ")}</td>
			<td>
				${mediaDto.filename}
			</td>
			<td>
				<c:set var="filesize" value="${fn:substringBefore(mediaDto.filesize/1024,'.')}"></c:set>
				${filesize}KB
			</td>
			<td>
				<input type="button" value="수정" onclick="location.href='update.do?mediano=${mediaDto.mediano}'">
				<input type="button" value="삭제" onclick="location.href='delete.do?mediano=${mediaDto.mediano}'">
			</td>
		</tr>
	</c:forEach>
	
	</table>

	

</body>
</html>
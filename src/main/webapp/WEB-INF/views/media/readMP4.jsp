<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib prefix="c" uri ="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri ="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>readMP3.jsp</title>
	<style>
   *{font-family: gulim; font-size: 24px; }
	</style>
	<link href="../css/style.css" rel="stylesheet" type="text/css">
</head>
<body>

	<div class="title">MP4 	듣기</div>
	
	<div class="content">
		<p><stron>${mediaDto.title}</stron></p>
		<img src="../storage/${mediaDto.poster}" width="400"><br>
		<audio src="../storage/${mediaDto.filename}" controls></audio>
	</div>	
	
	<div class="bottom">
		<input type="button" value="음원목록" onclick="location.href='list.do?mediagroupno=${mediaDto.mediagroupno}'">
		<input type="button" value="HOME"  onclick="location.href='/home.do'">
	</div>

	

</body>
</html>
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
	<div class="title">미디어 그룹 목록</div>
	<div class="content">
	   <input type="button" value="그룹등록" onclick="location.href='create.do'"> <!--get방식 -->
	</div>
      
	<c:if test ="${requestScope.count==0}">
	   <table><tr><td>게시판에 글 없음</td></tr></table>
	</c:if>
	
	<c:if test="${requestScope.count>0}">
		<table class="table">
		<tr>
		   <th>번호</th>
		   <th>그룹번호</th>
		   <th>그룹제목</th>
		   <th>수정/삭제</th>
		</tr>
		
		<%-- ${list}는 MediagroupCont클래스의 list()함수에서 mav.addObject("list")를 가리킴 --%>
		<c:forEach items="${list}" var="mediagroupDto" varStatus="vs">
		   <tr>
		      <td>${vs.count}</td>
		      <td>${mediagroupDto.mediagroupno}</td>
		      <%-- <td>${mediagroupDto.title}</td> 에 media_list 로 들어가는 링크 붙임--%>
		      <td><a href="../media/list.do?mediagroupno=${mediagroupDto.mediagroupno}">${mediagroupDto.title}</a></td>

		      <td>
		         <input type="button" value="수정" onclick="location.href='update.do?mediagroupno=${mediagroupDto.mediagroupno}'">
		         <input type="button" value="삭제" onclick="location.href='delete.do?mediagroupno=${mediagroupDto.mediagroupno}'">
		      </td>
		   </tr>
		</c:forEach>
		</table>
		
	<!-- 페이지 리스트 시작 -->
	<c:if test="${requestScope.count>0}">
		<!-- ${requestScope.count} or ${count} // MediagropuCont 에서 mav.addObject 를 통해 request 에 올린 변수들 -->
		<c:set var="totalPage" value="${requestScope.totalPage}"></c:set>
		<c:set var="startPage" value="${requestScope.startPage}"></c:set>
		<c:set var="endPage"   value="${requestScope.endPage}"></c:set>
		
		<div class="content">
			<c:if test="${endPage>totalPage}">
				<c:set var="endPage" value="${totalPage+1}"></c:set>
			</c:if>
			
			<!--<c:out value="${startPage}"/>-->

			<c:if test="${startPage>0}">
				<a href="./list.do?pageNum=${startPage}">[이전]</a>
			</c:if>
			
			<c:forEach var="i" begin="${startPage+1}" end="${endPage-1}">
				<c:choose>
					<c:when test="${pageNum==i}"><span>${i}</span></c:when>
					<c:when test="${pageNum!=i}"><a href="./list.do?pageNum=${i}">[${i}]</a></c:when>
				</c:choose>
			</c:forEach>
<%-- 			<c:out value="${endPage}"/>
			<c:out value="${totalPage}"/> --%>
			<c:if test="${endPage<=totalPage}">
				<a href="./list.do?pageNum=${endPage}">[다음]</a>
			</c:if>
		</div>
	</c:if>
	<!-- 페이지 리스트 끝 -->
	</c:if>
</body>
</html>
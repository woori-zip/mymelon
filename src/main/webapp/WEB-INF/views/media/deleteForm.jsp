<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>deleteForm.jsp</title>
	<style>
		*{ font-family: gulim; font-size: 24px; }
	</style> 
	<link href="../css/style.css" rel="stylesheet" type="text/css">
	<script>
		function deleteCheck(){
			let message="파일을 삭제하시겠습니까?";
			if(confirm(message)){
				return true;
			}else{
				return false;
			}
		}//updateCheck() end
	</script>
</head>
<body>
	<div class="title">미디어 그룹 삭제</div>
	
	<form name="frm" method="post" action="delete.do" onsubmit="return deleteCheck()">
		<input type="hidden" name="mediagroupno" value="${mediagroupno}">
		<input type="hidden" name="mediano" value="${mediano}">
		<div class="content">
			<p>음원을 삭제하시겠습니까?</p>
			<p>※ 관련 미디어 파일(mp3, mp4)도 전부 삭제됩니다.</p>
		</div>
		<div class='bottom'>
		    <input type='submit' value='삭제'>
		    <input type='button' value='취소' onclick="javascript:history.back()">
		</div>
	</form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<jsp:include page="/fragment/header.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Password</title>
<link rel="Shortcut Icon" type="image/x-icon" href="${pageContext.request.contextPath}/image/logo.jpg">
</head>
<body>
		<section class="software sm-parallax-window" data-parallax="scroll" style="margin:0 0; padding:0 0;">
	<div style="font-family: '微軟正黑體';">

		<div class="container">
			<div class="row">
				<div class="col-md-12 sm-text">
					<h1 style="color:#fff; text-shadow:1px 1px 8px #000;"><strong>Update Password</strong></h1>
				</div>
				<table class="table table-hover col-md-12 sm-text1">
					<tr>
						<td>
							<h4 style="font-size: 30px;">Member ID</h4>
						</td>
						<td>
							<h4 style="font-size: 30px;">
								${LoginOK.userID}
							</h4>
						</td>
					</tr>
				</table>
				<form role="form" method="POST"
					action="<c:url value='/_01_updatePasswordController.do'/>"
					id="/_01_updateEmailController.do" style="font-size: 30px;">
					<div class="form-group">
						<label for="exampleInputEmail1" style="color:#fff; text-shadow:1px 1px 8px #000;">Old Password</label><font
							style="color: red;">&nbsp;&nbsp;${MsgMap.errCheckPassword}
							${MsgMap.errPassword} ${MsgMap.errPassword}</font> <input
							type="password" name="checkpassword"
							value="${param.checkpassword}" class="form-control"
							placeholder="Insert your old password" style="font-size: 30px;">
					</div>
					<div class="form-group">
						<label for="exampleInputPassword1" style="color:#fff; text-shadow:1px 1px 8px #000;">New password</label><font
							style="color: red;">&nbsp;&nbsp;${MsgMap.errpassword}</font> <input
							type="password" name="password" value="${param.password}"
							class="form-control" placeholder="Insert your new password"
							style="font-size: 30px;">
					</div>
					<div>
						<div class="g-recaptcha form-group"
							data-sitekey="6LcLfQcUAAAAACCrN4x3uNDWUi-zoiJLyco9BnfN"></div>
						<font style="color: red;">&nbsp;&nbsp;${MsgMap.errorRecaptcha}</font>
					</div>
					<div id="btnArea" class="modal-footer">
						 <input
							type="submit" name="submit" id="submit" value="Submit"
							class="btn btn-success" style="font-size: 30px;" />
					</div>
				</form>
				<font style="color: red;">&nbsp;&nbsp;${MsgMap.errMessage}</font>
			</div>
		</div>		
			</div>
	<jsp:include page="/fragment/newfooter.jsp" />
	</section>

	<script>
		$('.sm-parallax-window')
				.parallax(
						{
							imageSrc : '${pageContext.request.contextPath}/image/blue-sky.jpg'
						});
	</script>
	<script src='https://www.google.com/recaptcha/api.js' async defer></script>
</body>
</html>
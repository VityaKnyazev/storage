<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Simple product list</title>
</head>
<body>
	<h1>List of products</h1>
	<c:if test="${!goods.isEmpty()}">
		<c:forEach items="${goods}" var="good">
			<div>
				<p>${good.getId()}</p>
				<ul>
					<li>Наименование продукта: ${good.getName()}</li>
					<li>Сорт продукта: ${good.getSort()}</li>
					<li>Описание проукта: ${good.getDescription()}</li>
					<li>Производитель: ${good.getProducer().getName()}</li>
					<li>Категория: ${good.getCategory().getName()}</li>
				</ul>
			</div>
		</c:forEach>
	</c:if>
	<c:if test="${goods.isEmpty()}">
		<div>
			<p>Empty...</p>
		</div>
	</c:if>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
  <h1>hello가나다</h1>
  <ul>
    <c:forEach items="${list}" var="list">
        <li>
            <img alt="없음" src="${list.image}">
            <p>제품 코드 = ${list.seq}</p>
            <p>제품 코드 = ${list.name}</p>
            <p>----------------------------</p>
        </li>
    </c:forEach>
  </ul>
</body>
</html>
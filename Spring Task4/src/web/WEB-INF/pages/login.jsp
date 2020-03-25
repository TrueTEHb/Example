<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div>
    <h1>Authorisation</h1>
    <div>
        <c:if test="${msg != null}">
            <h2>
                Wrong username or password
            </h2>
        </c:if>
    </div>
    <div>
        <c:if test="${message != null}">
            <h2>
                <c:out value="${message}"></c:out>
            </h2>
        </c:if>
    </div>
    <form method="post" action="/login">
        Login: <input type="text" name="j_username"/>
        Password: <input type="password" name="j_password"/>
        <input type="submit"/>
        <p></p>
        <a href="/new">Add new user</a>
    </form>
</div>
</body>
</html>
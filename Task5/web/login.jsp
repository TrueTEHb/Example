<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Login</title>
</head>
<body>
<div>
    <h1>Authorisation</h1>
    <form method="post" action="/login">
        Login: <input type="text" name="name"/>
        Password: <input type="password" name="password"/><br>
        <input type="submit" value="Save">
        <p></p>
        <input type="submit" value="Registration" name="/new">
    </form>
</div>
</body>
</html>
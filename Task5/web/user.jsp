<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>User menu</title>
</head>
<body>
<table border="1" cellpadding="5">
    <h2>
        Your account
    </h2>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Password</th>
        <th>Money</th>
        <th>Role</th>
        <th>Actions</th>
    </tr>
    <c:if test="${user != null}">
        <h2>
            Hello ${user.name}! You are logged like ${user.role}
        </h2>
        <tr>
            <td><c:out value="${user.id}"/></td>
            <td><c:out value="${user.name}"/></td>
            <td><c:out value="${user.password}"/></td>
            <td><c:out value="${user.money}"/></td>
            <td><c:out value="${user.role}"/></td>
            <td>
                <a href="/edit?id=<c:out value="${user.id}"/>">Edit</a>
                <a href="/delete?id=<c:out value="${user.id}"/>">Delete</a>
            </td>
        </tr>
        <p>
            <a href="/logout?id=<c:out value="${user.id}"/>">Log out</a>
        </p>
    </c:if>
</table>
</body>
</html>
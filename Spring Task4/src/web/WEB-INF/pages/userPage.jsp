<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>User page</title>
</head>
<body>
<table border="1" cellpadding="5">
    <c:if test="${role.equals('ADMIN')}">
        <a href="/admin/list">List all people</a>
        <br>
    </c:if>
    <h2>
        User account
    </h2>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Password</th>
        <th>Role</th>
        <c:if test="${role.equals('ADMIN')}">
            <th>Actions</th>
        </c:if>

    </tr>
    <c:if test="${user != null}">
        <h2>
            Hello ${user.name}!
        </h2>
        <tr>
            <td><c:out value="${user.id}"/></td>
            <td><c:out value="${user.name}"/></td>
            <td><c:out value="${user.password}"/></td>
            <td><c:out value="${role}"/></td>
            <c:if test="${role.equals('ADMIN')}">
                <td>
                    <a href="/admin/edit?id=<c:out value="${user.id}"/>">Edit</a>
                    <a href="/admin/delete?id=<c:out value="${user.id}"/>">Delete</a>
                </td>
            </c:if>

        </tr>
        <p>
        <form action="/logout" method="post">
            <input type="submit" value="Sign out">
        </form>
        </p>
    </c:if>
</table>
</body>
</html>

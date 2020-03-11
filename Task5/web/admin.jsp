<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin menu</title>
</head>
<body>

<table border="1" cellpadding="5">
    <h2>
        List users
    </h2>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Password</th>
        <th>Money</th>
        <th>Role</th>
        <th>Actions</th>
    </tr>
    <c:if test="${people != null}">
        Hello ${user.name}! You are are logged like ${user.role}
    </c:if>
    <h2>
    </h2>
    <c:forEach var="people" items="${people}">
        <tr>
            <td><c:out value="${people.id}"/></td>
            <td><c:out value="${people.name}"/></td>
            <td><c:out value="${people.password}"/></td>
            <td><c:out value="${people.money}"/></td>
            <td><c:out value="${people.role}"/></td>
            <td>
                <a href="/edit?id=<c:out value="${people.id}"/>">Edit</a>
                <a href="/delete?id=<c:out value="${people.id}"/>">Delete</a>
            </td>
        </tr>
    </c:forEach>
    <p>
        <a href="/logout?id=<c:out value="${user.id}"/>">Log out</a>
    </p>
</table>
</body>
</html>
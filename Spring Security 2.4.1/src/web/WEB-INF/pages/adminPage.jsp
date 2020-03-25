<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Admin page</title>
</head>
<body>
<table border="1" cellpadding="5">
    <h2>
        List people
    </h2>
    <h2>
        Hello ${u_name}!
    </h2>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Password</th>
        <th>Role</th>
        <th>Actions</th>
    </tr>

    <c:forEach var="user" items="${people}">
        <tr>
            <td><c:out value="${user.id}"/></td>
            <td><c:out value="${user.name}"/></td>
            <td><c:out value="${user.password}"/></td>
            <td><c:forEach var="roles" items="${user.roles}">
                <c:out value="${roles.value}"/>
            </c:forEach>
            </td>
            <td>
                <a href="/admin/edit?id=<c:out value='${user.id}' />">Edit</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="/admin/delete?id=<c:out value='${user.id}' />">Delete</a>
            </td>
        </tr>
    </c:forEach>

    <p>
    <form action="/logout" method="post">
        <input type="submit" value="Sign out">
    </form>
    </p>
</table>
</body>
</html>

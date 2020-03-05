<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>People List</title>
</head>
<body>
<a href="/new">Add new user</a>
<p></p>
<a href="/list">List all people</a>
<p></p>
<table border="1" cellpadding="5">
    <h2>
        List people
    </h2>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Password</th>
        <th>Money</th>
        <th>Actions</th>
    </tr>
    <c:forEach var="user" items="${people}">
        <tr>
            <td><c:out value="${user.id}"/></td>
            <td><c:out value="${user.name}"/></td>
            <td><c:out value="${user.password}"/></td>
            <td><c:out value="${user.money}"/></td>
            <td>
                <a href="/edit?id=<c:out value='${user.id}' />">Edit</a>
                &nbsp;&nbsp;&nbsp;&nbsp;
                <a href="/delete?id=<c:out value='${user.id}' />">Delete</a>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>

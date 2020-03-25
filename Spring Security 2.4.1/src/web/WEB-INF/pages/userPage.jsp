<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>User page</title>
</head>
<body>
<table border="1" cellpadding="5">

    <h2>
        User account
    </h2>
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Password</th>
        <th>Role</th>

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

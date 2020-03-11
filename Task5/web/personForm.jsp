<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Edit</title>
</head>
<body>
<c:if test="${user != null}">
<form action="update" method="post"></c:if>
    <c:if test="${user == null}">
    <form action="insert" method="post">

        </c:if>
            <table border="1" cellpadding="5">
                <h2>
                    <c:if test="${user != null}">
                        Edit Person
                    </c:if>
                    <c:if test="${user == null}">
                        Add New Person
                    </c:if>
                </h2>
                <c:if test="${user != null}">
                    <input type="hidden" name="id" value="<c:out value='${user.id}' />"/>
                </c:if>
                <tr>
                    <th>Name:</th>
                    <td>
                        <input type="text" name="name"
                               value="<c:out value='${user.name}' />"/>
                    </td>
                </tr>
                <tr>
                    <th>Password:</th>
                    <td>
                        <input type="text" name="password"
                               value="<c:out value='${user.password}' />"/>
                    </td>
                </tr>
                <tr>
                    <th>Money:</th>
                    <td>
                        <input type="number" name="money"
                               value="<c:out value='${user.money}' />"/>
                    </td>
                </tr>
                <c:if test="${user != null}">
                    <tr>
                        <th>Role:</th>
                        <td>
                            <input type="text" name="role" value="<c:out value='${user.role}' />"/>
                        </td>
                    </tr>
                </c:if>
            </table>
            <c:if test="${user != null}">
                <input type="submit" value="Update" name="/update">
            </c:if>
            <c:if test="${user == null}">
                <input type="submit" value="Add" name="/insert">
            </c:if>
    </form>
</body>
</html>
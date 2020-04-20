/**
 * Во время авторизации заполнить строку данными авторизованного пользователя
 * */
$(document).ready(function () {
    activeTab();
})

function activeTab() {
    $.ajax({
        type: "GET",
        url: "/user/list",
        success: function (data, status, param) {
            var u_email = data.user.email;
            var u_roles = data.role;

            $("#user_info").html("<strong class='text-white'>" + u_email + "</strong>" +
                "<span class='text-white'> with roles: </span>" +
                "<span class='text-white'>" + u_roles + "</span>"
            );
            var user_data = '';
            user_data += '<tr>';
            user_data += '<td>' + data.user.id + '</td>';
            user_data += '<td>' + data.user.firstName + '</td>';
            user_data += '<td>' + data.user.lastName + '</td>';
            user_data += '<td>' + data.user.age + '</td>';
            user_data += '<td>' + data.user.email + '</td>';
            user_data += '<td>' + data.user.roles[0].value + '</td>';
            $('#user_table_info').append(user_data);
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}
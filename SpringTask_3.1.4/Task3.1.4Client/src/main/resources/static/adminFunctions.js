/**
 * Во время авторизации заполнить строку данными авторизованного пользователя
 * */
$(document).ready(function () {
    $(".tab-pane").on("click", "button", function (event) {
        var button = $(this).attr('data-target');
        var id = $(this).data('content');

        //-----------------------------------------EDIT---------------------------------------------------------------//

        if (button == "edit_button") {
            event.preventDefault();
            var modal_data = {
                id: $("#id-user" + id).val(),
                firstName: $("#firstName-edit" + id).val(),
                lastName: $("#lastName-edit" + id).val(),
                age: $("#age-edit" + id).val(),
                email: $("#email-edit" + id).val(),
                password: $("#password-edit" + id).val(),
                roles: $("#role-edit" + id).children("option:selected").val()
            }
            $.ajax({
                type: "PUT",
                contentType: "application/json",
                url: "admin/" + id,
                async: false,
                data: JSON.stringify(modal_data),
                success: function (data) {
                    var user_data = '';

                    user_data += '<td>' + data.user.id + '</td>';
                    user_data += '<td>' + data.user.firstName + '</td>';
                    user_data += '<td>' + data.user.lastName + '</td>';
                    user_data += '<td>' + data.user.age + '</td>';
                    user_data += '<td>' + data.user.email + '</td>';
                    if (data.user.roles.length == 2) {
                        user_data += '<td>' + data.user.roles[0].value + ' ' + data.user.roles[1].value + '</td>';
                    } else {
                        user_data += '<td>' + data.user.roles[0].value + '</td>';
                    }

                    //edit button
                    user_data += '<td> <button type="button" class="btn btn-info" ' +
                        'data-toggle="modal" ' +
                        'id="ed_button" data-content="' + id + '"' +
                        'data-target="#editModal' + id + '">Edit </button></td>';

                    //delete button
                    user_data += '<td> <button type="button" class="btn btn-danger" ' +
                        'id="del_button" ' +
                        'data-toggle="modal" ' +
                        'data-target="#deleteModal' + id + '"> Delete</button> </td>';

                    $("#tr_id" + id).html(user_data);
                    $("#editModal" + id).modal('hide');

                },
                error: function (e) {
                    console.log("ERROR: ", e);
                }
            });
        }

        //-----------------------------------------DELETE-------------------------------------------------------------//

        if (button == "delete_button"){
            event.preventDefault();
            /*var id = $("#id-user" + id).val();*/
            $.ajax({
                type: "DELETE",
                contentType: "application/json",
                url: "admin/" + id,
                async: false,
                data: JSON.stringify(id),
                success: function (header, body, status) {
                    if (status.status == 200){
                        $("#tr_id" + id).remove();
                        $("#deleteModal" + id).modal('hide');
                        return activeTab();
                    }else if (status.status == 302){
                        $("#deleteModal" + id).modal('hide');
                        window.location.href = '/logout';
                    }else if (status.status == 400){
                        console.log("ERROR: " + "bad request");
                    }

                },
                error: function (e) {
                    console.log("ERROR: ", e);
                }
            });
        }

        //-----------------------------------------CREATE-------------------------------------------------------------//

        if (button == "create_button"){
            //event.preventDefault();
            var modal_data = {
                firstName: $("#first-name").val(),
                lastName: $("#last-name").val(),
                age: $("#age").val(),
                email: $("#email").val(),
                password: $("#password").val(),
                role: $("#role").children("option:selected").val()
            }
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "admin/",
                async: false,
                data: JSON.stringify(modal_data),
                success: function (header, body, status) {
                    if (status.status == 200){
                        alert("User was added successfully");
                        return activeTab();
                    }
                    if (status == 400){
                        alert("User creation error");
                        return activeTab();
                    }
                },
                error: function (e) {
                    console.log("ERROR: ", e);
                }
            });
        }
    })
})


/**
 * Во время авторизации заполнить строку данными авторизованного пользователя
 * */
$(document).ready(function () {
    $('.nav-tabs a[href="#users-table"]').on('shown.bs.tab', activeTab());
})

function activeTab() {
    $.ajax({
        type: "GET",
        url: "/admin/list",
        success: function (data) {
            var user_data = '';
            var user_role = '';
            $.each(data, function (key, value) {
                if (key == "people") {
                    $('#some').empty();
                    $.each(data.people, function (key1, value1) {
                        user_data = '';
                        user_role = '';

                        user_data += '<tr id="tr_id' + value1.id + '">';
                        user_data += '<td>' + value1.id + '</td>';
                        user_data += '<td>' + value1.firstName + '</td>';
                        user_data += '<td>' + value1.lastName + '</td>';
                        user_data += '<td>' + value1.age + '</td>';
                        user_data += '<td>' + value1.email + '</td>';
                        if (value1.roles.length == 2) {
                            user_data += '<td>' + value1.roles[0].value + ' ' + value1.roles[1].value + '</td>';
                            user_role = value1.roles[0].value + ' ' + value1.roles[1].value;
                        } else {
                            user_data += '<td>' + value1.roles[0].value + '</td>';
                            user_role = value1.roles[0].value;
                        }

                        var edit_modal_window = '';
                        var delete_modal_window = '';

                        edit_modal_window +=
                            '<div id="editModal' + value1.id + '" class="modal fade" tabindex="-1" role="dialog"\n' +
                            '                                                     aria-labelledby="ModalLabel" aria-hidden="true">\n' +
                            '                                                        <div class="modal-dialog" role="document">\n' +
                            '                                                            <div class="modal-content">\n' +
                            '                                                                <div class="modal-header">\n' +
                            '                                                                    <h5 class="modal-title" id="editModalLabel">Edit\n' +
                            '                                                                        user</h5>\n' +
                            '                                                                    <button type="button" class="close"\n' +
                            '                                                                            data-dismiss="modal"\n' +
                            '                                                                            aria-label="Close">\n' +
                            '                                                                        <span aria-hidden="true">&times;</span>\n' +
                            '                                                                    </button>\n' +
                            '                                                                </div>\n' +
                            '                                                                <div class="modal-body">\n' +
                            '                                                                    <!--id-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="id-user"\n' +
                            '                                                                               class="font-weight-bold ">ID</label>\n' +
                            '                                                                        <input type="number"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="id-user' + value1.id + '"\n' +
                            '                                                                               aria-describedby="id-user" readonly\n' +
                            '                                                                               name="id"\n' +
                            '                                                                               value="' + value1.id + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--first name-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="firstName-edit"\n' +
                            '                                                                               class="font-weight-bold ">\n' +
                            '                                                                            First name\n' +
                            '                                                                        </label>\n' +
                            '                                                                        <input type="text"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="firstName-edit' + value1.id + '"\n' +
                            '                                                                               aria-describedby="firstName-edit"\n' +
                            '                                                                               name="firstName"\n' +
                            '                                                                               value="' + value1.firstName + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--last name-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="lastName-edit"\n' +
                            '                                                                               class="font-weight-bold">Last\n' +
                            '                                                                            name</label>\n' +
                            '                                                                        <input type="text"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="lastName-edit' + value1.id + '"\n' +
                            '                                                                               aria-describedby="lastName-edit"\n' +
                            '                                                                               name="lastName"\n' +
                            '                                                                               value="' + value1.lastName + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--age-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="age-edit"\n' +
                            '                                                                               class="font-weight-bold">Age</label>\n' +
                            '                                                                        <input type="number"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="age-edit' + value1.id + '"\n' +
                            '                                                                               aria-describedby="age-edit"\n' +
                            '                                                                               name="age"\n' +
                            '                                                                               value="' + value1.age + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--email-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="email-edit"\n' +
                            '                                                                               class="font-weight-bold">Email</label>\n' +
                            '                                                                        <input type="email"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="email-edit' + value1.id + '"\n' +
                            '                                                                               aria-describedby="email-edit"\n' +
                            '                                                                               name="email"\n' +
                            '                                                                               value="' + value1.email + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--password-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="password-edit"\n' +
                            '                                                                               class="font-weight-bold">Password</label>\n' +
                            '                                                                        <input type="password"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="password-edit' + value1.id + '"\n' +
                            '                                                                               name="password"\n' +
                            '                                                                               aria-describedby="password-edit">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--role-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="role-edit"\n' +
                            '                                                                               class="font-weight-bold">Role</label>\n' +
                            '                                                                        <select multiple\n' +
                            '                                                                                class="form-control form-control-sm"\n' +
                            '                                                                                size="2"\n' +
                            '                                                                                name="role-edit"\n' +
                            '                                                                                id="role-edit' + value1.id + '">\n' +
                            '                                                                            <option>USER</option>\n' +
                            '                                                                            <option>ADMIN</option>\n' +
                            '                                                                        </select>\n' +
                            '                                                                    </div>\n' +
                            '                                                                </div>\n' +
                            '                                                                <div class="modal-footer">\n' +
                            '                                                                    <button type="button" class="btn btn-secondary"\n' +
                            '                                                                            data-dismiss="modal">Close\n' +
                            '                                                                    </button>\n' +
                            '                                                                    <button type="button" data-content="' + value1.id + '" data-target="edit_button" id="edit_button"\n' +
                            '                                                                             class="btn btn-primary">Edit\n' +
                            '                                                                    </button>\n' +
                            '                                                                </div>\n' +
                            '                                                            </div>\n' +
                            '                                                        </div>\n' +
                            '                                                </div>'

                        //edit button
                        user_data += '<td> <button type="button" class="btn btn-info" ' +
                            'data-toggle="modal"' +
                            'id="ed_button" data-content="' + value1.id + '"' +
                            'data-target="#editModal' + value1.id + '">Edit </button></td>';

                        //--------------------------------------------------------------------------------------//

                        delete_modal_window +=
                            '<div id="deleteModal' + value1.id + '" class="modal fade" tabindex="-1" role="dialog"\n' +
                            '                                                     aria-labelledby="ModalLabel" aria-hidden="true">\n' +
                            '                                                        <div class="modal-dialog" role="document">\n' +
                            '                                                            <div class="modal-content">\n' +
                            '                                                                <div class="modal-header">\n' +
                            '                                                                    <h5 class="modal-title" id="deleteModalLabel">Delete\n' +
                            '                                                                        user</h5>\n' +
                            '                                                                    <button type="button" class="close"\n' +
                            '                                                                            data-dismiss="modal"\n' +
                            '                                                                            aria-label="Close">\n' +
                            '                                                                        <span aria-hidden="true">&times;</span>\n' +
                            '                                                                    </button>\n' +
                            '                                                                </div>\n' +
                            '                                                                <div class="modal-body">\n' +
                            '                                                                    <!--id-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="id-user"\n' +
                            '                                                                               class="font-weight-bold ">ID</label>\n' +
                            '                                                                        <input type="number"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="id-delete'+value1.id+'"\n' +
                            '                                                                               aria-describedby="id-delete" readonly\n' +
                            '                                                                               name="id"\n' +
                            '                                                                               value="' + value1.id + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--first name-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="firstName-delete"\n' +
                            '                                                                               class="font-weight-bold ">\n' +
                            '                                                                            First name\n' +
                            '                                                                        </label>\n' +
                            '                                                                        <input type="text"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="firstName-delete'+value1.id+'"\n' +
                            '                                                                               aria-describedby="firstName-delete" readonly\n' +
                            '                                                                               name="firstName"\n' +
                            '                                                                               value="' + value1.firstName + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--last name-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="lastName-delete"\n' +
                            '                                                                               class="font-weight-bold">Last\n' +
                            '                                                                            name</label>\n' +
                            '                                                                        <input type="text"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="lastName-delete'+value1.id+'"\n' +
                            '                                                                               aria-describedby="lastName-delete" readonly\n' +
                            '                                                                               name="lastName"\n' +
                            '                                                                               value="' + value1.lastName + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--age-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="age-delete"\n' +
                            '                                                                               class="font-weight-bold">Age</label>\n' +
                            '                                                                        <input type="number"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="age-delete'+value1.id+'"\n' +
                            '                                                                               aria-describedby="age-delete" readonly\n' +
                            '                                                                               name="age"\n' +
                            '                                                                               value="' + value1.age + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--email-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="email-delete"\n' +
                            '                                                                               class="font-weight-bold">Email</label>\n' +
                            '                                                                        <input type="email"\n' +
                            '                                                                               class="form-control form-control-sm"\n' +
                            '                                                                               id="email-delete'+value1.id+'"\n' +
                            '                                                                               aria-describedby="email-delete" readonly\n' +
                            '                                                                               name="email"\n' +
                            '                                                                               value="' + value1.email + '">\n' +
                            '                                                                    </div>\n' +
                            '\n' +
                            '                                                                    <!--role-->\n' +
                            '                                                                    <div class="form-group text-center col-6 mx-auto">\n' +
                            '                                                                        <label for="role-delete"\n' +
                            '                                                                               class="font-weight-bold">Role</label>\n' +
                            '                                                                        <select multiple\n' +
                            '                                                                                class="form-control form-control-sm"\n' +
                            '                                                                                size="2"\n' +
                            '                                                                                name="role-delete"\n' +
                            '                                                                                id="role-delete'+value1.id+'" readonly="role-delete">\n' +
                            '                                                                            <option>USER</option>\n' +
                            '                                                                            <option>ADMIN</option>\n' +
                            '                                                                        </select>\n' +
                            '                                                                    </div>\n' +
                            '                                                                </div>\n' +
                            '                                                                <div class="modal-footer">\n' +
                            '                                                                    <button type="button" class="btn btn-secondary"\n' +
                            '                                                                            data-dismiss="modal">Close\n' +
                            '                                                                    </button>\n' +
                            '                                                                    <button type="button" data-content="' + value1.id + '" data-target="delete_button" id="delete_button"\n' +
                            '                                                                             class="btn btn-danger">Delete\n' +
                            '                                                                    </button>\n' +
                            '                                                                </div>\n' +
                            '                                                            </div>\n' +
                            '                                                        </div>\n' +
                            '                                                </div>'

                        //delete button
                        user_data += '<td> <button type="button" class="btn btn-danger" ' +
                            'id="del_button" ' +
                            'data-toggle="modal" data-content="' + value1.id + '" ' +
                            'data-target="#deleteModal' + value1.id + '"> Delete</button> </td>';
                        user_data += '</tr>';

                        $('#some').append(user_data);
                        $('.tab-content').append(edit_modal_window);
                        $('.tab-content').append(delete_modal_window);
                    })

                }
                if (key == "user") {
                    $('#loginUser').empty();
                    var u_email = data.user.email;
                    var u_roles = data.user.roles;

                    $("#auth_info").html("<strong class='text-white'>" + u_email + "</strong>" +
                        "<span class='text-white'> with roles: </span>" +
                        "<span class='text-white'>" +
                        u_roles[0].value + ' ' + u_roles[1].value
                        + "</span>"
                    );

                    user_data = '';
                    user_role = '';

                    user_data += '<tr id="tr_id' + data.user.id + '">';
                    user_data += '<td>' + data.user.id + '</td>';
                    user_data += '<td>' + data.user.firstName + '</td>';
                    user_data += '<td>' + data.user.lastName + '</td>';
                    user_data += '<td>' + data.user.age + '</td>';
                    user_data += '<td>' + data.user.email + '</td>';
                    if (data.user.roles.length == 2) {
                        user_data += '<td>' + data.user.roles[0].value + ' ' + data.user.roles[1].value + '</td>';
                        user_role = data.user.roles[0].value + ' ' + data.user.roles[1].value;
                    } else {
                        user_data += '<td>' + data.user.roles[0].value + '</td>';
                        user_role = data.user.roles[0].value;
                    }
                    $('#loginUser').append(user_data);
                }
            })
        },
        error: function (e) {
            console.log("ERROR: ", e);
        }
    });
}
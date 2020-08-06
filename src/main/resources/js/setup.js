var setupticket = {};

setupticket.module = (function () {
    var showMessage = function() {
        console.log("=========== проверка ===========");
        return false;
    };


    // показать окно редактирования пользователя
    var showUserWindow = function(id, login, password, name, email,depart) {
        AJS.$("#edituserid").val(id);
        AJS.$("#editlogin").val(login);
        AJS.$("#editpass").val(password);
        AJS.$("#editname").val(name);
        AJS.$("#editemail").val(email);
        AJS.$("#editdepart").val(depart);


        if (AJS.$("#mode").val() == "edit") {
            $("#user-edit-dialog .aui-dialog2-header-main").text("Редактировать пользователя");
        };

        if (AJS.$("#mode").val() == "create") {
            $("#user-edit-dialog .aui-dialog2-header-main").text("Создать пользователя");
        };

        AJS.dialog2("#user-edit-dialog").show();
    };


    // открыть окно редактирования пользователя
    var showUserEditWindow = function(userId) {

        if (AJS.$.trim(userId) == "") {
            return false
        }

        AJS.$("#mode").val("edit");

        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/users/getusers/" + userId,
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            // async: false,
            // async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                if ((data != null) && (data.status == "ok")){

                    var userinfo = data.users[0];

                    showUserWindow(userinfo.id, userinfo.login, userinfo.password, userinfo.name, userinfo.email, userinfo.depart);
                }

            },
            error: function(data) {
                var myFlag = AJS.flag({
                    type: 'error',
                    body: 'Ошибка загрузки данных о пользователе',
                });

            },
        });

    };


    // обновить информацию о пользователе в таблице
    var refreshUserInfoInTable = function(id, login, pass, name, email, depart) {

        // придется проходить всю таблицу
        var rows = AJS.$("#ticketsUsers tr");
        var rowId;
        for (var i = 0; i < rows.length; i++) {
            rowId = rows[i].getElementsByClassName("user-column-hidden")[0].innerText;

            console.log("=====");
            console.log(id.toString());
            console.log(rowId);

            if (id.toString() == rowId) {
                rows[i].getElementsByClassName("user-column-login")[0].innerText = login;
                rows[i].getElementsByClassName("user-column-password")[0].innerText = pass;
                rows[i].getElementsByClassName("user-column-fio")[0].innerText = name;
                rows[i].getElementsByClassName("user-column-email")[0].innerText = email;
                rows[i].getElementsByClassName("user-column-podr")[0].innerText = depart;
            }
        }
    };


    // сохранить информацию о пользователе
    var saveUserInfo = function() {
        var jsonObj = {};

        jsonObj.id      = AJS.$("#edituserid").val();
        jsonObj.login   = AJS.$("#editlogin").val();
        jsonObj.pass    = AJS.$("#editpass").val();
        jsonObj.name    = AJS.$("#editname").val();
        jsonObj.email   = AJS.$("#editemail").val();
        jsonObj.depart  = AJS.$("#editdepart").val();


        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/users/updateuser",
            type: 'post',
            dataType: 'json',
            data: JSON.stringify(jsonObj),
            async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                if ((data != null) && (data.status == "ok")){
                    var myFlag = AJS.flag({
                        title: "Сведения обновлены",
                        type: 'success',
                        body: "Данные пользователя " + jsonObj.name + " обновлены",
                        close: "auto"
                    });

                    refreshUserInfoInTable(jsonObj.id, jsonObj.login, jsonObj.pass, jsonObj.name, jsonObj.email, jsonObj.depart);

                    AJS.dialog2("#user-edit-dialog").hide();

                };

                if ((data != null) && (data.status == "error")){
                    var myFlag = AJS.flag({
                        title: "Ошибка",
                        type: 'error',
                        body: data.description,
                        close: "manual"
                    });
                };

                if (data == null){
                    var myFlag = AJS.flag({
                        title: "Ошибка загрузки",
                        type: 'error',
                        body: "что то пошло не так ...",
                        close: "manual"
                    });
                }

            },
            error: function(data) {
                var myFlag = AJS.flag({
                    title: "Ошибка загрузки",
                    type: 'error',
                    body: "что то пошло не так ...",
                });

            },
        });

    };

    // сохранить информацию о пользователе
    var createUserInfo = function() {

        var jsonObj = {};

        jsonObj.id      = "";
        jsonObj.login   = AJS.$("#editlogin").val();
        jsonObj.pass    = AJS.$("#editpass").val();
        jsonObj.name    = AJS.$("#editname").val();
        jsonObj.email   = AJS.$("#editemail").val();
        jsonObj.depart  = AJS.$("#editdepart").val();

        var errFlag = false;
        var errText = "";


        //////////////////////////////////////////////
        // проверка заполненности полей
        //////////////////////////////////////////////
        if (AJS.$.trim(jsonObj.login) == "") {
            errFlag = true;
            errText = errText + "<div>не заполнено поле ЛОГИН</div>";
            AJS.$("#editlogin").css("background-color", "#ffe9e9");
        };

        if (AJS.$.trim(jsonObj.pass) == "") {
            errFlag = true;
            errText = errText + "<div>не заполнено поле ПАРОЛЬ</div>";
            AJS.$("#editpass").css("background-color", "#ffe9e9");
        };

        if (AJS.$.trim(jsonObj.name) == "") {
            errFlag = true;
            errText = errText + "<div>не заполнено поле ФИО</div>";
            AJS.$("#editname").css("background-color", "#ffe9e9");
        };

        if (AJS.$.trim(jsonObj.email) == "") {
            errFlag = true;
            errText = errText + "<div>не заполнено поле EMAIL</div>";
            AJS.$("#editemail").css("background-color", "#ffe9e9");
        };

        if (AJS.$.trim(jsonObj.depart) == "") {
            errFlag = true;
            errText = errText + "<div>не заполнено поле ПОДРАЗДЕЛЕНИЕ</div>";
            AJS.$("#editdepart").css("background-color", "#ffe9e9");
        };


        if (errFlag) {
            var myFlag = AJS.flag({title: "Ошибка", type: 'error', body: errText, });
            return false
        };


        // сохраним данные
        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/users/newuser",
            type: 'post',
            dataType: 'json',
            data: JSON.stringify(jsonObj),
            async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                if ((data != null) && (data.status == "ok")){
                    var myFlag = AJS.flag({
                        title: "Сведения обновлены",
                        type: 'success',
                        body: "Данные пользователя " + jsonObj.name + " обновлены",
                        close: "auto"
                    });

                    getUsersAndPutInTable();

                    AJS.dialog2("#user-edit-dialog").hide();

                };

                if ((data != null) && (data.status == "error")){
                    var myFlag = AJS.flag({
                        title: "Ошибка",
                        type: 'error',
                        body: data.description,
                        close: "manual"
                    });
                };

                if (data == null){
                    var myFlag = AJS.flag({
                        title: "Ошибка загрузки",
                        type: 'error',
                        body: "что то пошло не так ...",
                        close: "manual"
                    });
                }

            },
            error: function(data) {
                var myFlag = AJS.flag({
                    title: "Ошибка загрузки",
                    type: 'error',
                    body: "что то пошло не так ...",
                });

            },
        });




    };



    // получить строку по шаблону
    var renderRow = function(id, number, login, name, email, depart, pass) {
        var rowTemplate =  '<tr>'
                            + '<td class="user-column-hidden">__id__</td>'
                            + '<td class="user-column-check"><input type="checkbox" /></td>'
                            + '<td class="user-column-npp">__number__</td>'
                            + '<td class="user-column-login">__login__</td>'
                            + '<td class="user-column-fio">__name__</td>'
                            + '<td class="user-column-email">__email__</td>'
                            + '<td class="user-column-podr">__depart__</td>'
                            + '<td class="user-column-password">__pass__</td>'
                            + '<td>'
                            + '     <div class="user-edit-button">'
                            + '         <span class="aui-icon aui-icon-small aui-iconfont-new-edit">Edit</span>'
                            + '     </div>'
                            + '</td>'
                         + '</tr>';

        var rowStr = rowTemplate;
        rowStr = rowStr.replace("__id__", id);
        rowStr = rowStr.replace("__number__", number);
        rowStr = rowStr.replace("__login__", login);
        rowStr = rowStr.replace("__name__", name);
        rowStr = rowStr.replace("__email__", email);
        rowStr = rowStr.replace("__depart__", depart);
        rowStr = rowStr.replace("__pass__", pass);

        return rowStr;
    };

    // очистить таблицу
    var clearUsersTable = function() {
        AJS.$("#ticketsUsers").empty();
        return false;
    };

    // сгенерить строки и поместить их в таблицу
    var refreshDataInTable = function(usersArray) {
        var dataLength = usersArray.length;

        if (dataLength == 0) {
            return false;
        }

        // таблица объект
        var tableObj = AJS.$("#ticketsUsers");
        // очистка таблицы
        tableObj.empty();

        for (var i = 0; i < dataLength; i ++) {
            var oneRow = renderRow(usersArray[i].id,
                        i + 1,
                                usersArray[i].login,
                                usersArray[i].name,
                                usersArray[i].email,
                                usersArray[i].depart,
                                usersArray[i].password);

            tableObj.append(oneRow);
        }

        // присвоим события кнопке редактирования в строках
        var buttonsArr = AJS.$("#ticketsUsers .user-edit-button");
        for (var i = 0; i < buttonsArr.length; i++) {
            AJS.$(buttonsArr[i]).on("click", function () {
                var userId = AJS.$(this).parent().parent().find(".user-column-hidden").text();
                showUserEditWindow(userId);
            });
        }
    };

    // получить данные с сервера и заполнить таблицу
    var getUsersAndPutInTable = function() {

        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/users/getusers/all",
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            // async: false,
            // async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                if ((data != null) && (data.status == "ok")){
                    refreshDataInTable(data.users);
                }

                // var dataLength = data.length;
                // var strMess = "";

                console.log("data");
                console.log(data);
                //
                // if (data.status == "ok") {
                //     console.log("ok");
                //     refreshDataInTable(data);
                // }


            },
            error: function(data) {
                var myFlag = AJS.flag({
                    type: 'error',
                    body: 'Ошибка загрузки пользователей',
                });

            },
        });

    };


    // удаление записей пользователя из базы
    var removeUserInfo = function() {
        // получим идентификаторы объектов для удаления
        // в простой массив все перешлем
        var jsonObj = []

        var rows = AJS.$("#ticketsUsers tr");

        for (var i = 0; i < rows.length; i++) {
            if (rows[i].getElementsByClassName("user-column-check")[0].getElementsByTagName("input")[0].checked) {
                jsonObj.push(rows[i].getElementsByClassName("user-column-hidden")[0].innerText);
            }
        }

        if (jsonObj.length == 0) {
            return false;
        }

        // тут надо вызывать рест для удаления объектов


    }



    //////////////////////////////////////////////////
    // основные события
    //////////////////////////////////////////////////

    // заполнить таблицу
    var fillUsersTable = function() {
        getUsersAndPutInTable();
    };

    // добавить пользователя
    var addUser = function() {
        // для добавления пользователя нужно показать пустую форму
        AJS.$("#mode").val("create");
        showUserWindow("", "", "", "", "", "");
    };

    // удаление пользователя
    var delUser = function() {
        // заполнить данные для окна удаления
        var rows = AJS.$("#ticketsUsers tr");

        var message = "";

        for (var i = 0; i < rows.length; i++) {
            if (rows[i].getElementsByClassName("user-column-check")[0].getElementsByTagName("input")[0].checked) {
                message = message
                            + "<p>"
                            + rows[i].getElementsByClassName("user-column-fio")[0].innerText
                            + "(" + rows[i].getElementsByClassName("user-column-login")[0].innerText + "), "
                            + "</p>";
            }
        }

        if (AJS.$.trim(message) == "") {
            message = "Нечего удалять";
        }

        AJS.$("#deleted-users").empty();
        AJS.$("#deleted-users").html(message);

        // нужно показать окно с подтверждением удаления
        AJS.dialog2("#delete-dialog").show();
    };



    return {
        showMessage:showMessage,
        fillUsersTable:fillUsersTable,
        saveUserInfo:saveUserInfo,
        addUser:addUser,
        createUserInfo:createUserInfo,
        delUser:delUser,
        removeUserInfo:removeUserInfo,


    };


}());


AJS.$(document).ready(function() {

    // кнопка добавить
    AJS.$("#button-add").on("click", function (e) {
        setupticket.module.addUser();
    });

    // кнопка удалить
    AJS.$("#button-del").on("click", function (e) {
        setupticket.module.delUser();
    });


    ///////////////////////////////////////////////////////
    // события кнопок формы редактирования
    AJS.$("#user-edit-ok").on("click", function (e) {
        e.preventDefault();

        if (AJS.$("#mode").val() == "edit") {
            setupticket.module.saveUserInfo();
        }

        if (AJS.$("#mode").val() == "create") {
            setupticket.module.createUserInfo();
        }

    });

    AJS.$("#user-edit-cancel").on("click", function (e) {
        e.preventDefault();
        AJS.dialog2("#user-edit-dialog").hide();
    });
    ///////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////
    // события кнопок формы удаления
    AJS.$("#delete-dialog-confirm").on('click', function (e) {
        e.preventDefault();
        setupticket.module.removeUserInfo();
    });

    AJS.$("#delete-dialog-cancel").on('click', function (e) {
        e.preventDefault();
        AJS.dialog2("#delete-dialog").hide();
    });

    ///////////////////////////////////////////////////////


    setupticket.module.fillUsersTable();
});
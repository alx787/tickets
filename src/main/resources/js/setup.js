var setupticket = {};

setupticket.module = (function () {
    var showMessage = function() {
        console.log("=========== проверка ===========");
        return false;
    };

    // открыть окно редактирования пользователя
    var showUserEditWindow = function(userId) {

        if (AJS.$.trim(userId) == "") {
            return false
        }


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

                }

                console.log("data");
                console.log(data);

            },
            error: function(data) {
                var myFlag = AJS.flag({
                    type: 'error',
                    body: 'Ошибка загрузки данных о пользователе',
                });

            },
        });





        AJS.dialog2("#user-edit-dialog").show();

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
    }

    // очистить таблицу
    var clearUsersTable = function() {
        AJS.$("#ticketsUsers").empty();
        return false;
    }

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
    }

    // получить данные с сервера и заполнить таблицу
    var grtUsersAndPutInTable = function() {

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

    }

    var fillUsersTable = function() {

        grtUsersAndPutInTable();

    }



    return {
        showMessage:showMessage,
        fillUsersTable:fillUsersTable,

    };


}());


AJS.$(document).ready(function() {
    setupticket.module.fillUsersTable();
});
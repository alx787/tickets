
var tickets = {};

tickets.module = (function () {
    var helloWorld = function(param1) {
        // console.log("привет мир !!!");
        console.log(param1);
        return true;
    }

    /////////////////////////////////////////////////////////
    // получить заполнить строку
    var renderRow = function (created, number, descr, status, duedate) {
        var rowTemplate = '<tr>'
                            + '<td class="order-created">__created__</td>'
                            + '<td class="order-number">__number__</td>'
                            + '<td class="order-descr">__descr__/td>'
                            + '<td class="order-status">__status__</td>'
                            + '<td class="order-duedate">__duedate__</td>'
                        + '</tr>';

        var rowStr = rowTemplate;
        rowStr = rowStr.replace("__created__", created);
        rowStr = rowStr.replace("__number__", number);
        rowStr = rowStr.replace("__descr__", descr);
        rowStr = rowStr.replace("__status__", status);
        rowStr = rowStr.replace("__duedate__", duedate);

        return rowStr;
    }


    /////////////////////////////////////////////////////////
    // получить заполнить строку
    var refreshDataInTable = function (jsonData) {
        // таблица объект
        var tableObj = AJS.$("#ticketsFromUsers");

        // очистим строки таблицы
        AJS.$("#ticketsFromUsers tr").remove();

        var dataLength = jsonData.issues.length;

        for (var i = 0; i < dataLength; i++) {
            var oneRow = renderRow(jsonData.issues[i].createdate,
                                    jsonData.issues[i].number,
                                    jsonData.issues[i].description,
                                    jsonData.issues[i].status,
                                    jsonData.issues[i].duedate);

            tableObj.append(oneRow);
        }

    }



    /////////////////////////////////////////////////////////
    // заполнение таблицы данными с сервера
    var fillTable = function (status, page, datefirst, datelast, issuenum) {

        var sIssueNum = issuenum;

        if (sIssueNum == null || sIssueNum === "") {
            sIssueNum = "-";
        }


        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/service/gettickets/" + status + "/" + page + "/" + datefirst + "/" + datelast + "/" + sIssueNum,
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            // async: false,
            // async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                // var dataLength = data.length;
                // var strMess = "";

                console.log(data);

                if (data.status == "ok") {
                    console.log("ok");
                    refreshDataInTable(data);
                }


                //
                // for (var i = 0; i < dataLength; i++) {
                //
                //     rowStr = rowTemplate.replace("__objectId__", "attachsign_" + data[i].id);
                //     rowStr = rowStr.replace("__object__", data[i].name);
                //     rowStr = rowStr.replace("__status__", "&nbsp;");
                //
                //     tableObj.append(rowStr);
                //
                //
                //     rows = AJS.$("div#signDetailDiv ul li");
                //     AJS.$(rows[rows.size() - 1]).find(".sign-bthcheck button").bind("click", function() {
                //         getSignForObject(this);
                //     });
                //
                //
                // }
                //
            },
            error: function(data) {
                // var myFlag = AJS.flag({
                //     type: 'error',
                //     body: 'Ошибка загрузки',
                // });

            },
        });


        return true;

    }






    return {
        helloWorld:helloWorld,
        fillTale:fillTable,
    }

}())


AJS.$(document).ready(function() {
    AJS.$('#date-begin').datePicker({'overrideBrowserDefault': true, firstDay: -1, 'languageCode': 'ru'});
    AJS.$('#date-end').datePicker({'overrideBrowserDefault': true, 'languageCode': 'ru'});


    // Shows the dialog when the "Show dialog" button is clicked
    //AJS.$("#dialog-show-button").click(function(e) {
    AJS.$(AJS.$("#ticketsFromUsers tr")[0]).click(function(e) {
        e.preventDefault();
        AJS.dialog2("#demo-dialog").show();
    });

    // Hides the dialog
    AJS.$("#dialog-submit-button").click(function (e) {
        e.preventDefault();
        AJS.dialog2("#demo-dialog").hide();
    });

    // заполнение таблицы заявками
    //fillTable("open", 1, "-", "-", "-");
});
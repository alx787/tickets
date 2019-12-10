
var tickets = {};

tickets.module = (function () {
    var helloWorld = function(param1) {
        // console.log("привет мир !!!");
        console.log(param1);
        return true;
    }


    /////////////////////////////////////////////////////////
    // текущий номер страницы - глобальная переменная
    var currPage = 0;

    var setCurrPage = function (pageNum) {
        if (isNaN(pageNum)) {
            //currPage = 0;
        } else {
            currPage = pageNum;
        }
    }

    var getCurrPage = function () {
        return currPage;
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

        // если количество возвратных данных больше нуля то заменяем тек.страницу на новую
        setCurrPage(jsonData.currPage);
        //setCurrPage(101);

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

        // проверка того что page является числом
        if(isNaN(page)) {
            return false;
        }


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
        fillTable:fillTable,
        setCurrPage:setCurrPage,
        getCurrPage:getCurrPage,
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

    ///////////////////////////////////////
    // обработка событий пагинатора
    AJS.$("#paginator li.aui-nav-first").click(function (e) {
        console.log("<---");
    });

    AJS.$("#paginator li.aui-nav-previous").click(function (e) {
        console.log("<");
    });

    AJS.$("#paginator li.aui-nav-next").click(function (e) {
        console.log(">");
    });

    AJS.$("#paginator li.aui-nav-last").click(function (e) {
        console.log("--->");
    });

    AJS.$("#paginator input[name='currpage']").keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){
            console.log("enter detected");
        }
    });


    // заполнение таблицы заявками
    // status, page, datefirst, datelast, issuenum
    tickets.module.fillTable("open", 1, "-", "-", "-");

    // общая схема такова
    // при нажатии на кнопку, получаем номер страницы и его отправляем в рест
    // если запрос выполнился и вернул строки то устанавливаем новый номер страницы в модуле и из него устанавливаем номер страницы в UI
    // если запрос ничего не вернул то ничего не меняем, оставляем все без изменений
});
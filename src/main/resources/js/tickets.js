
var tickets = {};

tickets.module = (function () {
    var helloWorld = function(param1) {
        // console.log("привет мир !!!");
        console.log(param1);
        return true;
    }


    /////////////////////////////////////////////////////////
    // глобальные переменные

    // текущий номер страницы
    var currPage = 0;
    // максимальный номер страницы
    var maxPage = 0;
    // заявки open или done
    var orderStatus = "";


    /////////////////////////////////////////////////////////
    // геттеры и сеттеры глобальных переменных

    //
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

    //
    var getMaxPage = function () {
        return maxPage;
    }

    //
    var setCurrStatus = function (newCurrStatus) {
        currStatus = newCurrStatus;
    }

    var getCurrStatus = function () {
        return currStatus;
    }

    /////////////////////////////////////////////////////////
    // получить заполнить строку
    var renderRow = function (created, number, summary, status, duedate) {
        var rowTemplate = '<tr>'
                            + '<td class="order-created">__created__</td>'
                            + '<td class="order-number">__number__</td>'
                            + '<td class="order-descr">__summary__</td>'
                            + '<td class="order-status">__status__</td>'
                            + '<td class="order-duedate">__duedate__</td>'
                        + '</tr>';

        var rowStr = rowTemplate;
        rowStr = rowStr.replace("__created__", created);
        rowStr = rowStr.replace("__number__", number);
        rowStr = rowStr.replace("__summary__", summary);
        rowStr = rowStr.replace("__duedate__", duedate);

        // перевод поля на русский
        // rowStr = rowStr.replace("__status__", status);
        if (status == "To Do") {
            rowStr = rowStr.replace("__status__", "сделать");
        }
        if (status == "In Progress") {
            rowStr = rowStr.replace("__status__", "выполняется");
        }
        if (status == "Done") {
            rowStr = rowStr.replace("__status__", "выполнено");
        }

        return rowStr;
    }


    /////////////////////////////////////////////////////////
    // показать окно с описанием задачи
    var showPopupWindow = function (issueId) {
        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/service/getticketinfo/" + issueId,
            type: 'get',
            dataType: 'json',
            // data: JSON.stringify(jsonObj),
            // async: false,
            // async: true,
            contentType: "application/json; charset=utf-8",
            success: function(data) {

                // var dataLength = data.length;
                // var strMess = "";

                //console.log(data);
                //
                // if (data.status == "ok") {
                //     console.log("ok");
                //     refreshDataInTable(data);
                // }
                AJS.$("#popupHeader").text("Заявка " + data.number + " от " + data.created);
                AJS.$("#popupSummary").text(data.summary);

                // AJS.$("#popupDescription").text(data.description);
                AJS.$("#popupDescription").html(data.description.replace(/\n/g, "<p/>"));

                AJS.dialog2("#demo-dialog").show();


            },
            error: function(data) {
                // var myFlag = AJS.flag({
                //     type: 'error',
                //     body: 'Ошибка загрузки',
                // });

            },
        });

    }

    /////////////////////////////////////////////////////////
    // получить заполнить строку
    var refreshDataInTable = function (jsonData) {
        var dataLength = jsonData.issues.length;

        if (dataLength == 0) {
            return false;
        }

        // таблица объект
        var tableObj = AJS.$("#ticketsFromUsers");

        // очистим строки таблицы
        AJS.$("#ticketsFromUsers tr").remove();


        // если количество возвратных данных больше нуля то заменяем тек.страницу на новую
        setCurrPage(jsonData.currPage);
        // тут же обновим номер страницы
        AJS.$("input[name='currpage']").val(jsonData.currPage);
        AJS.$("#total-cnt").text(jsonData.pages);
        maxPage = jsonData.pages;
        // и количество страниц всего

        //setCurrPage(101);

        for (var i = 0; i < dataLength; i++) {
            var oneRow = renderRow(jsonData.issues[i].createdate,
                                    jsonData.issues[i].number,
                                    jsonData.issues[i].summary,
                                    jsonData.issues[i].status,
                                    jsonData.issues[i].duedate);

            tableObj.append(oneRow);
        }

        // назначим события на нажатие на строку
        AJS.$("#ticketsFromUsers tr").each(function (indx) {
            AJS.$(this).click(function (e) {

                // тут надо заполнить поля в инфо окне через ajax
                //console.log(AJS.$(this).find(".order-number").text());
                // console.log(e);
                // console.log(this);

                e.preventDefault();
                // AJS.dialog2("#demo-dialog").show();

                var issueId = AJS.$(this).find(".order-number").text();
                showPopupWindow(issueId);

            })
        });

        // Hides the dialog
        AJS.$("#dialog-submit-button").click(function (e) {
            e.preventDefault();
            AJS.dialog2("#demo-dialog").hide();
        });

    }



    // Shows the dialog when the "Show dialog" button is clicked
    //AJS.$("#dialog-show-button").click(function(e) {
    // AJS.$(AJS.$("#ticketsFromUsers tr")[0]).click(function(e) {
    //     e.preventDefault();
    //     AJS.dialog2("#demo-dialog").show();
    // });

    // Hides the dialog
    // AJS.$("#dialog-submit-button").click(function (e) {
    //     e.preventDefault();
    //     AJS.dialog2("#demo-dialog").hide();
    // });




    /////////////////////////////////////////////////////////
    // заполнение таблицы данными с сервера
    var fillTable = function (page, datefirst, datelast, issuenum) {

        var sIssueNum = issuenum;

        // проверка того что page является числом
        if(isNaN(page)) {
            return false;
        }


        if (sIssueNum == null || sIssueNum === "") {
            sIssueNum = "-";
        }


        AJS.$.ajax({
            url: AJS.params.baseURL + "/rest/exploretickets/1.0/service/gettickets/" + currStatus + "/" + page + "/" + datefirst + "/" + datelast + "/" + sIssueNum,
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

    /////////////////////////////////////////////////////////
    // получить параметры для фильтра
    var getFilterParameters = function () {
        var params = {};

        params.datefirst = AJS.$("#date-begin").val();
        params.datelast = AJS.$("#date-end").val();
        params.issuenum = AJS.$("input[name='ticket-number']").val();

        if (params.datefirst == "") {
            params.datefirst = "-"
        };

        if (params.datelast == "") {
            params.datelast = "-"
        };

        if (params.issuenum == "") {
            params.issuenum = "-"
        };

        return params;
    }



    return {
        helloWorld:helloWorld,
        fillTable:fillTable,
        setCurrPage:setCurrPage,
        getCurrPage:getCurrPage,
        setCurrStatus:setCurrStatus,
        getCurrStatus:getCurrStatus,
        getFilterParameters:getFilterParameters,
        getMaxPage:getMaxPage,
    }

}())


AJS.$(document).ready(function() {
    AJS.$('#date-begin').datePicker({'overrideBrowserDefault': true, firstDay: -1, 'languageCode': 'ru'});
    AJS.$('#date-end').datePicker({'overrideBrowserDefault': true, 'languageCode': 'ru'});


    // Shows the dialog when the "Show dialog" button is clicked
    //AJS.$("#dialog-show-button").click(function(e) {
    // AJS.$(AJS.$("#ticketsFromUsers tr")[0]).click(function(e) {
    //     e.preventDefault();
    //     AJS.dialog2("#demo-dialog").show();
    // });

    // Hides the dialog
    // AJS.$("#dialog-submit-button").click(function (e) {
    //     e.preventDefault();
    //     AJS.dialog2("#demo-dialog").hide();
    // });

    ///////////////////////////////////////
    // обработка событий пагинатора
    AJS.$("#paginator li.aui-nav-first").click(function (e) {
        var currPageNumber = tickets.module.getCurrPage();
        if (currPageNumber == 1) {
            return false;
        }

        var params = tickets.module.getFilterParameters();
        tickets.module.fillTable(1, params.datefirst, params.datelast, params.issuenum);

        console.log("<---");
    });

    AJS.$("#paginator li.aui-nav-previous").click(function (e) {
        var currPageNumber = tickets.module.getCurrPage();
        if (currPageNumber == 1) {
            return false;
        }

        var params = tickets.module.getFilterParameters();
        tickets.module.fillTable(currPageNumber - 1, params.datefirst, params.datelast, params.issuenum);


        console.log("<");
    });

    AJS.$("#paginator li.aui-nav-next").click(function (e) {
        var currPageNumber = tickets.module.getCurrPage();
        var maxPageNumber = tickets.module.getMaxPage();

        if (currPageNumber + 1 > maxPageNumber) {
            return false;
        }

        var params = tickets.module.getFilterParameters();
        tickets.module.fillTable(currPageNumber + 1, params.datefirst, params.datelast, params.issuenum);

        console.log(">");
    });

    AJS.$("#paginator li.aui-nav-last").click(function (e) {
        var maxPageNumber = tickets.module.getMaxPage();
        var params = tickets.module.getFilterParameters();
        tickets.module.fillTable(maxPageNumber, params.datefirst, params.datelast, params.issuenum);

        console.log("--->");
    });

    AJS.$("#paginator input[name='currpage']").keypress(function(event) {
        var keycode = (event.keyCode ? event.keyCode : event.which);
        if(keycode == '13'){

            var currPageNumber = tickets.module.getCurrPage();
            var params = tickets.module.getFilterParameters();
            tickets.module.fillTable(currPageNumber, params.datefirst, params.datelast, params.issuenum);

            console.log("enter detected");
        }
    });

    // кнопка поиска
    AJS.$("#searchButton").click(function (e) {
        var params = tickets.module.getFilterParameters();
        tickets.module.fillTable(1, params.datefirst, params.datelast, params.issuenum);
    });



    // заполнение таблицы заявками
    // status, page, datefirst, datelast, issuenum
    if (AJS.$("input[name='orders-mode']").val() == "open") {
        tickets.module.setCurrStatus("open");
    } else {
        tickets.module.setCurrStatus("done");
    }
    tickets.module.fillTable(1, "-", "-", "-");

    // общая схема такова
    // при нажатии на кнопку, получаем номер страницы и его отправляем в рест
    // если запрос выполнился и вернул строки то устанавливаем новый номер страницы в модуле и из него устанавливаем номер страницы в UI
    // если запрос ничего не вернул то ничего не меняем, оставляем все без изменений
});

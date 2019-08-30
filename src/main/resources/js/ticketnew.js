var createticket = {};


window.onbeforeunload = function() {
    // return 'You have unsaved changes!';
}


createticket.module = (function () {

    ///////////////////////////////////////////
    // проверка на заполненность полей
    ///////////////////////////////////////////
    var findErrorsInFields = function() {

        var errorInFields = false;
        // тема
        if (!AJS.$.trim(AJS.$("#ticket-theme").val())) {
            errorInFields = true;

            AJS.$("#ticket-theme").css("border", "1px solid #FF0000");

            var myFlag = AJS.flag({
                type: "error",
                body: '<span style="color: #FF0000;">Тема заявки должна быть заполнена !!!</span>'
            });
        };

        if (!AJS.$.trim(AJS.$("#ticket-text").val())) {
            errorInFields = true;

            AJS.$("#ticket-text").css("border", "1px solid #FF0000");

            var myFlag = AJS.flag({
                type: "error",
                body: '<span style="color: #FF0000;">Текст заявки должен быть заполнен !!!</span>'
            });
        };

        if (errorInFields) {
            return true;
        }

        return false;
    };


    var createIssue = function() {

        ///////////////////////////////////////////
        // проверка на заполненность полей
        ///////////////////////////////////////////
        if (findErrorsInFields()) {
            return true;
        }


        ///////////////////////////////////////////

        // на время заблокируем кнопку
        AJS.$("#ticket-form .buttons button").attr("disabled", "disabled");
        // AJS.$("#ticket-form .buttons button").removeAttr("disabled");


        var restUrl = AJS.params.baseURL + "/rest/exploretickets/1.0/service/createticket";

        var formDataTicket = new FormData();

        formDataTicket.append("ticket-theme", AJS.$("#ticket-theme").val());
        formDataTicket.append("ticket-text", AJS.$("#ticket-text").val());


        formDataTicket.append("ticket-username", AJS.$("#ticket-username").text());
        formDataTicket.append("ticket-useremail", AJS.$("#ticket-useremail").text());
        formDataTicket.append("ticket-userdepart", AJS.$("#ticket-userdepart").text());


        var fileObj = AJS.$("#ticket-file-upload")[0].files;
        var fileLen =  fileObj.length;
        for (var i = 0; i < fileLen; i++) {
            formDataTicket.append("ticket-file-upload", fileObj[i]);

        }


        // var form = AJS.$("#ticket-form")[0];
        // var formDataTicket = new FormData(form);


        AJS.$.ajax({
            url: restUrl,
            type: 'post',
            enctype: 'multipart/form-data',
            processData: false,  // Important!
            dataType: 'json',
            data: formDataTicket,
            cache: false,
            async: true,
            // async: asyncFlag,
            // contentType: "application/json; charset=utf-8",
            contentType: false,
            success: function (data) {
                AJS.dialog2("#demo-dialog").show();
            },
            error: function (data) {
                // разблокируем кнопку
                AJS.$("#ticket-form .buttons button").removeAttr("disabled");

                var myFlag = AJS.flag({
                    type: "error",
                    body: '<span style="color: #FF0000;">Ошибка при отправке !!!</span>'
                });

            },
            complete: function () {

            },

        });
    };

    // var redirectToMenu = function() {
    //     // window.confirm = null;
    //     window.location.href = AJS.params.baseURL + "/secure/ordersAction!menu.jspa";
    //         // window.location.href = window.location.protocol +'//'+ window.location.host + window.location.pathname;
    //
    // }



    return {
        createIssue:createIssue,
        // redirectToMenu:redirectToMenu
    };

}());

var createticket = {};

createticket.module = (function () {

    var createIssue = function() {

        // на время заблокируем кнопку
        AJS.$("#ticket-form .buttons button").attr("disabled", "disabled");
        // AJS.$("#ticket-form .buttons button").removeAttr("disabled");


        var restUrl = AJS.params.baseURL + "/rest/exploretickets/1.0/service/createticket";

        var formDataTicket = new FormData();

        formDataTicket.append("ticket-theme", AJS.$("#ticket-theme").val());
        formDataTicket.append("ticket-text", AJS.$("#ticket-text").val());

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

            },
            error: function (data) {

            },
            complete: function () {

            },

        });
    };

    return {
        createIssue:createIssue
    };

}());

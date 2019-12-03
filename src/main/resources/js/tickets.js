
var tickets = {};

tickets.module = (function () {
    var helloWorld = function() {
        console.log("привет мир !!!");
        return true;
    }


    return {
        helloWorld:helloWorld,
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
});

AJS.$(document).ready(function() {
    AJS.$('#date-begin').datePicker({'overrideBrowserDefault': true});
    AJS.$('#date-end').datePicker({'overrideBrowserDefault': true});


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
});
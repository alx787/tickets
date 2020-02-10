var setupticket = {};


setupticket.module = (function () {
    var showMessage = function() {
        console.log("=========== проверка ===========");
        return false;
    };

    return {
        showMessage:showMessage,
        // redirectToMenu:redirectToMenu
    };


}());
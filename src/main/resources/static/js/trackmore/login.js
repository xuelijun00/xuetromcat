$("body").keydown(function (event) {
    var event = window.event || event;
    if (event.keyCode == 13) {//keyCode=13是回车键
        $('#login-button').click();
    }
});
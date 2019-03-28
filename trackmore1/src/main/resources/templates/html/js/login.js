(function () {
    $('#login-button').click(function (event) {
        $.ajax({
            type: 'POST',
            url: contextPath + '/login',
            async: false,
            dataType: 'json',
            data: $(".form").serializeArray(),
            success: function (data) {
                $("#error").text("");
                if (data.status !== 200) {
                    $("#error").text(data.message);
                } else {
                    window.location.href = contextPath + '/index';
                }
            }
        });
    });
    $("body").keydown(function (event) {
        var event = window.event || event;
        if (event.keyCode == "13") {//keyCode=13是回车键
            $('#login-button').click();
        }
    });
})();
var loginObj = {
    /**
     * 描述 : 请求登录
     * 作者 : Edgar.lee
     */
    'login': function (thisObj) {
        var post = {};
        L.open('tip')('正在登录...', false);

        $('input', thisObj).each(function () {
            post[$(this).attr('name')] = this.value;
        });
        $.post(ROOT_URL + '/index.php?c=ctrl_main&a=login', post, function (data) {
            if (data === 'done') {
                window.location.reload(true);
            } else {
                $('#captcha').click();
                L.open('tip')(data);
            }
        });
        return false;
    }
}
var channelManagement = {
    save: function () {
        $("#statusManagement").ajaxForm({
            beforeSubmit: function (arr, $form, options) {
                var bo = true;
                $.each(arr, function (i, data) {
                    if ((data.name == 'channel' || data.name == 'statusName' || data.name == 'keyword') && data.value.trim().length <= 0) {
                        layer.msg("前三项为必填字段", {icon: 5, closeBtn: 0, time: 3000});
                        bo = false;
                    }
                });
                return bo;
            },
            success: function (message, j) {
                if (message.status == 200) {
                    parent.common.showMessage(message.status, message.message);
                } else {
                    parent.common.showMessage(message.status, message.message);
                }
            }
        });
    },
    downloadCsv: function () {
        $.ajax({
            url: "getInformation",
            cache: false,
            type: "GET",
            //async: false,
            data: {
                page: 0,
                rows: 0,
                channel: $("#form [name='channel']").val(),
                startTime: $("#start_date").val(),
                endTime: $("#end_date").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "状态信息管理.csv";
                    var dataArr = data.rows;
                    var title = ["物流渠道", "状态(1、上网 2、封发 3、交航 4、落地 5、妥投 )", "标识关键字", "排除标识关键字", "添加时间"];
                    var column = ["channel", "logisticsStatus", "keyword", "createAt"];
                    exportDataToCSV(fileName, dataArr, title, column);
                }
            }
        });
    },
    pagefunc: function () {
        layui.use(['laypage', 'layer'], function () {
            var laypage = layui.laypage;
            laypage({
                cont: 'page'
                , pages: parseInt($("#pagetotal").val() ? $("#pagetotal").val() : 1) //总页数
                , groups: 5 //连续显示分页数
                , skip: true
                , curr: $("#curPage").val()
                , jump: function (obj, first) {
                    //得到了当前页，用于向服务端请求对应数据
                    var page = obj.curr;
                    var rows = $("#pagesize").val();
                    $("#curPage").val(obj.curr);
                    channelManagement.pageRenderData(page, rows);
                }
            });
        });
    },
    pageRenderData: function (page, rows) {
        $.getJSON('getInformation', {
            page: page,
            rows: rows,
            channel: $("#form [name='channel']").val(),
            startTime: $("#start_date").val(),
            endTime: $("#end_date").val(),
        }, function (data) {
            $("table tbody").empty();
            var trs;
            $.each(data.rows, function (index, item) {
                var tr = '<tr>' +
                    '<td>' + (index + 1) + '</td>' +
                    '<td>' + item.channel + '</td>' +
                    '<td>' + item.statusName + '</td>' +
                    '<td>' + item.keyword + '</td>' +
                    '<td>' + item.excludeKeyword + '</td>' +
                    '<td>' + item.createAt + '</td>' +
                    '<td><div class="dropdown">' +
                    '<button class="btn btn-sm btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">操作 <span class="caret"></span></button> ' +
                    '<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"> ' +
                    '<li><a onclick="common.showAddOrEdit2(\'修改物流状态\',\'updateview?channel=' + item.channel + '\',\'1000px\',\'500px\')" >修改</a></li>' +
                    '</ul></div>' +
                    '</td>' +
                    '</tr>';
                trs += tr;
            });
            $("table tbody").html(trs);
            $("#pageinfo").empty();
            if (page == $("#pagetotal").val()) {
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 ' + ((data.page - 1) * 10 + 1 < 0 ? 0 : (data.page - 1) * 10 + 1) + ' 到 ' + data.records + ' 项，共 ' + data.records + ' 项</label>');
            } else {
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 ' + ((data.page - 1) * 10 + 1 < 0 ? 0 : (data.page - 1) * 10 + 1) + ' 到 ' + (data.page * 10) + ' 项，共 ' + data.records + ' 项</label>');
            }
            $("#pagesize").val(data.pagesize);
        });
    },
    pageSizeChange: function (dom) {
        var pagesize = $(dom).val();
        channelManagement.pageRenderData(1, pagesize);
    }
};
(function(){
    $("#start_date").jeDate({
        format: "YYYY-MM-DD",
        //isinitVal:true,
        //isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    $("#end_date").jeDate({
        format: "YYYY-MM-DD",
        //isinitVal:true,
        //isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    channelManagement.pagefunc();
})();





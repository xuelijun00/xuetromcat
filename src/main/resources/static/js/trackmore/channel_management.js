var channelManagement = {
    save: function () {
        $("#channelManagement").ajaxForm({
            success:function (i,j) {
                var message = JSON.parse(i);
                if(message.status == 200){
                    /*layer.msg(message.message, {time: 3000});
                    parent.common.sleep(3000);
                    parent.common.close(message.status);*/
                    parent.common.showMessage(message.status,message.message);
                }else{
                    parent.common.showMessage(message.status,message.message);
                }
             }
        });
       /* $.ajax({
            url: "LogisticChannel/insert",
            cache: false,
            type: "POST",
            async: false,
            data: $("#channelManagement").serializeArray(),
            success: function (data) {
                if (data.status == 200) {
                    parent.common.close(parent.common.win);
                    layer.msg('添加物流渠道成功！', {time: 3000});
                } else {
                    layer.msg('添加物流渠道失败！', {time: 3000});
                }
            }
        });*/
    },
    downloadCsv:function(){
        $.ajax({
            url: "get",
            cache: false,
            type: "GET",
            //async: false,
            data: {
                page: 0,
                rows: 0,
                shortCode: $("#form [name='shortCode']").val(),
                startTime: $("#start_date").val(),
                endTime: $("#end_date").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "物流渠道信息管理.csv";
                    var dataArr = data.rows;
                    var title=["yks物流渠道简码","快递商简码","快递商中文名称","快递商中文名称","公司定义的物流渠道名称","状态(0:禁用1:启用)","创建时间"];
                    var column=["yksChannelCode","shortCode","englishName","chinessName","yksChannelName","status","createTime"];
                    exportDataToCSV(fileName,dataArr,title,column);
                }
            }
        });
    },
    showUpload:function(){
        var content = '<div class="panel-body">'+
            '<div class="alert alert-info" role="alert">'+
                '<p>模板下载: <a href="../excel/channel_templater.csv">模板</a></p>'+
                '<p style="color: red">注意：带*号字段不能为空,不能添加已有数据和重复数据，导入需要时间，请耐心等待</p>'+
            '</div>'+
            '<form action="" enctype="multipart/form-data" class="form-horizontal" role="form" method="post">'+
            '<div class="form-group">'+
                '<label class="col-sm-3 control-label">上传文件：</label>'+
                '<div class="col-sm-7">'+
                    '<input name="file" class="layui-upload-file form-control" data-val="true" data-val-required="请选择[上传文件]!" id="File" value="" type="file" />'+
                '</div>'+
            '</div>'+
            '</form>'+
            '</div><script>channelManagement.upload()</script>';
        layer.open({
            type: 1,
            title: '上传物流渠道信息',
            maxmin: false,
            shadeClose: false, //点击遮罩关闭层
            area: ['560px', '350px'],
            content: content,
        });
    },
    upload:function(){
        layui.use('upload', function(){
            layui.upload({
                url: 'upload'
                ,ext: 'csv|xlsx' //那么，就只会支持这种格式的上传。注意是用|分割。
                ,type: 'file'
                ,unwrap: false
                ,title: '请上传文件'
                ,method:'post'
                ,success: function(res, input){
                    if(res.status == 200){
                        //layer.msg(res.message, {time: 3000});
                        common.showMessage(res.status,res.message);
                    }else{
                        common.showMessage(res.status,res.message);
                    }
                }
            });
        });
    },
    disableOrEnabled:function(url){
        $.getJSON(url, function (data) {
            if(data.status == 200){
                common.showMessage(data.status,data.message);
                common.reload(channelManagement.getCurrUrl());
            }else{
                common.showMessage(data.status,data.message);
            }
        });
    },
    pagefunc:function(){
        layui.use(['laypage', 'layer'], function(){
            var laypage = layui.laypage;
            laypage({
                cont: 'page'
                ,pages: parseInt($("#pagetotal").val()?$("#pagetotal").val():1) //总页数
                ,groups: 5 //连续显示分页数
                ,skip: true
                ,curr:$("#curPage").val()
                ,jump: function(obj, first){
                    //得到了当前页，用于向服务端请求对应数据
                    var page = obj.curr;
                    var rows = $("#pagesize").val();
                    $("#curPage").val(obj.curr);
                    channelManagement.pageRenderData(page, rows);
                }
            });
        });
    },
    pageRenderData:function(page, rows){
        $.getJSON('get', {
            page: page,
            rows: rows,
            shortCode: $("#form [name='shortCode']").val(),
            englishName: $("#englishName").val(),
            chinessName: $("#chinessName").val(),
            yksChannelName: $("#yksChannelName").val(),
            startTime: $("#start_date").val(),
            endTime: $("#end_date").val(),
        }, function (data) {
            $("table tbody").empty();
            var trs;
            $.each(data.rows,function(index,item){
                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.yksChannelCode +'</td>'+
                    '<td>'+ item.yksChannelName +'</td>'+
                    '<td>'+ item.shortCode +'</td>'+
                    '<td>'+ item.englishName +'</td>'+
                    '<td>'+ item.chinessName +'</td>'+
                    '<td>'+ (item.status==1?'启用':'禁用') +'</td>'+
                    '<td>'+ item.createTime +'</td>'+
                    '<td><div class="dropdown">' +
                    '<button class="btn btn-sm btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">操作 <span class="caret"></span></button> ' +
                    '<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"> ' +
                    '<li><a onclick="common.showAddOrEdit(\'修改物流渠道\',\'updateview?id='+ item.id +'\')" >修改</a></li>';
                    if(item.status==1){ tr += '<li><a onclick="channelManagement.disableOrEnabled(\'disable/'+ item.id +'\')">禁用</a></li> '}
                    if(item.status==0){ tr += '<li><a onclick="channelManagement.disableOrEnabled(\'enabled/'+ item.id +'\')">启用</a></li> '}
                    tr += '</ul></div>' +
                    '</td>'+
                    '</tr>';
                trs += tr;
            });
            $("table tbody").html(trs);
            $("#pageinfo").empty();
            if(page == $("#pagetotal").val()){
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 '+ ((data.page - 1) * 10 + 1<0?0:(data.page - 1) * 10 + 1) +' 到 '+ data.records +' 项，共 '+ data.records +' 项</label>');
            }else{
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 '+ ((data.page - 1) * 10 + 1<0?0:(data.page - 1) * 10 + 1) +' 到 '+ (data.page * 10) +' 项，共 '+ data.records +' 项</label>');
            }
            $("#pagesize").val(data.pagesize);
        });
    },
    pageSizeChange:function(dom){
        var pagesize = $(dom).val();
        channelManagement.pageRenderData(1, pagesize);
    },
    getCurrUrl:function(){
        var param = "view?";
        param += "rows=" + $("#pagesize").val();
        param += "&page=" + $("#curPage").val();
        param += "&shortCode=" + $("#form [name='shortCode']").val();
        param += "&englishName=" + $("#englishName").val();
        param += "&chinessName=" + $("#chinessName").val();
        param += "&yksChannelName=" + $("#yksChannelName").val();
        param += "&startTime=" + $("#start_date").val();
        param += "&endTime=" + $("#end_date").val();
        return param;
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





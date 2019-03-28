var channelManagement = {
    downloadCsv:function(){
        $.ajax({
            url: "get",
            cache: false,
            type: "GET",
            //async: false,
            data: {
                page: 0,
                rows: 0,
                platforms:$("#form [name='platforms']").val(),
                country:$("#form [name='country']").val(),
                channelNames: $("#form [name='channelNames']").val(),
                ordersMailCode: $("#ordersMailCode").val(),
                startTime: $("#start_date").val(),
                endTime: $("#end_date").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "异常信息.csv";
                    var dataArr = data.rows;
                    for(var i=0;i<dataArr.length;i++){
                        dataArr[i].ordersMailCode = "'" + dataArr[i].ordersMailCode;
                    }
                    var title=["平台","国家","渠道","发货时间","订单追踪码","内单号","销售账号","16包裹查询不到17投递失败18包裹异常19包裹很长时间运输途中20到达待取","异常备注"];
                    var column=["platform","country","channelName","ordersOutTime","ordersMailCode","erpOrdersId","salesAccount","exceptionStatus","remark"];
                    exportDataToCSV(fileName,dataArr,title,column);
                }
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
                ,jump: function(obj, first){
                    //得到了当前页，用于向服务端请求对应数据
                    var page = obj.curr;
                    var rows = $("#pagesize").val();
                    channelManagement.pageRenderData(page, rows);
                }
            });
        });
    },
    pageRenderData:function(page,rows){
        $.getJSON('get', {
            page: page,
            rows: rows,
            platforms:$("#form [name='platforms']").val(),
            country:$("#form [name='country']").val(),
            channelNames: $("#form [name='channelNames']").val(),
            ordersMailCode: $("#ordersMailCode").val(),
            startTime: $("#start_date").val(),
            endTime: $("#end_date").val(),
        }, function (data) {
            $("table tbody").empty();
            var trs;
            $.each(data.rows,function(index,item){
                var ex = "";
                if(item.exceptionStatus == 10){ex = "未上网";}
                else if(item.exceptionStatus == 11){ex = "未封发";}
                else if(item.exceptionStatus == 12){ex = "未交航";}
                else if(item.exceptionStatus == 13){ex = "未落地";}
                else if(item.exceptionStatus == 14){ex = "未妥投";}

                else if(item.exceptionStatus == 16){ex = "包裹查询不到";}
                else if(item.exceptionStatus == 17){ex = "投递失败";}
                else if(item.exceptionStatus == 18){ex = "包裹异常";}
                else if(item.exceptionStatus == 19){ex = "包裹很长时间运输途中";}
                else if(item.exceptionStatus == 20){ex = "到达代取";}
                else if(item.exceptionStatus == 21){ex = "异常";}
                else if(item.exceptionStatus == 22){ex = "异常处理完结";}
                else{ex = "异常";}

                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.platform +'</td>'+
                    '<td>'+ item.country +'</td>'+
                    '<td>'+ item.channelName +'</td>'+
                    '<td>'+ item.ordersOutTime +'</td>'+
                    '<td class="ordersMailCode">'+ item.ordersMailCode +'</td>'+
                    '<td>'+ item.erpOrdersId +'</td>'+
                    '<td>'+ item.salesAccount +'</td>'+
                    '<td>'+ ex +'</td>'+
                    '<td>'+ item.remark +'</td>'+
                    '<td><button type="button" class="showWriteExceptionMessage btn btn-primary" onclick="channelManagement.writeExceptionMessage(this)">增加备注</button></td>'+
                    '</tr>';
                trs += tr;
            });
            $("table tbody").html(trs);
            $("#pageinfo").empty();
            if(page == $("#pagetotal").val()){
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 '+ ((data.page - 1) * 10 + 1) +' 到 '+ data.records +' 项，共 '+ data.records +' 项</label>');
            }else{
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 '+ ((data.page - 1) * 10 + 1) +' 到 '+ (data.page * 10) +' 项，共 '+ data.records +' 项</label>');
            }
            $("#pagesize").val(data.pagesize);
        });
    },
    pageSizeChange:function(dom){
        var pagesize = $(dom).val();
        channelManagement.pageRenderData(1, pagesize);
    },
    writeExceptionMessage:function(td){
        var curtd = td;
        var ordersMailCode = $(".ordersMailCode",$(curtd).parent().parent()).text();
        layer.prompt(function(val, index){
            $.ajax({
                url: "updateExceptionMessage",
                cache: false,
                type: "GET",
                //async: false,
                data: {
                    message: val,
                    ordersMailCode:ordersMailCode
                },
                success: function (data) {
                    if (data) {
                        $(curtd).parent().prev("td").text(val);
                    }
                }
            });
            layer.close(index);
        });
    },
    showUpload:function(){
        var content = '<div class="panel-body">'+
            '<div class="alert alert-info" role="alert">'+
            '<p>请耐性等待上传，不要重复操作!</p>'+
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
            title: '上传异常备注csv',
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
                ,ext: 'csv' //那么，就只会支持这种格式的上传。注意是用|分割。
                ,type: 'file'
                ,unwrap: false
                ,title: '请上传文件'
                ,method:'post'
                ,success: function(res, input){
                    if(res.status == 200){
                        common.showMessage(res.status,res.message);
                    }else{
                        common.showMessage(res.status,res.message);
                    }
                }
            });
        });
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





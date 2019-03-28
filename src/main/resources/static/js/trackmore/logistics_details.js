var channelManagement = {
    save: function () {
        $("#channelManagement").ajaxForm({
            success:function (i,j) {

                if(i.status == 200){
                    parent.common.showMessage(i.status,i.message);
                }else{
                    parent.common.showMessage(i.status,i.message);
                }
             }
        });
    },
    showUpload:function(){
        var content = '<div class="panel-body">'+
            '<div class="alert alert-info" role="alert">'+
            '<p>模板下载: <a href="../excel/orderprocess_template.csv">模板</a></p>'+
            '<p style="color: red">注意：带*号字段不能为空,时间格式为:yyyy/m/d h:mm,不能修改不存在或未获取到节点状态的订单，导入需要时间，请耐心等待</p>'+
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
            title: '上传物流状态对应时间节点信息',
            maxmin: false,
            shadeClose: false, //点击遮罩关闭层
            area: ['560px', '350px'],
            content: content,
        });
    },
    upload:function(){
        layui.use('upload', function(){
            layui.upload({
                url: 'updateOrdersProcess'
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
    downloadCsv:function(){
        $.ajax({
            url: "get",
            cache: false,
            type: "GET",
            //async: false,
            data: {
                page: 0,
                rows: 0,
                erpOrdersId1: $("#erpOrdersId").val(),
                ordersMailCode1: $("#ordersMailCode").val(),
                platform: $("#form [name='platform']").val(),
                channelNames:$("#form [name='channelNames']").val(),
                startTime: $("#start_date").val(),
                endTime: $("#end_date").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "详情信息.csv";
                    var dataArr = data.rows;
                    for(var i=0;i<dataArr.length;i++){
                        dataArr[i].erpOrdersId = "'" + dataArr[i].erpOrdersId;
                        dataArr[i].ordersMailCode = "'" + dataArr[i].ordersMailCode;
                    }
                    var title=["发货日期","订单号","订单追踪号","平台","国家","渠道","状态803同步成功1上网2封发3交航4落地5妥投","异常状态16查询不到17投递失败18包裹异常19运输过久20到达待取","上网时间","封发时间","交航时间","落地时间","妥投时间","异常备注"];
                    var column=["ordersOutTime","erpOrdersId","ordersMailCode","platform","country","channelName","trackStatus","exceptionStatus","internetDate","baleDate","trafficDate","landingDate","deliveredDate","remark"];
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
            erpOrdersId1: $("#erpOrdersId").val(),
            ordersMailCode1: $("#ordersMailCode").val(),
            platform: $("#form [name='platform']").val(),
            channelNames:$("#form [name='channelNames']").val(),
            startTime: $("#start_date").val(),
            endTime: $("#end_date").val(),
        }, function (data) {
            $("table tbody").empty();
            var trs;
            $.each(data.rows,function(index,item){
            	 var statusTrack = "";
                 if(item.trackStatus == 1){statusTrack = "上网";}
                 else if(item.trackStatus == 2){statusTrack = "封发";}
                 else if(item.trackStatus == 3){statusTrack = "交航";}
                 else if(item.trackStatus == 4){statusTrack = "落地";}
                 else if(item.trackStatus == 5){statusTrack = "妥投";}
                 else if(item.trackStatus == 803){statusTrack = "生成request task成功";}
                 else{
                	 statusTrack = "已同步"; 
                 }
              	 var exceptionflag = "";
                 if(item.exceptionStatus == 15){exceptionflag = "已请求,但没有物流信息";}
                 else if(item.exceptionStatus == 10){exceptionflag = "未上网";}
                 else if(item.exceptionStatus == 11){exceptionflag = "未封发";}
                 else if(item.exceptionStatus == 12){exceptionflag = "未交航";}
                 else if(item.exceptionStatus == 13){exceptionflag = "未落地";}
                 else if(item.exceptionStatus == 14){exceptionflag = "未妥投";}
                 else if(item.exceptionStatus == 16){exceptionflag = "查询不到";}
                 else if(item.exceptionStatus == 17){exceptionflag = "投递失败";}
                 else if(item.exceptionStatus == 18){exceptionflag = "可能异常";}
                 else if(item.exceptionStatus == 19){exceptionflag = "运输过久";}
                 else if(item.exceptionStatus == 20){exceptionflag = "到达代取";}
                 else if(item.exceptionStatus == 602){exceptionflag = "erp数据异常关键字段为空";}
                 else if(item.exceptionStatus == 604){exceptionflag = "缺失trackmore物流渠道编码";}
                 else if(item.exceptionStatus == 607){exceptionflag = "没有找到物流状态";}
                 else if(item.exceptionStatus == 609){exceptionflag = "根据关键字未匹配到状态";}
                 else{
                	 exceptionflag = "正常"; 
                 }

                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.ordersOutTime +'</td>'+
                    '<td>'+ item.erpOrdersId +'</td>'+
                    '<td>'+ item.ordersMailCode +'</td>'+
                    '<td>'+ item.platform +'</td>'+
                    '<td>'+ item.country +'</td>'+
                     '<td>'+ item.channelName +'</td>'+
                     '<td>'+ statusTrack +'</td>'+
                     '<td>'+ exceptionflag +'</td>'+
                     '<td>'+ item.internetDate +'</td>'+
                     '<td>'+ item.baleDate +'</td>'+
                     '<td>'+ item.trafficDate +'</td>'+
                     '<td>'+ item.landingDate +'</td>'+
                     '<td>'+ item.deliveredDate +'</td>'+
                     '<td>'+ item.remark +'</td>'+
                    '<td><div class="dropdown">' +
                    '<button class="btn btn-sm btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">操作 <span class="caret"></span></button> ' +
                    '<ul class="dropdown-menu" aria-labelledby="dropdownMenu1"> ' +
                    '<li><a onclick="common.showAddOrEdit(\'详细明细\',\'detailedview?ordersMailCode='+ item.ordersMailCode +'\')" >明细</a></li>'+
                    '<li><a onclick="common.showAddOrEdit3(\'修改信息\',\'getOrderToUpdate?ordersMailCode='+ item.ordersMailCode +'\')" >修改</a></li>'+
                    '</ul></div>' +
                    '</td>'+
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
    $("#ordersOutTime").jeDate({
        format: "YYYY-MM-DD hh:mm:ss",
        //isinitVal:true,
        isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    $("#internetDate").jeDate({
        format: "YYYY-MM-DD hh:mm:ss",
        //isinitVal:true,
        isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    $("#baleDate").jeDate({
        format: "YYYY-MM-DD hh:mm:ss",
        //isinitVal:true,
        isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    $("#trafficDate").jeDate({
        format: "YYYY-MM-DD hh:mm:ss",
        //isinitVal:true,
        isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    $("#landingDate").jeDate({
        format: "YYYY-MM-DD hh:mm:ss",
        //isinitVal:true,
        isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    $("#deliveredDate").jeDate({
        format: "YYYY-MM-DD hh:mm:ss",
        //isinitVal:true,
        isTime:true, //isClear:false,
        minDate: "2014-09-19 00:00:00"
    });
    channelManagement.pagefunc();

})();




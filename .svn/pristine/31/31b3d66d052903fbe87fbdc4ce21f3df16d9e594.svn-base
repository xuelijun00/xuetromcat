var channelManagement = {
    save: function () {
        $("#channelManagement").ajaxForm({
            success:function (i,j) {
                var message = JSON.parse(i);
                if(message.status == 200){
                    parent.common.showMessage(message.status,message.message);
                }else{
                    parent.common.showMessage(message.status,message.message);
                }
             }
        });

    },
    downloadCsv:function(){
        $.ajax({
            url: "../get",
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
                    var values = [];
                    for(var i=0;i<dataArr.length;i++){
                        var row = {};
                        row.ordersOutTime = dataArr[i].ordersOutTime;
                        row.erpOrdersId = "'" + dataArr[i].erpOrdersId;
                        row.ordersMailCode = "'" + dataArr[i].ordersMailCode;
                        row.platform = dataArr[i].platform;
                        row.channelName = dataArr[i].channelName;
                        row.country = dataArr[i].country;
                        row.remark = dataArr[i].remark;
                        var statusTrack = "";
                        if(dataArr[i].trackStatus == 1){statusTrack = "上网";}
                        else if(dataArr[i].trackStatus == 2){statusTrack = "封发";}
                        else if(dataArr[i].trackStatus == 3){statusTrack = "交航";}
                        else if(dataArr[i].trackStatus == 4){statusTrack = "落地";}
                        else if(dataArr[i].trackStatus == 5){statusTrack = "妥投";}
                        else if(dataArr[i].trackStatus == 803){statusTrack = "生成request task成功";}
                        else{
                            statusTrack = "已同步";
                        }
                        row.trackStatus = statusTrack;
                        var exceptionflag = "";
                        if(dataArr[i].exceptionStatus == 15){exceptionflag = "已请求,但没有物流信息";}
                        else if(dataArr[i].exceptionStatus == 16){exceptionflag = "查询不到";}
                        else if(dataArr[i].exceptionStatus == 17){exceptionflag = "投递失败";}
                        else if(dataArr[i].exceptionStatus == 18){exceptionflag = "可能异常";}
                        else if(dataArr[i].exceptionStatus == 19){exceptionflag = "运输过久";}
                        else if(dataArr[i].exceptionStatus == 20){exceptionflag = "到达代取";}
                        else if(dataArr[i].exceptionStatus == 602){exceptionflag = "erp数据异常关键字段为空";}
                        else if(dataArr[i].exceptionStatus == 604){exceptionflag = "缺失trackmore物流渠道编码";}
                        else if(dataArr[i].exceptionStatus == 607){exceptionflag = "没有找到物流状态";}
                        else if(dataArr[i].exceptionStatus == 609){exceptionflag = "根据关键字未匹配到状态";}
                        else{
                            exceptionflag = "正常";
                        }
                        row.exceptionStatus = exceptionflag;
                        for(var j=0;j<15;j++){
                            if(dataArr[i].nodeDtos[j]){
                                row["date" + (j+1)] = new Date(dataArr[i].nodeDtos[j].process_time).Format("yyyy-MM-dd HH:mm:ss")
                            }else{
                                row["date" + (j+1)] = "";
                            }
                        }
                        values.push(row);
                    }
                    var title=["发货日期","订单号","订单追踪号","平台","国家","渠道","状态803同步成功1上网2封发3交航4落地5妥投","异常状态16查询不到17投递失败18包裹异常19运输过久20到达待取","异常备注","上网时间","封发时间","交航时间","落地时间","妥投时间","时间6","时间7","时间8","时间9","时间10","时间11","时间12","时间13","时间14","时间15"];
                    var column=["ordersOutTime","erpOrdersId","ordersMailCode","platform","country","channelName","trackStatus","exceptionStatus","remark","date1","date2","date3","date4","date5","date6","date7","date8","date9","date10","date11","date12","date13","date14","date15"];
                    exportDataToCSV(fileName,values,title,column);
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
        $.getJSON('../get', {
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
                    '<td>'+ item.remark +'</td>'+
                    '<td>' +
                    '<button class="btn btn-sm btn-info dropdown-toggle"  onclick="common.showAddOrEdit(\'详细明细\',\'../detailedview?ordersMailCode='+ item.ordersMailCode +'\')" >明细</button>'+
                    '</td>';
                    if(item.nodeDtos == null || item.nodeDtos.length <= 0){
                        tr += '<td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td><td></td>'
                    }else{
                        for(var j = 0;j<item.nodeDtos.length;j++){
                            tr += '<td>'+ new Date(item.nodeDtos[j].process_time).Format("yyyy-MM-dd HH:mm:ss") +'</td>';
                        }
                        for(var j = 0;j<15 - item.nodeDtos.length;j++){
                            tr += '<td></td>';
                        }
                    }
                    tr += '</tr>';
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

    // 对Date的扩展，将 Date 转化为指定格式的String
    // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
    // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
    // 例子：
    // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
    // (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
    Date.prototype.Format = function (fmt) { //author: meizz
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
    channelManagement.pagefunc();

})();





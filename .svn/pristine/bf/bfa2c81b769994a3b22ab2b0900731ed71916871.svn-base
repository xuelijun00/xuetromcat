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
                    var title=["平台","国家","渠道","发货时间","订单追踪码"];
                    var column=["platform","buyerCountry","channelName","ordersOutTime","ordersMailCode"];
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
                if(item.trackStatus == 602){
                    ex = "erp数据异常关键字段为空";
                }else if(item.trackStatus == 604){
                    ex = "缺失trackmore物流渠道编码,请补充物流渠道基础信息";
                }else if(item.trackStatus == 607){
                    ex = "没有找到物流状态";
                }else if(item.trackStatus == 609){
                    ex = "根据关键字未匹配到状态";
                }else if(item.trackStatus == 10){
                    ex = "48h未上网";
                }else if(item.trackStatus == 11){
                    ex = "72h未封发";
                }else if(item.trackStatus == 12){
                    ex = "96h未交航";
                }else if(item.trackStatus == 13){
                    ex = "120h未落地";
                }else if(item.trackStatus == 14){
                    ex = "144h未妥投";
                }else if(item.trackStatus == 15){
                    ex = "暂无物流信息";
                }

                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.platform +'</td>'+
                    '<td>'+ item.buyerCountry +'</td>'+
                    '<td>'+ item.channelName +'</td>'+
                    '<td>'+ item.ordersOutTime +'</td>'+
                    '<td>'+ item.ordersMailCode +'</td>'+
                    '<td>'+ ex +'</td>'+
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
    channelManagement.pagefunc();

})();





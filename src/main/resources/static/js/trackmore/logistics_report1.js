var channelManagement = {
    downloadCsv:function(){
        $.ajax({
            url: "kylin/get1",
            cache: false,
            type: "GET",
            //async: false,
            data: {
                page: 0,
                rows: 0,
                platform:$("#form [name='platform']").val(),
                country:$("#form [name='country']").val(),
                channel: $("#form [name='channel']").val(),
                startTime: $("#form [name='startTime']").val(),
                endTime: $("#form [name='endTime']").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "物流信息统计报表.csv";
                    var dataArr = data.rows;
                    var title=["平台","国家","渠道","付款订单","发货总数","订单执行占比","上网数","上网率(%)","平均上网天数","封发数","封发率(%)","平均封发天数","交航数","交航率(%)","平均交航天数","落地数","落地率(%)","平均落地天数","妥投数","妥投率(%)","平均妥投天数","总重量","平均重量","总运费","平均单价（/g）"];
                    var column=["platform","buyerCountry","channelName","paymentcount","shipcount","orderexecutionPercent","internetcount","internetPercent","internetDate","balecount","balePercent","baleDate","trafficcount","trafficPercent","trafficDate","landingcount","landingPercent","landingDate","deliveredcount","deliveredPercent","deliveredDate","heavi","avgheavi","fee","avgprice"];
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
        $.getJSON('kylin/get1', {
            page: page,
            rows: rows,
            platform:$("#form [name='platform']").val(),
            country:$("#form [name='country']").val(),
            channel: $("#form [name='channel']").val(),
            startTime: $("#form [name='startTime']").val(),
            endTime: $("#form [name='endTime']").val(),
        }, function (data) {
            $("table tbody").empty();
            var trs;
            $.each(data.rows,function(index,item){
                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.platform +'</td>'+
                    '<td>'+ item.buyerCountry +'</td>'+
                    '<td>'+ item.channelName +'</td>'+
                    '<td>'+ item.paymentcount +'</td>'+
                    '<td>'+ item.shipcount +'</td>'+
                    '<td>'+ (item.orderexecutionPercent!=''?(item.orderexecutionPercent*100).toFixed(2)+'%':'0%') +'</td>'+
                    '<td>'+ (item.internetcount!=''?item.internetcount:0) +'</td>'+
                    '<td>'+ (item.internetPercent!=''?(item.internetPercent*100).toFixed(2)+'%':'0%') +'</td>'+
                    '<td>'+ (item.internetDate!=''?item.internetDate.toFixed(1):0) +'</td>'+
                    '<td>'+ (item.balecount!=''?item.balecount:0) +'</td>'+
                    '<td>'+ (item.balePercent!=''?(item.balePercent*100).toFixed(2)+'%':'0%') +'</td>'+
                    '<td>'+ (item.baleDate!=''?item.baleDate.toFixed(1):0) +'</td>'+
                    '<td>'+ (item.trafficcount!=''?item.trafficcount:0) +'</td>'+
                    '<td>'+ (item.trafficPercent!=''?(item.trafficPercent*100).toFixed(2)+'%':'0%') +'</td>'+ //
                    '<td>'+ (item.trafficDate!=''?item.trafficDate.toFixed(1):0) +'</td>'+
                    '<td>'+ (item.landingcount !=''?item.landingcount:0) +'</td>'+
                    '<td>'+ (item.landingPercent!=''?(item.landingPercent*100).toFixed(2)+'%':'0%') +'</td>'+
                    '<td>'+ (item.landingDate!=''?item.landingDate.toFixed(1):0) +'</td>'+
                    '<td>'+ (item.deliveredcount!=''?item.deliveredcount:0) +'</td>'+
                    '<td>'+ (item.deliveredPercent!=''?(item.deliveredPercent*100).toFixed(2)+'%':'0%') +'</td>'+
                    '<td>'+ (item.deliveredDate!=''?item.deliveredDate.toFixed(1):0) +'</td>'+
                    '<td>'+ item.heavi.toFixed(2) +'</td>'+
                    '<td>'+ item.avgheavi.toFixed(2) +'</td>'+
                    '<td>'+ item.fee.toFixed(2) +'</td>'+
                    '<td>'+ item.avgprice.toFixed(2) +'</td>'+
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
        minDate: "2014-09-19 00:00:00"
    });
    $("#end_date").jeDate({
        format: "YYYY-MM-DD",
        minDate: "2014-09-19 00:00:00"
    });
    channelManagement.pagefunc();
})();





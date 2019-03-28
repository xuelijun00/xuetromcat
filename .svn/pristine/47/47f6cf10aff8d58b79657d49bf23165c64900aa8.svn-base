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
                country:$("#form [name='country']").val(),
                erpOrdersId:$("#form [name='erpOrdersId']").val(),
                ordersMailCode: $("#form [name='ordersMailCode']").val(),
                startTime: $("#start_date").val(),
                endTime: $("#end_date").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "订单信息报表.csv";
                    var dataArr = data.rows;
                    var title=["订单号-内","发货仓库","买家城市","买家州","买家国家","运输方式","销售帐号","订单类型","订单状态","进老erp时间","实际运费","发货重量","尺寸","发货时间","追踪号2API获取","获取追踪码时间","发货方式","订单打印时间","国家简称","渠道编码","物流优选系统yksid","数据抓取时间","注册状态()"];
                    var column=["erpOrdersId","warehouseid","buyerCity","buyerState","buyerCountry","shippingMethod","salesAccount","ordersType","ordersStatus","ordersExportTime","fee","heavi","size","ordersOutTime","ordersMailCode","ordersMailTime","erpPostOffice","ordersPrintTime","ebayCounycode","freightcode","yksid","createTime","trackStatus"];
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
            country:$("#form [name='country']").val(),
            erpOrdersId:$("#form [name='erpOrdersId']").val(),
            ordersMailCode: $("#form [name='ordersMailCode']").val(),
            startTime: $("#start_date").val(),
            endTime: $("#end_date").val(),
        }, function (data) {
            $("table tbody").empty();
            var trs;
            $.each(data.rows,function(index,item){
                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.erpOrdersId +'</td>'+
                    '<td>'+ item.warehouseid +'</td>'+
                    '<td>'+ item.buyerCity +'</td>'+
                    '<td>'+ item.buyerState +'</td>'+
                     '<td>'+ item.buyerCountry +'</td>'+
                     '<td>'+ item.shippingMethod +'</td>'+
                     '<td>'+ item.salesAccount +'</td>'+
                     '<td>'+ item.ordersType +'</td>'+
                     '<td>'+ item.ordersStatus +'</td>'+
                     '<td>'+ item.ordersExportTime +'</td>'+
                     '<td>'+ item.fee +'</td>'+
                     '<td>'+ item.heavi +'</td>'+
                     '<td>'+ item.size +'</td>'+
                     '<td>'+ item.ordersOutTime +'</td>'+
                     '<td>'+ item.ordersMailCode +'</td>'+
                     '<td>'+ item.ordersMailTime +'</td>'+
                     '<td>'+ item.erpPostOffice +'</td>'+
                     '<td>'+ item.ordersPrintTime +'</td>'+
                     '<td>'+ item.ebayCounycode +'</td>'+
                     '<td>'+ item.freightcode +'</td>'+
                     '<td>'+ item.yksid +'</td>'+
                     '<td>'+ item.createTime +'</td>';
                    if(item.trackStatus == 0){tr += '<td>聚合</td>'}
                    else if(item.trackStatus == 1){tr += '<td>上网</td>'}
                    else if(item.trackStatus == 2){tr += '<td>封发</td>'}
                    else if(item.trackStatus == 3){tr += '<td>交航</td>'}
                    else if(item.trackStatus == 4){tr += '<td>落地</td>'}
                    else if(item.trackStatus == 5){tr += '<td>妥投</td>'}
                    else if(item.trackStatus == 602){tr += '<td>erp数据异常关键字段为空</td>'}
                    else if(item.trackStatus == 604){tr += '<td>缺失物流渠道编码</td>'}
                    else if(item.trackStatus == 607){tr += '<td>没有找到物流状态</td>'}
                    else if(item.trackStatus == 803){tr += '<td>生成request task成功</td>'}
                    tr += '</tr>';
                trs += tr;
            });
            $("table tbody").html(trs);
            $("#pageinfo").empty();
            if(page == $("#pagetotal").val()){
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 '+ ((data.page - 1) * 10 + 1<0?0:(data.page - 1) * 10 + 1) +' 到 '+ data.records +' 项，共 '+ data.records +' 项</label>');
            }else{
                $("#pageinfo").html('<label>每页 <select class="input-sm" id="pagesize" onchange="channelManagement.pageSizeChange(this)"><option value="10">10</option><option value="25">25</option><option value="50">50</option></select> 条记录 显示 '+ ((data.page - 1) * 10 + 1<0?0:(data.page - 1) * 10 + 1) +' 项，共 '+ data.records +' 项</label>');
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





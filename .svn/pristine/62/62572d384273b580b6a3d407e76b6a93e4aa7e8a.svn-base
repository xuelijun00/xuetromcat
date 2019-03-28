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
                erpOrdersId1: $("#erpOrdersId").val(),
                ordersMailCode1: $("#ordersMailCode").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "详情信息.csv";
                    var dataArr = data.rows;
                    var title=["发货日期","订单号","订单追踪号","平台","国家","渠道","状态"];
                    var column=["ordersOutTime","erpOrdersId","ordersMailCode","platform","buyerCountry","channelName","trackStatus"];
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
                 else if(item.trackStatus == 608){statusTrack = "已请求,暂无物流信息";}
                 else{
                	 statusTrack = "已同步"; 
                 }
            	
                var tr = '<tr>'+
                    '<td>'+ (index + 1) +'</td>'+
                    '<td>'+ item.ordersOutTime +'</td>'+
                    '<td>'+ item.erpOrdersId +'</td>'+
                    '<td>'+ item.ordersMailCode +'</td>'+
                    '<td>'+ item.platform +'</td>'+
                    '<td>'+ item.buyerCountry +'</td>'+
                     '<td>'+ item.channelName +'</td>'+
                     '<td>'+ statusTrack +'</td>'+
                     '<td>' +
                     '<button class="btn btn-sm btn-info dropdown-toggle"  onclick="common.showAddOrEdit(\'详细明细\',\'detailedview?ordersMailCode='+ item.ordersMailCode +'\')" >明细</button>'+
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
    channelManagement.pagefunc();

})();





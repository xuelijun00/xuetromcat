var channelManagement = {
    downloadCsv:function(){
        $.ajax({
            url: "../get1/redis",
            cache: false,
            type: "GET",
            //async: false,
            data: {
                page: 0,
                rows: 0,
                channel: $("#form [name='channel']").val(),
                startTime: $("#form [name='startTime']").val(),
                endTime: $("#form [name='endTime']").val(),
            },
            success: function (data) {
                if (data) {
                    var fileName = "物流信息统计报表-渠道"+ $("#form [name='channel']").val() +".csv";
                    var title=["渠道","日期","付款订单","发货总数","订单执行占比","总重量","平均重量","总运费","平均单价（/g）"];
                    var column=["channelName",'orderOutTime',"paymentcount","shipcount","orderexecutionPercent","heavi","avgheavi","fee","avgprice"];
                    for(var i=0;i<data.nodes.length;i++){
                        title.push(data.nodes[i].statusName + "数");
                        title.push(data.nodes[i].statusName + "率(%)");
                        title.push("平均" + data.nodes[i].statusName + "天数");

                        column.push("count" + i);
                        column.push("rate" + i);
                        column.push("differDay" + i);
                    }

                    var dataArr = data.values;
                    var values = [];
                    for(var i=0;i<dataArr.length;i++){
                        var row = {};
                        row.channelName = dataArr[i].channelName;
                        row.orderOutTime = dataArr[i].orderOutTime;
                        row.paymentcount = dataArr[i].paymentcount;
                        row.shipcount = dataArr[i].shipcount;
                        row.orderexecutionPercent = (dataArr[i].orderexecutionPercent!=''?(dataArr[i].orderexecutionPercent*100).toFixed(2)+'%':'0%');
                        row.heavi = dataArr[i].heavi;
                        row.avgheavi = dataArr[i].avgheavi;
                        row.fee = dataArr[i].fee;
                        row.avgprice = dataArr[i].avgprice;

                        var item = dataArr[i].nodeDtoList;
                        for(var j=0;j<item.length;j++){
                            row["count" + j] = (item[j].count!=0?item[j].count.toFixed(2):0)
                            row["rate" + j] = (item[j].rate!=0?(item[j].rate*100).toFixed(2)+'%':'0%')
                            row["differDay" + j] = (item[j].differDay!=0?item[j].differDay.toFixed(1):0)
                        }
                        values.push(row);
                    }
                    exportDataToCSV(fileName,values,title,column);
                }
            }
        });
    },
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
    debugger;
    $.getJSON('../get1/redis', {
        channel: $("#form [name='channel']").val(),
        startTime: $("#form [name='startTime']").val(),
        endTime: $("#form [name='endTime']").val(),
      
    }, function (data) {
        var dataValue = data.values;
        $("table tbody").empty();
        var trs = "";
        $.each(dataValue, function (index, item) {
            var tr = '<tr>' +
                '<td>' + (index + 1) + '</td>' +
                '<td>' + item.channelName + '</td>' +
                '<td>' + item.orderOutTime + '</td>' +
                '<td>' + item.paymentcount + '</td>' +
                '<td>' + item.shipcount + '</td>'+
                '<td>'+ (item.orderexecutionPercent!=''?(item.orderexecutionPercent*100).toFixed(2)+'%':'0%') +'</td>';
            for (var i = 0; i<item.nodeDtoList.length; i++) {
                tr +=  '<td>'+ (item.nodeDtoList[i].count!=''?item.nodeDtoList[i].count:0) +'</td>'+
                    '<td>'+ (item.nodeDtoList[i].rate!=''?(item.nodeDtoList[i].rate*100).toFixed(2)+'%':'0%') +'</td>'+
                    '<td>'+ (item.nodeDtoList[i].differDay!=''?item.nodeDtoList[i].differDay.toFixed(1):0) +'</td>';
            }
            tr += '<td>' + item.heavi.toFixed(2) + '</td>' +
                '<td>' + item.avgheavi.toFixed(2) + '</td>' +
                '<td>' + item.fee.toFixed(2) + '</td>' +
                '<td>' + item.avgprice.toFixed(2) + '</td>' +
                '</tr>';
            trs += tr;
        });
        $("table tbody").html(trs);
    });
})();





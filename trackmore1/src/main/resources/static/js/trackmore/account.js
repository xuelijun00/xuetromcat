

var accountManagement = {
    save: function () {
        $("#accountForm").ajaxForm({
            success:function (i,j) {
                var message = JSON.parse(i);
                if(message.status == 200){
                    parent.common.close(message.status);
                    layer.msg('添加账号成功！', {time: 3000});
                }else{
                    layer.msg('添加账号失败！', {time: 3000});
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
                    account:$("input[name='account']").val(),
                    createDate:$("#createTime").val()
                },
                success: function (data) {
                    if (data) {
                        var fileName = "账号.csv";
                        var dataArr = data.rows;
                        var title=["账号名称","key","创建时间","可用额度","状态(1、正常2、禁用)"];
                        var column=["account","apiKey","createTime","dataCount","status"];
                        exportDataToCSV(fileName,dataArr,title,column);
                    }
                }
            });
        },
     enable:function(obj){
           $.ajax({
                  url: "enable",
                  cache: false,
                  type: "POST",
                  async: false,
                  data: {
                      id:$(obj)[0]
                  },
                  success: function (data) {
                     layer.msg(data.message, {time: 3000});
                     location.reload();
                  }
              });

     },
     disable:function(obj){
          $.ajax({
               url: "disable",
               cache: false,
               type: "POST",
               async: false,
               data: {
                   id:$(obj)[0]
               },
               success: function (data) {
                  layer.msg(data.message, {time: 3000});
                  location.reload();
               }
           });
     }
};

$("#createTime").jeDate({
    format: "YYYY-MM-DD",
    //isinitVal:true,
    //isTime:true, //isClear:false,
    minDate: "2014-09-19 00:00:00"
});
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
            var rows = 10;

            $.getJSON('get', {
                page: page,
                rows: rows,
                account:$("input[name='account']").val(),
                createDate:$("#createTime").val()?$("#createTime").val():""
            }, function (data) {
                $("table tbody").empty();
                var trs;
                $.each(data.rows,function(index,item){
                    var str = '';
                    var status='';
                    if(item.status ==2){
                        str ="<li><a onclick='accountManagement.enable("+item.id+")' >启用</a></li>";
                        status='<td>停用</td>';
                    }else{
                        str ="<li><a onclick='accountManagement.disable("+item.id+")' >禁用</a></li>";
                        status='<td>启用</td>';
                    }


                    var tr = '<tr>'+
                        '<td>'+ (index + 1) +'</td>'+
                        '<td>'+ item.account +'</td>'+
                        '<td>'+ item.apiKey +'</td>'+
                        '<td>'+ item.createTime +'</td>'+
                        '<td>'+ item.dataCount +'</td>'+
                        status+
                        '<td><div class="dropdown"><button class="btn btn-sm btn-info dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">操作 <span class="caret"></span></button> <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">'+
                        str+
                         '</ul> </div></td>'+
                        '</tr>';
                    trs += tr;
                });
                $("table tbody").html(trs);
                $("#pageinfo").empty();
                if(page == $("#pagetotal").val()){
                    $("#pageinfo").html('<label>每页 10 条记录 显示 '+ ((data.page - 1) * 10 + 1) +' 到 '+ data.records +' 项，共 '+ data.records +' 项</label>');
                }else{
                    $("#pageinfo").html('<label>每页 10 条记录 显示 '+ ((data.page - 1) * 10 + 1) +' 到 '+ (data.page * 10) +' 项，共 '+ data.records +' 项</label>');
                }
            });
        }
    });
});



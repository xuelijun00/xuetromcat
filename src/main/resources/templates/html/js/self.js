/**
 * Created by 333 on 2017/2/10.
 */
//////////////////////////全选按钮
$(function() {
    $("#checkAll").click(
        function() {
            if (this.checked) {
                $("input[name='subBox']").prop('checked', true);
            } else {
                $("input[name='subBox']").prop('checked', false);
            }
        }
    );
});

/////页面日期插件  定义id为 start   end
/*
window.L.open('wDate', {
    'obj' : document.getElementById('start'),    //需绑定的对象
    'eventType' : 'focus',    //绑定的触发事件,默认click
    'params' : {'readOnly' : true}    //传递WdatePicker的参数
});
window.L.open('wDate', {
    'obj' : document.getElementById('end'),    //需绑定的对象
    'eventType' : 'focus',    //绑定的触发事件,默认click
    'params' : {'readOnly' : true}    //传递WdatePicker的参数
});
*/
/*
jeDate({
  dateCell:"#start_date",
  format:"YYYY-MM-DD",
  //isinitVal:true,
  //isTime:true, //isClear:false,
  minDate:"2014-09-19 00:00:00"
});
jeDate({
    dateCell:"#end_date",
    format:"YYYY-MM-DD",
    //isinitVal:true,
    //isTime:true, //isClear:false,
    minDate:"2014-09-19 00:00:00"
});
 */

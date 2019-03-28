//弹出一个页面层
function displayGallary(){
    layer.open({
        type: 2,
        title: '新增用户组',
        maxmin: false,
        shadeClose: true, //点击遮罩关闭层
        area : ['780px' , '450px'],
        content: 'addacc.html'
    });
}

function addAccount(){
    layer.open({
        type: 2,
        title: '新增track more账号',
        maxmin: false,
        shadeClose: true, //点击遮罩关闭层
        area : ['780px' , '250px'],
        content: 'add_account.html'
    });
}

function import(){
    layer.open({
        type: 2,
        title: '批量导入',
        maxmin: false,
        shadeClose: true, //点击遮罩关闭层
        area : ['780px' , '400px'],
        content: 'import.html'
    });
}

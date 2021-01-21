layui.use(['form','table'],function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table;

    table.render({
        elem: '#scheduledTable',
        url: '/scheduled/queryList',
        toolbar: '#toolbarDemo',
        cols: [[
            {field: 'taskId', title: '任务编号'},
            {field: 'className', title: '类名称'},
            {field: 'methodName', title: '方法名称'},
            {field: 'reqParams', title: '请求参数'},
            {field: 'taskCron', title: 'cron表达式'},
            {field: 'taskState', title: '任务状态',templet: '#switchState'},
            {field: 'createTime', title: '创建时间'},
            {field: 'updateTime', title: '更新时间'},
            {field: 'remark', title: '备注'},
            {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 10,
        page: true,
        skin: 'line'
    });


    /**
     * toolbar 头部监听事件
     */
    table.on('toolbar(currentTableFilter)', function (obj) {
        var data = obj.data;
        if (obj.event === 'add') {  // 监听新增操作
            var index = layer.open({
                title: '创建任务',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['45%', '55%'],
                content: '/page/addSchedule',
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        }
    });

    //监听状态操作
    form.on('switch(stateDemo)', function(obj){
        var taskState;
        if(obj.elem.checked) {
            taskState = 0;
        }else {
            taskState = 1;
        }
        var params = {
            'taskId': this.value,
            'taskState': taskState
        };

        $.ajax({
            beforeSend: function() {
                layer.load(2);
            },
            type: 'POST',
            url: '/scheduled/updateState',
            data: JSON.stringify(params),
            dataType: 'json',
            contentType: 'application/json; charset=UTF-8',
            success: function (res) {
                if(res.code == 200) {
                    //layer.msg(res.msg, { icon: 1 ,time: 1000});
                    table.reload('scheduledTable');
                }else {
                    layer.msg(res.msg, {icon: 5, time: 2000});
                }
            },
            complete: function() {
                layer.closeAll("loading");
            },
            error: function() {
                layer.msg('系统繁忙请稍后重试', {icon: 5, time: 2000});
            }
        });

    });


    /**
     * 监听表格选择
     */
    table.on('tool(currentTableFilter)', function (obj) {
        var data = obj.data;
        if (obj.event === 'edit') {//提交申请
            var index = layer.open({
                title: '编辑申请单',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['45%', '55%'],
                content: '/scheduled/toEdit?taskId='+data.taskId,
            });
        } else if (obj.event === 'delete') {
            layer.confirm('确定删除行么', function (index) {
                $.ajax({
                    beforeSend: function() {
                        layer.load(2);
                    },
                    type: 'POST',
                    url: '/scheduled/delTask',
                    data: {
                        taskId: data.taskId
                    },
                    dataType: 'json',
                    success: function (res) {
                        if(res.code == 200) {
                            layer.msg(res.msg, { icon: 1 ,time: 1000});
                            table.reload('scheduledTable');
                        }else {
                            layer.msg(res.msg, {icon: 5, time: 2000});
                        }
                    },
                    complete: function() {
                        layer.closeAll("loading");
                    },
                    error: function() {
                        layer.msg('系统繁忙请稍后重试', {icon: 5, time: 2000});
                    }
                });
            });
        }
    });

});


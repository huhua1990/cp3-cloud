layui.use(['form','table'],function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table;

    var dataTable = table.render({
        elem: '#myTaskTable',
        url: '/task/queryMyTask',
        toolbar: '#toolbarDemo',
        cols: [[
            {field: 'vacationId', title: '请假单'},
            {field: 'userId', title: '请假人'},
            {field: 'startTime', title: '请假开始时间'},
            {field: 'endTime', title: '请假结束时间'},
            {field: 'vacationContext', title: '请假原因'},
            {field: 'taskName', title: '任务名称'},
            {field: 'createTime',templet:'<div>{{ layui.util.toDateString(d.createTime, "yyyy-MM-dd HH:mm:ss") }}</div>', title: '任务创建时间'},
            {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: "center"}
        ]],
        limits: [10, 15, 20, 25, 50, 100],
        limit: 10,
        page: true,
        skin: 'line'
    });


    /**
     * 监听表格选择
     */
    table.on('tool(currentTableFilter)', function (obj) {
        var data = obj.data;
        if (obj.event === 'execute') {
            var index = layer.open({
                title: '任务审批',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['45%', '70%'],
                content: '/task/toTaskExec?vacationId=' + data.vacationId,
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        } else if (obj.event === 'examine') {  //查看审批详情
            var index = layer.open({
                title: '审批详情',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['80%', '80%'],
                content: '/vacation/provalDetail?flowId='+data.flowId + '&orderNo='+data.vacationId,
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        }
    });

});


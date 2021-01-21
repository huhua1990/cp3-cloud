layui.use(['form','table'],function () {
    var $ = layui.jquery,
        form = layui.form,
        table = layui.table;

        table.render({
        elem: '#vacationTable',
        url: '/vacation/queryList',
        toolbar: '#toolbarDemo',
        cols: [[
            {field: 'userName', orderNo: '请假单编号'},
            {field: 'userName', title: '请假人'},
            {field: 'startTime', title: '请假开始时间'},
            {field: 'endTime', title: '请假结束时间'},
            {field: 'typeName', title: '请假类型'},
            {field: 'vacationContext', title: '请假原因'},
            {field: 'createTime', title: '请假时间'},
            {field: 'vacationState', title: '状态',templet:function (d) {
                    if(d.vacationState == 0) {
                        return '<span style="color:#FF2F30;">未提交</span>';
                    }else if(d.vacationState == 1) {
                        return '<span style="color:#0087FF;">审批中</span>';
                    }else if(d.vacationState == 2) {
                        return '<span style="color:#c00;">已放弃</span>';
                    }else if(d.vacationState == 3) {
                        return '<span style="color:#008000;">审批完成</span>';
                    }

                }},
            {title: '操作', minWidth: 240, toolbar: '#currentTableBar', align: "center"}
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
        if (obj.event === 'add') {  // 监听删除操作
            var index = layer.open({
                title: '填写请假',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['100%', '100%'],
                content: '/vacation/toAdd',
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        }
    });


    /**
     * 监听表格选择
     */
    table.on('tool(currentTableFilter)', function (obj) {
        var data = obj.data;
        if (obj.event === 'submit') {//提交申请
            layer.confirm('确认提交申请吗?', function (index) {
                var vacationId = data.orderNo;
                $.ajax({
                    beforeSend: function() {
                        layer.load(2);
                    },
                    type: 'POST',
                    url: '/vacation/submitApply',
                    data: {
                        vacationId: vacationId
                    },
                    dataType: 'json',
                    success: function (res) {
                        if(res.code == 200) {
                            layer.msg(res.msg, { icon: 1 ,time: 1000});
                            table.reload('vacationTable');
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

        } else if (obj.event === 'examine') {  // 查看流程图
            var index = layer.open({
                title: '审批详情',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['80%', '80%'],
                content: '/vacation/provalDetail?flowId='+data.flowId + '&orderNo='+data.orderNo,
            });
            $(window).on("resize", function () {
                layer.full(index);
            });
        }else if (obj.event === 'edit') {  // 编辑
            var index = layer.open({
                title: '编辑申请单',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['80%', '80%'],
                content: '/vacation/toAdd?orderNo='+data.orderNo,
            });
        } else if (obj.event === 'delete') {
            layer.confirm('确定删除行么', function (index) {
                var modelId = data.id;
                $.ajax({
                    beforeSend: function() {
                        layer.load(2);
                    },
                    type: 'POST',
                    url: '/vacation/delVacation',
                    data: {
                        vacationId: data.orderNo
                    },
                    dataType: 'json',
                    success: function (res) {
                        if(res.code == 200) {
                            layer.msg(res.msg, { icon: 1 ,time: 1000});
                            table.reload('vacationTable');
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


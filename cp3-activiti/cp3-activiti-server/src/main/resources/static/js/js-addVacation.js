layui.use(['form','laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        $ = layui.$;
    laydate.render({
        elem: '#startTime',
        done: function (value, date, endDate) {
            var startDate = new Date(value).getTime();
            var endTime = new Date($('#endTime').val()).getTime();
            if (endTime < startDate) {
                layer.msg('结束时间不能小于开始时间');
                //$('#startTime').val($('#endTime').val());
                $('#startTime').val('');
            }
        }
    });

    laydate.render({
        elem: '#endTime',
        done: function (value, date, endDate) {
            var startDate = new Date($('#startTime').val()).getTime();
            var endTime = new Date(value).getTime();
            if (endTime < startDate) {
                layer.msg('结束时间不能小于开始时间');
                // $('#endTime').val($('#startTime').val());
                $('#endTime').val('');
            }
        }
    });

    //监听提交
    form.on('submit(saveBtn)', function (data) {
        $.ajax({
            type: 'POST',
            beforeSend: function() {
                layer.load(2);
            },
            url: '/vacation/saveOrder',
            data: JSON.stringify(data.field),
            dataType: 'JSON',
            contentType: 'application/json; charset=UTF-8',
            success: function (res) {
                if(res.code == '200') {
                    layer.msg(res.msg, { icon: 1 ,time: 1000},function () {
                        var iframeIndex = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(iframeIndex);
                        //刷新父页面的table
                        parent.layui.table.reload('vacationTable');
                    });
                }else {
                    layer.msg(res.msg, {icon: 5, time: 2000});
                }
            }, complete: function() {
                layer.closeAll("loading");
            },
            error: function() {
                layer.msg('系统繁忙请稍后重试', {icon: 5, time: 2000});
            }
        });
        return false;
    });

});
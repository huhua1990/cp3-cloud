layui.use(['form','laydate'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.$;

    //自定义验证规则
    form.verify({
        mustRadio: function (value, item) { //单选必选
            var xname = $(item).attr("id")
            var va = $(item).find("input[type='radio']:checked").val();
            if (typeof (va) == "undefined") {
                return $(item).attr("lay-verify-msg");
            }
        },
    });
    //监听提交
    form.on('submit(saveBtn)', function (data) {
        $.ajax({
            type: 'POST',
            beforeSend: function() {
                layer.load(2);
            },
            url: '/scheduled/addTask',
            data: JSON.stringify(data.field),
            dataType: 'JSON',
            contentType: 'application/json; charset=UTF-8',
            success: function (res) {
                if(res.code == '200') {
                    layer.msg(res.msg, { icon: 1 ,time: 1000},function () {
                        var iframeIndex = parent.layer.getFrameIndex(window.name);
                        parent.layer.close(iframeIndex);
                        //刷新父页面的table
                        parent.layui.table.reload('scheduledTable');
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
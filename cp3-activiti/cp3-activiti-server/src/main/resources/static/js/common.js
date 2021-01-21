function getLogin() {
    $.ajax({
        url: '/user/getLoginInfo',
        type: 'POST',
        dataType: 'JSON',
        success: function(res) {
            if(res.code == '200') {
                $('#js-user-name').text(res.data.userName)
            }else {
                location.href = "/login";
            }
        },
        error: function(error) {
            layer.closeAll('loading');
        }
    });
}
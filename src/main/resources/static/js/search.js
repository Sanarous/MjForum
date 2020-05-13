function search() {
    let keywords = $("#keywords").val();
    if($.trim(keywords).length === 0){
        layer.msg("搜索关键词不能为空哦",function () {
            
        });
        return false;
    }
    window.location.href = "/search?keywords=" + keywords;
}

//当光标在搜索框内时
$(document).keydown(function (event) {
    let keywords = $("#keywords").val();
    if ($.trim(keywords).length > 0 && event.keyCode === 13) {
        $('#searchBtn').triggerHandler('click');
    }
});


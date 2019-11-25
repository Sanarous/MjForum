function search() {
    var keywords = $("#keywords").val();
    if($.trim(keywords).length == 0){
        layer.msg("搜索关键词不能为空哦",function () {
            
        });
        return false;
    }
    window.location.href = "/search?keywords=" + keywords;
}
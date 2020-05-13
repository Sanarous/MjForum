function init(loginUserInfo) {
    let username,uid,avatar;

    if(loginUserInfo !== undefined && loginUserInfo !== null){
        username = loginUserInfo.username;
        uid = loginUserInfo.uid;

        $.ajax({
            type: "get",
            url: "/getavatar",
            data: {username:username},
            async: false,
            success: function (res) {
                if(res.status === 200){
                    avatar = res.data;
                }else{
                    avatar = "https://bestzuo.cn/images/forum/anonymous.jpg";
                }
            },
            error: function () {
                avatar = "https://bestzuo.cn/images/forum/anonymous.jpg";
            }
        });
    }

    if(username !== undefined && username !== null){
        //用户名和用户头像初始化
        $("#username").attr("value", username);
        $("#useravatar").attr("src", avatar);
    }

    if (($.trim(username).length > 0)) {
        //将登录注册按钮隐藏
        $('.layui-login').attr('class', "layui-hide");
        $('.layui-register').attr('class', "layui-hide");
        //添加一段div
        let publishHtml = "<li id='title-publish' class=\"layui-nav-item\">\n" +
            "    <a href=\"/publish\" style='font-size: 17px'><i style='font-size: 17px' class=\"layui-icon layui-icon-release\"></i>&nbsp;&nbsp;发帖</a>\n" +
            "  </li>\n" +
            "  <li id='title-notice' class=\"layui-nav-item\">\n" +
            "    <a href=\"/notice\" style='font-size: 17px'><i style='font-size: 17px' class=\"layui-icon layui-icon-notice\"></i>&nbsp;&nbsp;消息通知</a>\n" +
            "       <dl class=\"layui-nav-child\">" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getCommentNotice\"><i class=\"layui-icon layui-icon-dialogue\"></i>&nbsp;&nbsp;评论通知</a></dd>" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getCollectionNotice\"><i class=\"layui-icon layui-icon-star\"></i>&nbsp;&nbsp;收藏通知</a></dd>" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getFollowNotice\"><i class=\"layui-icon layui-icon-user\"></i>&nbsp;&nbsp;关注通知</a></dd>" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getPraiseNotice\"><i class=\"layui-icon layui-icon-praise\"></i>&nbsp;&nbsp;获赞通知</a></dd>" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getMajiangNotice\"><i class=\"layui-icon layui-icon-survey\"></i>&nbsp;&nbsp;码匠信箱</a></dd>" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getReportNotice\"><i class=\"layui-icon layui-icon-auz\"></i>&nbsp;&nbsp;举报反馈</a></dd>" +
            "           <dd><a style='font-size: 16px' href=\"/notice/getOtherNotice\"><i class=\"layui-icon layui-icon-service\"></i>&nbsp;&nbsp;其它通知</a></dd>" +
            "       </dl>"+
            "  </li>";
        let insertHtml = "<li id='title-user' class=\"layui-nav-item\">\n" +
            "        <a href=\"javascript:;\">\n" +
            "          <img src='"+ avatar +"' class=\"layui-nav-img user-small-avatar\">\n" +
            username +
            "        </a>\n" +
            "        <dl class=\"layui-nav-child\">\n" +
            "          <dd><a href=\"/user/" + uid + "\"><i class=\"layui-icon layui-icon-home\" style=\"color: #38941a;\"></i>&nbsp;&nbsp;我的主页</a></dd>\n" +
            "          <dd><a href=\"/user/settings\"><i class=\"layui-icon layui-icon-username\" style=\"color: #38941a;\"></i>&nbsp;&nbsp;基本资料</a></dd>\n" +
            "          <dd><a href=\"/safe\"><i class=\"layui-icon layui-icon-password\" style=\"color: #38941a;\"></i>&nbsp;&nbsp;安全设置</a></dd>\n" +
            "          <dd><a href=\"/logout?username=" + username + "\" onClick=\"return confirm('确定退出登录?');\"><i class=\"layui-icon layui-icon-vercode\" style=\"color: #38941a\"></i>&nbsp;&nbsp;退出</a></dd>\n" +
            "        </dl>\n" +
            "      </li>";
        $("#userinfo").html(publishHtml + insertHtml);
    }
}

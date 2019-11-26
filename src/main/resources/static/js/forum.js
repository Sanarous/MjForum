function init(username,token) {
    //赋值给输入框
    $("#username").attr("value", username);

    //去查询用户的头像
    getAvatar(username);

    if (($.trim(username).length > 0)) {
        //将登录注册按钮隐藏
        $('.layui-login').attr('class', "layui-hide");
        $('.layui-register').attr('class', "layui-hide");
        //添加一段div
        let publishHtml = "<li class=\"layui-nav-item\">\n" +
            "    <a href=\"/publish\">提问</a>\n" +
            "  </li>\n" +
            "  <li class=\"layui-nav-item\">\n" +
            "    <a href=\"/notice\">消息通知</a>\n" +
            "  </li>";
        let insertHtml = "<li class=\"layui-nav-item\">\n" +
            "        <a href=\"javascript:;\">\n" +
            "          <img src=\"\" class=\"layui-nav-img user-small-avatar\">\n" +
            username +
            "        </a>\n" +
            "        <dl class=\"layui-nav-child\">\n" +
            "          <dd><a href=\"/user/" + token + "\">我的主页</a></dd>\n" +
            "          <dd><a href=\"/user/settings\">基本资料</a></dd>\n" +
            "          <dd><a href=\"/safe\">安全设置</a></dd>\n" +
            "          <dd><a href=\"/logout?username=" + username + "\" onClick=\"return confirm('确定退出登录?');\">退出</a></dd>\n" +
            "        </dl>\n" +
            "      </li>";
        $("#userinfo").html(publishHtml + insertHtml);
    }
}

/** EasyWeb spa v3.1.8 date:2020-05-04 License By http://easyweb.vip */

layui.define(['table'], function (exports) {
    var setter = {
        baseServer: 'json/', // 接口地址，实际项目请换成http形式的地址
        pageTabs: false,   // 是否开启多标签
        cacheTab: true,  // 是否记忆Tab
        defaultTheme: '',  // 默认主题
        openTabCtxMenu: true,   // 是否开启Tab右键菜单
        maxTabNum: 20,  // 最多打开多少个tab
        viewPath: 'components', // 视图位置
        viewSuffix: '.html',  // 视图后缀
        reqPutToPost: true,  // req请求put方法变成post
        apiNoCache: true,  // ajax请求json数据不带版本号
        tableName: 'easyweb-spa',  // 存储表名
        /* 获取缓存的token */
        getToken: function () {
            var cache = layui.data(setter.tableName);
            if (cache) return cache.token;
        },
        /* 清除token */
        removeToken: function () {
            layui.data(setter.tableName, {key: 'token', remove: true});
        },
        /* 缓存token */
        putToken: function (token) {
            layui.data(setter.tableName, {key: 'token', value: token});
        },
        /* 当前登录的用户 */
        getUser: function () {
            var cache = layui.data(setter.tableName);
            if (cache) return cache.loginUser;
        },
        /* 缓存user */
        putUser: function (user) {
            layui.data(setter.tableName, {key: 'loginUser', value: user});
        },
        /* 获取用户所有权限 */
        getUserAuths: function () {
            var auths = [], user = setter.getUser();
            var authorities = user ? user.authorities : [];
            for (var i = 0; i < authorities.length; i++) {
                auths.push(authorities[i].authority);
            }
            return auths;
        },
        /* ajax请求的header */
        getAjaxHeaders: function (url) {
            var headers = [];
            var token = setter.getToken();
            if (token) headers.push({name: 'Authorization', value: 'Bearer ' + token.access_token});
            return headers;
        },
        /* ajax请求结束后的处理，返回false阻止代码执行 */
        ajaxSuccessBefore: function (res, url, obj) {
            if (res.code === 401) {  // 登录过期退出到登录界面
                setter.removeToken();
                layui.layer.msg('登录过期', {icon: 2, anim: 6, time: 1500}, function () {
                    location.replace('components/template/login/login.html');
                });
                return false;
            }
            return true;
        },
        /* 路由不存在处理 */
        routerNotFound: function (r) {
            layui.layer.alert('路由<span class="text-danger">' + r.path.join('/') + '</span>不存在', {
                title: '提示', offset: '30px', skin: 'layui-layer-admin', btn: [], anim: 6, shadeClose: true
            });
        }
    };
    /* table全局设置 */
    var token = setter.getToken();
    if (token && token.access_token) {
        layui.table.set({
            headers: {'Authorization': 'Bearer ' + token.access_token}
        });
    }
    setter.base_server = setter.baseServer;  // 兼容旧版
    exports('setter', setter);
});

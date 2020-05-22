/** EasyWeb spa v3.1.8 date:2020-05-04 License By http://easyweb.vip */
layui.config({
    version: '318',   // 更新组件缓存，设为true不缓存，也可以设一个固定值
    // base: '/static/assets/module/'
    base: getProjectUrl() + '/static/assets/module/'
}).extend({
    steps: 'steps/steps',
    notice: 'notice/notice',
    cascader: 'cascader/cascader',
    dropdown: 'dropdown/dropdown',
    fileChoose: 'fileChoose/fileChoose',
    Split: 'Split/Split',
    Cropper: 'Cropper/Cropper',
    tagsInput: 'tagsInput/tagsInput',
    citypicker: 'city-picker/city-picker',
    introJs: 'introJs/introJs',
    zTree: 'zTree/zTree'
}).use(['layer', 'setter', 'index', 'admin'], function () {
    let $ = layui.jquery;
    let layer = layui.layer;
    let setter = layui.setter;
    let index = layui.index;
    let admin = layui.admin;

    /* 检查是否登录 */
    if (!setter.getToken()) {
        // return location.replace('components/login/login.html');
    }

    /* 获取用户信息 */
    admin.req('userInfo.json', function (res) {
        if (200 === res.code) {
            setter.putUser(res.user);  // 缓存用户信息
            admin.renderPerm();  // 移除没有权限的元素
            $('#huName').text(res.user.nickName);
        } else {
            layer.msg('获取用户失败', {icon: 2, anim: 6});
        }
    });

    /* 加载侧边栏 */
    admin.req('menus.json', function (res) {
        index.regRouter(res);  // 注册路由
        index.renderSide(res);  // 渲染侧边栏
        // 加载主页
        index.loadHome({
            url: '#/console/workplace',
            name: '<i class="layui-icon layui-icon-home"></i>'
        });
    });
});

/* 获取项目根路径 */
function getProjectUrl() {
    let layuiDir = layui.cache.dir;
    if (!layuiDir) {
        let js = document.scripts, last = js.length - 1, src;
        for (let i = last; i > 0; i--) {
            if (js[i].readyState === 'interactive') {
                src = js[i].src;
                break;
            }
        }
        let jsPath = src || js[last].src;
        layuiDir = jsPath.substring(0, jsPath.lastIndexOf('/') + 1);
    }
    return layuiDir.substring(0, layuiDir.indexOf('assets'));
}

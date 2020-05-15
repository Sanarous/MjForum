package cn.bestzuo.mjforum.admin.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * 配置在线监听器
 */
public class MyHttpSessionListener implements HttpSessionListener {

    public static int online = 0;

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        online ++;
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        online --;
    }

}

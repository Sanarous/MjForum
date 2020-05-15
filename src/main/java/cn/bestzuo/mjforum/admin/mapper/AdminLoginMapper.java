package cn.bestzuo.mjforum.admin.mapper;

import cn.bestzuo.mjforum.admin.pojo.LoginInfo;

import java.util.List;

/**
 * 管理员登录Mapper
 */
public interface AdminLoginMapper {

    /**
     * 新增一条登录信息
     * @param ip
     * @param info
     * @param time
     * @return
     */
    int insertLoginInfo(String ip,String info,String time);

    /**
     * 查询最近5次的登录信息
     * @return
     */
    List<LoginInfo> queryAllLoginInfo();
}

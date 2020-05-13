package cn.bestzuo.zuoforum.admin.service;

import cn.bestzuo.zuoforum.admin.mapper.AdminLoginMapper;
import cn.bestzuo.zuoforum.admin.pojo.LoginInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员Service
 */
@Service
public class AdminLoginService {

    @Autowired
    private AdminLoginMapper loginMapper;

    @Transactional
    public int insertLoginInfo(String ip,String info,String time){
        return loginMapper.insertLoginInfo(ip, info, time);
    }

    /**
     * 查询最近5次的登录的信息
     * @return
     */
    public List<LoginInfo> queryAllLoginInfo(){
        return loginMapper.queryAllLoginInfo();
    }
}

package cg.software.settings.service.impl;

import cg.software.settings.Exception.MyException;
import cg.software.settings.dao.UserDao;
import cg.software.settings.domain.User;
import cg.software.settings.service.UserService;
import cg.software.utils.DateTimeUtil;
import cg.software.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {
    private UserDao sqlSession= SqlSessionUtil.getSqlSession().getMapper(UserDao.class);

    @Override
    public User login(String userAct, String userPwd, String userIp) throws MyException {
        Map<String,String> map=new HashMap<>();
        map.put("UserAct",userAct);
        map.put("UserPwd",userPwd);
        User user=sqlSession.login(map);
        if (user == null){
            throw new MyException("账号密码错误!");
        }
        String expireTimetime=user.getExpireTime();
        String sy= DateTimeUtil.getsystime();
        if (expireTimetime.compareTo(sy)<0){
            throw new MyException("账号已失效!");
        }

        String lock=user.getLockState();
        if ("0".equals(lock)){
            throw new MyException("账号被锁定!");
        }
        String ip=user.getAllowIps();
        if (!ip.contains(userIp)){
            throw new MyException("ip访问受限!");
        }
        return user;
    }

    @Override
    public List<User> getUserlist() {
    List<User> userList= sqlSession.getuserList();
    return userList;
    }
}

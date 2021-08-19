package cg.software.settings.web.controller;

import cg.software.settings.domain.User;
import cg.software.settings.service.UserService;
import cg.software.settings.service.impl.UserServiceImpl;
import cg.software.utils.MD5Util;
import cg.software.utils.PrintJson;
import cg.software.utils.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=request.getRequestURI();

        if("/crm/settings/user/login.do".equals(uri)){
            login(request,response);
        }else if("cg/software/settings/xxx".equals(uri)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        //获取登录信息，账号密码
        System.out.println("进入登录验证");
        String UserAct=request.getParameter("UserAct");
        String pwd=request.getParameter("UserPwd");
        String UserPwd= MD5Util.getMD5(pwd);
        String UserIp=request.getRemoteAddr();
        //调用业务层进行账号验证,可能涉及到事务所以用代理类，代理类中增加了事务提交功能
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        try{
            User user=userService.login(UserAct,UserPwd,UserIp);//调用业务层的登陆验证方法
            request.getSession().setAttribute("User",user);
            PrintJson.booleanPrintJson(response,true);
        }catch (Exception e){
            e.printStackTrace();
            //用户登录失败，传递错误信息
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",e.getMessage());
            PrintJson.ObjectPrintJson(response,map);
        }

    }

}

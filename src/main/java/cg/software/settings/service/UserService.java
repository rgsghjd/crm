package cg.software.settings.service;

import cg.software.settings.Exception.MyException;
import cg.software.settings.domain.User;

import javax.servlet.http.HttpServlet;
import java.util.List;

public interface UserService {


    User login(String userAct, String userPwd, String userIp) throws MyException;

    List<User> getUserlist();
}





















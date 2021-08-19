package cg.software.settings.dao;

import cg.software.settings.domain.User;

import java.util.List;
import java.util.Map;

public interface UserDao {
      List<User> getuserList();

    public User login(Map<String,String> map);


}

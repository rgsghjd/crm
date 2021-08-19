package cg.software.workbench.dao;

import cg.software.workbench.domain.Activity;
import cg.software.workbench.service.ActivityService;
import cg.software.workbench.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ActivityDao {
    int save(Activity a);

    List<Activity> pageList(Map<String,Object> map);


    int pageTotal(Map<String, Object> map);


    int delete(String[] ids);

    Activity edit(String id);

    int update(Activity a);

    Activity detail(String id);

    List<Activity> getList(String id);

    List<Activity> showList(Map<String, String> map);
}

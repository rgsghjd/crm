package cg.software.workbench.service;

import cg.software.workbench.domain.Activity;
import cg.software.workbench.domain.ActivityRemark;
import cg.software.workbench.vo.PageVo;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    int save(Activity a);

    PageVo<Activity> pageList(Map<String,Object> map);


    boolean delete(String[] ids);

    Map<String, Object> edit(String id);

    boolean update(Activity a);

    Activity detail(String id);

    List<ActivityRemark> remarkList(String id);

    boolean deleteRemark(String id);

    ActivityRemark editRemark(String id);

    boolean updateRemark(ActivityRemark ar);

    boolean insertRemark(ActivityRemark ar);

    List<Activity> getList(String id);

    List<Activity> showList(Map<String, String> map);
}

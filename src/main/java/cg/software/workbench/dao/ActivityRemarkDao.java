package cg.software.workbench.dao;

import cg.software.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkDao {
    
    int delete(String[] ids);

    int getCount(String[] ids);


    List<ActivityRemark> remarkList(String id);

    int deleteRemark(String id);

    ActivityRemark editRemark(String id);

    int updateRemark(ActivityRemark ar);

    int insertRemark(ActivityRemark ar);
}

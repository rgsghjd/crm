package cg.software.workbench.service.impl;

import cg.software.settings.dao.UserDao;
import cg.software.settings.domain.User;
import cg.software.utils.SqlSessionUtil;
import cg.software.workbench.dao.ActivityDao;

import cg.software.workbench.dao.ActivityRemarkDao;
import cg.software.workbench.domain.Activity;
import cg.software.workbench.domain.ActivityRemark;
import cg.software.workbench.service.ActivityService;
import cg.software.workbench.vo.PageVo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImp implements ActivityService{
    //获取ActivityDao实现类
    private ActivityDao activity= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public int save(Activity a) {
        //调用dao层插入活动数据
        int i=activity.save(a);

        return i;
    }

    @Override
    public PageVo<Activity> pageList(Map<String,Object> map) {
        List<Activity> dataList=activity.pageList(map);
        int total=activity.pageTotal(map);
        PageVo<Activity> vo=new PageVo<>();
        vo.setDataList(dataList);
        vo.setTotal(total);
        return vo;
    }

    @Override
    public boolean delete(String[] ids) {
        boolean flage=true;
        //获取要修改的市场活动描述表数
        int count1=activityRemarkDao.getCount(ids);

        //实际的市场活动描述影响数
        int count2=activityRemarkDao.delete(ids);
        if (count1!=count2){
            flage = false;
        }

        //实际删除的市场活动条数
        int count3=activity.delete(ids);

        if (count3!=ids.length){
            flage = false;
        }

        return flage;
    }

    @Override
    public Map<String, Object> edit(String id) {
        //获取用户的列表，在获取市场活动的记录
        List<User> list=userDao.getuserList();
        Activity a=activity.edit(id);
        Map<String,Object> map=new HashMap<>();
        map.put("uList",list);
        map.put("a",a);
        return map;
    }

    @Override
    public boolean update(Activity a) {
        boolean flage=true;
        int result=activity.update(a);
        if (result!=1){
            flage=false;
        }

        return flage;
    }

    @Override
    public Activity detail(String id) {
        Activity a=activity.detail(id);
        return a;
    }

    @Override
    public List<ActivityRemark> remarkList(String id) {
        List<ActivityRemark> ar=activityRemarkDao.remarkList(id);
        return ar;
    }

    @Override
    public boolean deleteRemark(String id) {
        boolean flag=true;
        int i=activityRemarkDao.deleteRemark(id);
        if(i!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public ActivityRemark editRemark(String id) {

        ActivityRemark ar=activityRemarkDao.editRemark(id);

        return ar;
    }

    @Override
    public boolean updateRemark(ActivityRemark ar) {
        boolean flag=true;
        int i=activityRemarkDao.updateRemark(ar);
        if(i!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean insertRemark(ActivityRemark ar) {
        boolean flag=true;
        int i=activityRemarkDao.insertRemark(ar);
        if (i!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public List<Activity> getList(String id) {
        List<Activity> aList = activity.getList(id);


        return aList;
    }

    @Override
    public List<Activity> showList(Map<String, String> map) {
          List<Activity> aList = activity.showList(map);

        return aList;
    }


}

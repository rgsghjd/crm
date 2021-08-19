package cg.software.workbench.web.controller;

import cg.software.settings.domain.User;
import cg.software.settings.service.UserService;
import cg.software.settings.service.impl.UserServiceImpl;
import cg.software.utils.*;
import cg.software.workbench.domain.Activity;
import cg.software.workbench.domain.ActivityRemark;
import cg.software.workbench.service.ActivityService;
import cg.software.workbench.service.impl.ActivityServiceImp;
import cg.software.workbench.vo.PageVo;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=request.getServletPath();
        System.out.println("进入市场活动模块");
        System.out.println(uri);
        if("/workbench/activity/getUserList.do".equals(uri)){
            getUserList(response);
        }else if("/workbench/activity/save.do".equals(uri)){
            save(request,response);
        }else if("/workbench/activity/activityList.do".equals(uri)){
            dataList(request,response);
        }else if("/workbench/activity/delete.do".equals(uri)){
            delete(request,response);
        }else if("/workbench/activity/edit.do".equals(uri)){
            edit(request,response);
        }else if("/workbench/activity/update.do".equals(uri)){
            update(request,response);
        }else if("/workbench/activity/detail.do".equals(uri)){
            detail(request,response);
        }else if("/workbench/activity/remarkList.do".equals(uri)){
            remarkList(request,response);
        }else if("/workbench/activity/deleteRemark.do".equals(uri)){
            deleteRemark(request,response);
        }else if("/workbench/activity/editRemark.do".equals(uri)){
            editRemark(request,response);
        }else if("/workbench/activity/updateRemark.do".equals(uri)){
            updateRemark(request,response);
        }else if("/workbench/activity/insertRemark.do".equals(uri)){
            insertRemark(request,response);
        }
    }

    private void insertRemark(HttpServletRequest request, HttpServletResponse response) {
        String noteContent=request.getParameter("noteContent");
        String activityId=request.getParameter("id");
       String  id =UUIDUtil.getUUID();
       String  createTime=DateTimeUtil.getsystime();
       String  createBy =((User)request.getSession().getAttribute("User")).getName();
       String  editFlag="0";
       ActivityRemark ar=new ActivityRemark();
       ar.setId(id);
       ar.setNoteContent(noteContent);
       ar.setEditFlag(editFlag);
       ar.setCreateTime(createTime);
       ar.setCreateBy(createBy);
       ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
       boolean flag=as.insertRemark(ar);
       Map<String,Object> map=new HashMap<>();
       map.put("success",flag);
       map.put("ar",ar);
       PrintJson.ObjectPrintJson(response,map);

    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入备注修改模块");
        String noteContent=request.getParameter("noteContent");
        String id=request.getParameter("id");

        String editTime= DateTimeUtil.getsystime();
        String editBy=((User)request.getSession().getAttribute("User")).getName();
        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setEditBy(editBy);
        ar.setEditTime(editTime);
        ar.setEditFlag("1");
        ar.setNoteContent(noteContent);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        boolean flag=as.updateRemark(ar);
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.ObjectPrintJson(response,map);
    }

    private void editRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入添加备注信息模块");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        ActivityRemark ar=as.editRemark(id);
        PrintJson.ObjectPrintJson(response,ar);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入删除市场活动备注!");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        boolean flag=as.deleteRemark(id);
        PrintJson.booleanPrintJson(response,flag);
    }

    private void remarkList(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        List<ActivityRemark> arList=as.remarkList(id);
        PrintJson.ObjectPrintJson(response,arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到信息跳转详细页");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        Activity ar=as.detail(id);
        request.setAttribute("a",ar);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {
        String id=request.getParameter("id");

       String  owner=request.getParameter("owner");
       String  name=request.getParameter("name");
       String  startDate=request.getParameter("startDate");
       String  endDate=request.getParameter("endDate");
       String  cost=request.getParameter("cost");
       String  description=request.getParameter("description");
       String  editTime=DateTimeUtil.getsystime();
       String  editBy=((User)request.getSession().getAttribute("User")).getName();

        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setEditTime(editTime);
        a.setEditBy(editBy);
        ActivityService activityService=(ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        boolean flage=activityService.update(a);


        PrintJson.booleanPrintJson(response,flage);

    }

    private void edit(HttpServletRequest request, HttpServletResponse response) {
        //获取要修改的记录id
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        Map<String,Object> map=as.edit(id);
        PrintJson.ObjectPrintJson(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String ids[]=request.getParameterValues("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        boolean flage=as.delete(ids);
        PrintJson.booleanPrintJson(response,flage);
    }

    private void dataList(HttpServletRequest request, HttpServletResponse response) {
        String name =request.getParameter("name");
        String owner=request.getParameter("owner");
        String startTime=request.getParameter("startTime");
        String endTime=request.getParameter("endTime");
        String PageNoStr=request.getParameter("PageNo");
        String PageSizeStr=request.getParameter("PageSize");
        int PageNo=Integer.parseInt(PageNoStr);
        int PageSize=Integer.parseInt(PageSizeStr);
        int skipCount=(PageNo-1)*PageSize;//跳过的记录数

        /**
         * 从前端拿到数据，进行处理，PageNoStr,要计算出跳过了的记录数
         */
        //将数据打包传给service层处理
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        map.put("skipCount",skipCount);
        map.put("PageSize",PageSize);
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        PageVo<Activity> vo=as.pageList(map);


        PrintJson.ObjectPrintJson(response,vo);

        /**
         * 为前端传送查询到的数据,因为需要一个List,还有记录条数，此时考虑用集合还是vo对象
         * 由于其他模块还需要查询相关的分页信息，所以创建一个vo对象
         */
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入市场活动模块");
        String id = UUIDUtil.getUUID();
        String owner=request.getParameter("owner");

        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime= DateTimeUtil.getsystime();
        String createBy=((User)request.getSession().getAttribute("User")).getName();
        //封装成User对象
        Activity a=new Activity();
        a.setId(id);
        a.setOwner(owner);
        a.setName(name);
        a.setStartDate(startDate);
        a.setEndDate(endDate);
        a.setCost(cost);
        a.setDescription(description);
        a.setCreateTime(createTime);
        a.setCreateBy(createBy);
        ActivityService activityService=(ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        int i=activityService.save(a);
        System.out.println("插入条数"+i);
        boolean flage=true;
        if (i!=1){
            flage=false;
        }
        PrintJson.booleanPrintJson(response,flage);



    }


    private void getUserList(HttpServletResponse response) {
        UserService userService=(UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> userList=userService.getUserlist();
        PrintJson.ObjectPrintJson(response,userList);
    }


}

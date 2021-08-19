package cg.software.workbench.web.controller;

import cg.software.settings.dao.UserDao;
import cg.software.settings.domain.DicValue;
import cg.software.settings.domain.User;
import cg.software.settings.service.UserService;
import cg.software.settings.service.impl.UserServiceImpl;
import cg.software.utils.DateTimeUtil;
import cg.software.utils.PrintJson;
import cg.software.utils.ServiceFactory;
import cg.software.utils.UUIDUtil;
import cg.software.workbench.dao.ClueDao;
import cg.software.workbench.domain.Activity;
import cg.software.workbench.domain.ActivityRemark;
import cg.software.workbench.domain.Clue;
import cg.software.workbench.domain.Tran;
import cg.software.workbench.service.ActivityService;
import cg.software.workbench.service.ClueService;
import cg.software.workbench.service.impl.ActivityServiceImp;
import cg.software.workbench.service.impl.ClueServiceImp;
import cg.software.workbench.vo.PageVo;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static jdk.nashorn.api.scripting.ScriptUtils.convert;

public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=request.getServletPath();
        System.out.println("进入线索模块");

        if("/workbench/clue/getUserList.do".equals(uri)){
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(uri)){
            save(request,response);
        }else if("/workbench/clue/detail.do".equals(uri)){
            detail(request,response);
        }else if("/workbench/clue/getList.do".equals(uri)){
            getList(request,response);
        }else if("/workbench/clue/unBun.do".equals(uri)){
            unBun(request,response);
        }else if("/workbench/clue/showList.do".equals(uri)){
            showList(request,response);
        }else if("/workbench/clue/bun.do".equals(uri)){
            bun(request,response);
        }else if("/workbench/clue/getActivity.do".equals(uri)){
            getActivity(request,response);
        }else if("/workbench/clue/convertClue.do".equals(uri)){
            convertClue(request,response);
        }
    }

    private void convertClue(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("进入线索转换模块...");
        String flag=request.getParameter("flag");
        String cid=request.getParameter("cid");
        Tran t=null;
        String createBy=((User)request.getSession().getAttribute("User")).getName();
        if ("true".equals(flag)){
            String id=UUIDUtil.getUUID();
            String stage=request.getParameter("stageValue");
            String createTime=DateTimeUtil.getsystime();
            String money=request.getParameter("money");
            String expectedDate=request.getParameter("expectedDate");
            String source=request.getParameter("source");
            String name=request.getParameter("name");
            String aid=request.getParameter("aid");

            t=new Tran();

            t.setStage(stage);
            t.setCreateBy(createBy);
            t.setCreateTime(createTime);
            t.setId(id);
            t.setActivityId(aid);
            t.setExpectedDate(expectedDate);
            t.setMoney(money);
            t.setSource(source);
            t.setName(name);

        }
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
       boolean flag1= cs.convert(cid,t,createBy);
       if (flag1){
           response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
       }
    }

    private void getActivity(HttpServletRequest request, HttpServletResponse response) {
        String aName=request.getParameter("aName");
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        List<Activity> aList=cs.getActivity(aName);
        PrintJson.ObjectPrintJson(response,aList);
    }

    private void bun(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入关联市场活动模块");
        String cId=request.getParameter("cid");
        String aId[]=request.getParameterValues("aid");

        Map<String,Object> map=new HashMap<>();
        map.put("cid",cId);
        map.put("aid",aId);

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        boolean flag=cs.bun(map);
        PrintJson.booleanPrintJson(response,flag);
    }

    private void showList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入到关联市场活动查询模块");
        String id=request.getParameter("id");
        String aName=request.getParameter("aName");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        Map<String,String> map=new HashMap<>();
        map.put("id",id);
        map.put("aName",aName);
        List<Activity> aList = as.showList(map);
        PrintJson.ObjectPrintJson(response,aList);
    }

    private void unBun(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索关系解除模块");
        String id=request.getParameter("id");
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        boolean flag=cs.unBun(id);
        PrintJson.booleanPrintJson(response,flag);
    }

    private void getList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("获取线索模块的市场活动列表");
        String id=request.getParameter("id");
        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImp());
        List<Activity> aList=as.getList(id);
        PrintJson.ObjectPrintJson(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        System.out.println("进入到获取详细信息页信息!");
        String id=request.getParameter("id");
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        Clue c=cs.detail(id);

        request.setAttribute("c",c);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {
        String id=UUIDUtil.getUUID();
        String fullname=request.getParameter("fullname");
        String appellation=request.getParameter("appellation");
        String owner=((User)request.getSession().getAttribute("User")).getId();
        String company=request.getParameter("company");
        String job=request.getParameter("job");
        String email=request.getParameter("email");
        String phone=request.getParameter("phone");
        String website=request.getParameter("website");
        String mphone=request.getParameter("mphone");
        String state=request.getParameter("state");
        String source=request.getParameter("source");
        String createBy=((User)request.getSession().getAttribute("User")).getName();
        String createTime=DateTimeUtil.getsystime();
        String description=request.getParameter("description");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        String address=request.getParameter("address");
        Clue c=new Clue();
        c.setAddress(address);
        c.setAppellation(appellation);
        c.setCompany(company);
        c.setContactSummary(contactSummary);
        c.setCreateBy(createBy);
        c.setDescription(description);
        c.setSource(source);
        c.setNextContactTime(nextContactTime);
        c.setCreateTime(createTime);
        c.setState(state);
        c.setMphone(mphone);
        c.setJob(job);
        c.setId(id);
        c.setFullname(fullname);
        c.setEmail(email);
        c.setPhone(phone);
        c.setOwner(owner);
        c.setWebsite(website);
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        boolean flag=cs.save(c);
        PrintJson.booleanPrintJson(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入线索模块的获取用户列表");
        UserService userService= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList=userService.getUserlist();
        PrintJson.ObjectPrintJson(response,uList);

    }


}

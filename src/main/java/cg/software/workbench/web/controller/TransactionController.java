package cg.software.workbench.web.controller;

import cg.software.settings.domain.User;
import cg.software.settings.service.UserService;
import cg.software.settings.service.impl.UserServiceImpl;
import cg.software.utils.DateTimeUtil;
import cg.software.utils.PrintJson;
import cg.software.utils.ServiceFactory;
import cg.software.utils.UUIDUtil;
import cg.software.workbench.domain.*;
import cg.software.workbench.service.ActivityService;
import cg.software.workbench.service.ClueService;
import cg.software.workbench.service.CustomerService;
import cg.software.workbench.service.TransactionService;
import cg.software.workbench.service.impl.ActivityServiceImp;
import cg.software.workbench.service.impl.ClueServiceImp;
import cg.software.workbench.service.impl.CustomerServiceImp;
import cg.software.workbench.service.impl.TransactionServiceImp;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionController extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri=request.getServletPath();
        System.out.println("进入交易模块");

        if("/workbench/transaction/save.do".equals(uri)){
            save(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(uri)){
            getCustomerName(request,response);
        }else if("/workbench/transaction/createTran.do".equals(uri)){
            createTran(request,response);
        }else if("/workbench/transaction/detail.do".equals(uri)){
            detail(request,response);
        }else if("/workbench/transaction/stageList.do".equals(uri)){
            stageList(request,response);
        }else if("/workbench/transaction/changeStage.do".equals(uri)){
            changeStage(request,response);
        }else if("/workbench/transaction/getCharts.do".equals(uri)){
            getCharts(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入交易统计图模块");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImp());
        Map<String,Object> map=ts.getcharts();
        PrintJson.ObjectPrintJson(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入阶段改变模块");
        String stage=request.getParameter("stage");
        String money=request.getParameter("money");
        String expectedDate=request.getParameter("expectedDate");
        String id=request.getParameter("id");
        String editBy=((User)request.getSession().getAttribute("User")).getName();
        String editTime=DateTimeUtil.getsystime();
        Tran t=new Tran();
        t.setId(id);
        t.setStage(stage);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        t.setEditBy(editBy);
        t.setEditTime(editTime);
        ServletContext appllication =request.getServletContext();
        Map<String,String> map2= (Map<String, String>) appllication.getAttribute("stagePossibility");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImp());
        t.setPossibility(map2.get(stage));
        boolean flag=ts.changeStage(t);

        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("t",t);
        PrintJson.ObjectPrintJson(response,map);
    }

    private void stageList(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入阶段历史模块");
        String id=request.getParameter("id");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImp());
        List<TranHistory> sList=ts.stageList(id);
        ServletContext appllication =request.getServletContext();
        Map<String,String> map= (Map<String, String>) appllication.getAttribute("stagePossibility");
        for (TranHistory s:sList){
            String stage=s.getStage();
            String possibility=map.get(stage);
            s.setPossibility(possibility);
        }
        PrintJson.ObjectPrintJson(response,sList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入到交易详细信息页");
        String id=request.getParameter("id");
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImp());
        Tran t=ts.find(id);
        String stage=t.getStage();
        ServletContext appllication =request.getServletContext();
        Map<String,String> map= (Map<String, String>) appllication.getAttribute("stagePossibility");
        String possibility=map.get(stage);
        t.setPossibility(possibility);
        request.setAttribute("t",t);
        request.getRequestDispatcher("detail.jsp").forward(request,response);
    }

    private void createTran(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("创建交易模块");
        String id=UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String money=request.getParameter("money");
        String name=request.getParameter("name");
        String expectedDate=request.getParameter("expectedClosingDate");
        String customerName=request.getParameter("customerName");
        String stage=request.getParameter("stage");
        String type=request.getParameter("transactionType");
        String source=request.getParameter("clueSource");
        String activityId=request.getParameter("asource");
        String contactsId=request.getParameter("contactsName");//这里是联系人的id
        String createBy=((User)request.getSession().getAttribute("User")).getName();
        String createTime=DateTimeUtil.getsystime();
        String description=request.getParameter("describe");
        String contactSummary=request.getParameter("contactSummary");
        String nextContactTime=request.getParameter("nextContactTime");
        Tran t=new Tran();
        t.setOwner(owner);
        t.setNextContactTime(nextContactTime);
        t.setContactSummary(contactSummary);
        t.setDescription(description);
        t.setContactsId(contactsId);
        t.setActivityId(activityId);
        t.setCreateTime(createTime);
        t.setStage(stage);
        t.setCreateBy(createBy);
        t.setId(id);
        t.setName(name);
        t.setSource(source);
        t.setType(type);
        t.setMoney(money);
        t.setExpectedDate(expectedDate);
        TransactionService ts= (TransactionService) ServiceFactory.getService(new TransactionServiceImp());
        boolean flag=ts.saveTran(t,customerName);
        if (flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }else {
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/save.jsp");
        }


    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("进入查询客户名称模块");
        String name=request.getParameter("name");
        CustomerService cs= (CustomerService) ServiceFactory.getService(new CustomerServiceImp());
        List<String> cList=cs.find(name);
        PrintJson.ObjectPrintJson(response,cList);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserlist();
        request.setAttribute("uList",uList);
        request.getRequestDispatcher("save.jsp").forward(request,response);

    }

}

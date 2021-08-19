package cg.software.web.Listener;

import cg.software.settings.domain.DicValue;
import cg.software.utils.ServiceFactory;
import cg.software.workbench.domain.Clue;
import cg.software.workbench.service.ClueService;
import cg.software.workbench.service.impl.ClueServiceImp;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("数据字典创建开始...");
        /**
         * 需要对数据进行分类存储在application中
         */
        ServletContext application=sce.getServletContext();
        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImp());
        Map<String,List<DicValue>> map=cs.getDicList();

        Set keys=map.keySet();
        for (Object key:keys){
            application.setAttribute(key+"List",map.get(key));
        }

        //绑定可能性和阶段的对应关系
        ResourceBundle rb=ResourceBundle.getBundle("Stage2Possibility");
        Map<String ,String> map2=new HashMap<>();
        Enumeration<String> stages= rb.getKeys();
        while (stages.hasMoreElements()){
           String stage = stages.nextElement();
          String value = rb.getString(stage);

          map2.put(stage,value);
        }
        application.setAttribute("stagePossibility",map2);

    }
}

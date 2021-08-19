package cg.software.workbench.service.impl;

import cg.software.utils.DateTimeUtil;
import cg.software.utils.SqlSessionUtil;
import cg.software.utils.UUIDUtil;
import cg.software.workbench.dao.CustomerDao;
import cg.software.workbench.dao.TranDao;
import cg.software.workbench.dao.TranHistoryDao;
import cg.software.workbench.domain.Customer;
import cg.software.workbench.domain.Tran;
import cg.software.workbench.domain.TranHistory;
import cg.software.workbench.service.TransactionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionServiceImp implements TransactionService {
    TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    TranHistoryDao tranHistoryDao= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    @Override
    public boolean saveTran(Tran t, String customerName) {
        boolean flag=true;
        //根据客户名查找是否存在该客户
        Customer c=customerDao.find(customerName);
        if(c==null){
            c=new Customer();
            c.setOwner(t.getOwner());
            c.setNextContactTime(t.getNextContactTime());
            c.setName(customerName);
            c.setId(UUIDUtil.getUUID());
            c.setDescription(t.getDescription());
            c.setCreateTime(t.getCreateTime());
            c.setCreateBy(t.getCreateBy());
            c.setContactSummary(t.getContactSummary());
            int i3=customerDao.save(c);
            if (i3!=1){
                flag=false;
            }
        }
        t.setCustomerId(c.getId());
        int i=tranDao.save(t);
        if (i!=1){
            flag=false;
        }
        TranHistory th=new TranHistory();
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setId(UUIDUtil.getUUID());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(t.getCreateTime());
        th.setCreateBy(t.getCreateBy());
        int i2 = tranHistoryDao.save(th);
        if (i2!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Tran find(String id) {
        Tran t=tranDao.find(id);
        return t;
    }

    @Override
    public List<TranHistory> stageList(String id) {
        List<TranHistory> sList=tranHistoryDao.find(id);
        return sList;
    }

    @Override
    public boolean changeStage(Tran t) {
        boolean flag=true;
        int i=tranDao.changstage(t);
        if (i!=1){
            flag=false;
        }
        TranHistory tranHistory=new TranHistory();
        tranHistory.setCreateBy(t.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getsystime());
        tranHistory.setExpectedDate(t.getExpectedDate());
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setMoney(t.getMoney());
        tranHistory.setStage(t.getStage());
        tranHistory.setTranId(t.getId());
        int i2=tranHistoryDao.save(tranHistory);
        if (i2!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Map<String, Object> getcharts() {
        int i=tranDao.getTotal();

        List<Map<String,String>> sList=tranDao.getcharts();
        Map<String,Object> map=new HashMap<>();
        map.put("total",i);
        map.put("sList",sList);
        System.out.println(sList);
        return map;
    }
}

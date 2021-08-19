package cg.software.workbench.service.impl;

import cg.software.settings.dao.DicTypeDao;
import cg.software.settings.dao.DicValueDao;
import cg.software.settings.domain.DicType;
import cg.software.settings.domain.DicValue;
import cg.software.utils.DateTimeUtil;
import cg.software.utils.SqlSessionUtil;
import cg.software.utils.UUIDUtil;
import cg.software.workbench.dao.*;
import cg.software.workbench.domain.*;
import cg.software.workbench.service.ClueService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImp implements ClueService {
    //线索相关表
    ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    ClueActivityRelationDao carDao= SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);
    //联系人相关表
    ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);
    //客户相关表
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    CustomerRemarkDao customerRemarkDao= SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);
    //交易相关表
    TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    TranHistoryDao tranHistoryDao= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public Map<String, List<DicValue>> getDicList() {
        DicTypeDao dicTypeDao=SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
        DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);
        Map<String,List<DicValue>> map= new HashMap();
        List<DicType> typeList=dicTypeDao.getTypeList();
        for (DicType dicType:typeList){
            List<DicValue> dvlue=dicValueDao.getValue(dicType.getCode());
            map.put(dicType.getCode(),dvlue);
        }



        return map;
    }

    @Override
    public boolean save(Clue c) {
        boolean flag=true;
        int i= clueDao.save(c);
        if (i!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public Clue detail(String id) {
        Clue c=clueDao.detail(id);

        return c;
    }

    @Override
    public boolean unBun(String id) {
        boolean flag=true;
        int i=carDao.unBun(id);
        if (i!=1){
            flag=false;
        }
        return flag;
    }

    @Override
    public boolean bun(Map<String,Object> map) {
        boolean flag = true;
        String aids[]=(String[]) map.get("aid");
        String cid=(String) map.get("cid");
        Map<String,String> map1=new HashMap<>();
        for (String aid:aids){
            String id= UUIDUtil.getUUID();
            map1.put("aid",aid);
            map1.put("cid",cid);
            map1.put("id",id);
            int i=clueDao.bun(map1);
            if (i!=1)
                flag=false;
        }


        return flag;
    }

    @Override
    public List<Activity> getActivity(String aName) {
        List<Activity> aList = clueDao.getActivity(aName);
        return aList;
    }

    @Override
    public boolean convert(String cid, Tran t, String createBy) {
        boolean flag=true;
        //(1) 获取到线索id，通过线索id获取线索对象（线索对象当中封装了线索的信息）
        Clue c=clueDao.find(cid);
        if (c == null){
            flag=false;
        }

        //(2) 通过线索对象提取客户信息，当该客户不存在的时候，新建客户（根据公司的名称精确匹配，判断该客户是否存在！）
        String company=c.getCompany();
        Customer customer=customerDao.find(company);
        if (customer==null){
            Customer customer1=new Customer();
            customer1.setAddress(c.getAddress());
            customer1.setContactSummary(c.getContactSummary());
            customer1.setCreateBy(createBy);
            customer1.setCreateTime(DateTimeUtil.getsystime());
            customer1.setDescription(c.getDescription());
            customer1.setId(UUIDUtil.getUUID());
            customer1.setName(company);
            customer1.setNextContactTime(c.getNextContactTime());
            customer1.setOwner(c.getOwner());
            customer1.setPhone(c.getPhone());
            customer1.setWebsite(c.getWebsite());

            int count1=customerDao.save(customer1);
            if (count1!=1){
                flag=false;
            }
        }
        Customer customer1=customerDao.find(company);
        //(3) 通过线索对象提取联系人信息，保存联系人
        Contacts contacts=new Contacts();
        contacts.setAddress(c.getAddress());
        contacts.setAppellation(c.getAppellation());
        contacts.setContactSummary(c.getContactSummary());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(DateTimeUtil.getsystime());
        contacts.setCustomerId(customer1.getId());
        contacts.setEmail(c.getEmail());
        contacts.setFullname(c.getFullname());
        contacts.setJob(c.getJob());
        contacts.setSource(c.getSource());
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(c.getOwner());
        contacts.setMphone(c.getMphone());
        contacts.setNextContactTime(c.getNextContactTime());
        int count2=contactsDao.save(contacts);
        if (count2!=1){
            flag=false;
        }
        //(4) 线索备注转换到客户备注以及联系人备注
        List<ClueRemark> clueRemarks= clueRemarkDao.find(c.getId());
        for (ClueRemark clueRemark1:clueRemarks) {
            String noteContent = clueRemark1.getNoteContent();
            CustomerRemark customerRemark = new CustomerRemark();
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(DateTimeUtil.getsystime());
            customerRemark.setCustomerId(customer1.getId());
            customerRemark.setEditFlag("0");
            customerRemark.setNoteContent(noteContent);
            customerRemark.setId(UUIDUtil.getUUID());
            int count3 = customerRemarkDao.save(customerRemark);
            if (count3 != 1) {
                flag = false;
            }

            ContactsRemark contactsRemark = new ContactsRemark();
            contactsRemark.setContactsId(contacts.getId());
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(DateTimeUtil.getsystime());
            contactsRemark.setEditFlag("0");
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setId(UUIDUtil.getUUID());
            int count4 = contactsRemarkDao.save(contactsRemark);
            if (count4 != 1) {
                flag = false;
            }
        }
        //(5) “线索和市场活动”的关系转换到“联系人和市场活动”的关系
        List<ClueActivityRelation> clueActivityRelation=carDao.find(c.getId());
        for(ClueActivityRelation car:clueActivityRelation){
            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setActivityId(car.getActivityId());
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setContactsId(contacts.getId());
            int count5=contactsActivityRelationDao.save(contactsActivityRelation);
            if (count5!=1){
                flag=false;
            }
        }
        //(6) 如果有创建交易需求，创建一条交易
        if(t!=null){
            t.setOwner(c.getOwner());
            t.setDescription(c.getDescription());
            t.setContactSummary(c.getContactSummary());
            t.setNextContactTime(c.getNextContactTime());
            t.setCustomerId(customer1.getId());
            t.setContactsId(contacts.getId());
            int count6=tranDao.save(t);
            if (count6!=1){
                flag=false;
            }
            //(7) 如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory=new TranHistory();
            tranHistory.setCreateBy(createBy);
            tranHistory.setCreateTime(DateTimeUtil.getsystime());
            tranHistory.setExpectedDate(t.getExpectedDate());
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setMoney(t.getMoney());
            tranHistory.setStage(t.getStage());
            tranHistory.setTranId(t.getId());
            int count7=tranHistoryDao.save(tranHistory);
            if(count7!=1){
                flag=false;
            }
        }


        //(8) 删除线索备注
        int count8=clueRemarkDao.delete(c.getId());
        if(count8==0){
            flag=false;
        }
        //(9) 删除线索和市场活动的关系
        int count9=carDao.delete(c.getId());
        if(count9==0){
            flag=false;
        }
        //(10) 删除线索
        int count10=clueDao.delete(c.getId());
        if(count10==0){
            flag=false;
        }

        return flag;
    }
}

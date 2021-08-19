package cg.software.workbench.service.impl;

import cg.software.utils.SqlSessionUtil;
import cg.software.workbench.dao.CustomerDao;
import cg.software.workbench.domain.Customer;
import cg.software.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImp implements CustomerService {
    CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> find(String name) {
       List<String> cList = customerDao.find2(name);

        return cList;
    }
}

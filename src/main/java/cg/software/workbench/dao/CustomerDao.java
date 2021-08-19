package cg.software.workbench.dao;

import cg.software.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer find(String company);

    int save(Customer customer1);

    List<String> find2(String name);
}

package cg.software.workbench.dao;

import cg.software.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory tranHistory);

    List<TranHistory> find(String id);
}

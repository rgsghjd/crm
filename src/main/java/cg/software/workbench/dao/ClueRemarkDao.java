package cg.software.workbench.dao;

import cg.software.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> find(String id);

    int delete(String id);
}

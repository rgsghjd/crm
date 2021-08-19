package cg.software.workbench.dao;

import cg.software.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran find(String id);

    int changstage(Tran t);

    int getTotal();

    List<Map<String, String>> getcharts();
}

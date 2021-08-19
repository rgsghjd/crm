package cg.software.workbench.dao;


import cg.software.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unBun(String id);

    List<ClueActivityRelation> find(String id);

    int delete(String id);
}

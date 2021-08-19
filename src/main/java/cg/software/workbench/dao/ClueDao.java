package cg.software.workbench.dao;


import cg.software.workbench.domain.Activity;
import cg.software.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {


    int save(Clue c);

    Clue detail(String id);

    int unBun(String id);

    int bun(Map<String,String> map);

    List<Activity> getActivity(String aName);

    Clue find(String cid);

    int delete(String id);
}

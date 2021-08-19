package cg.software.workbench.service;

import cg.software.settings.domain.DicValue;
import cg.software.workbench.domain.Activity;
import cg.software.workbench.domain.Clue;
import cg.software.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface ClueService {
    Map<String, List<DicValue>> getDicList();

    boolean save(Clue c);

    Clue detail(String id);

    boolean unBun(String id);

    boolean bun(Map<String,Object> map);

    List<Activity> getActivity(String aName);

    boolean convert(String cid, Tran t, String createBy);
}

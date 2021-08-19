package cg.software.workbench.service;

import cg.software.workbench.domain.Tran;
import cg.software.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    boolean saveTran(Tran t, String customerName);

    Tran find(String id);

    List<TranHistory> stageList(String id);

    boolean changeStage(Tran t);

    Map<String, Object> getcharts();
}

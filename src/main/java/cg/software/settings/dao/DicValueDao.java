package cg.software.settings.dao;

import cg.software.settings.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    List<DicValue> getValue(String code);
}

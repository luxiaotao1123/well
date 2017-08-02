package org.wella.dao;

import org.springframework.stereotype.Repository;
import org.wella.entity.Repay;

import java.util.List;
import java.util.Map;


/**
 * Created by liuwen on 2017/6/8.
 */
@Repository
public interface RepayDao {

    void createRepay(Repay repay);

    List<Map<String,Object>> listRepayByConditions(Map<String, Object> query);
}

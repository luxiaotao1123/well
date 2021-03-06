package org.wella.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.wella.entity.Bankcard;
import org.wella.entity.UserMoney;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwen on 2017/6/8.
 */
@Repository
public interface UserMoneyDao {

    void create(UserMoney userMoney);

    void update(Map map);

    Map<String,Object> singlePoByPrimaryKey(@Param("moneyId") long moneyId);

    List<Map<Integer,Object>> turnover(Map map);

    List<Map<Integer,Object>> fundFlowOut(Map map);


    List<Map<Integer,Object>> fundFlowIn(Map map);

    /**
     * 用户交易记录
     * @param param 分页参数
     * @return 用户交易记录
     */
    List<Map<String,Object>> getJyList(Map param);

    /**
     * 用户交易记录总记录数
     * @param param 分页参数
     * @return 用户交易记录总记录数
     */
    int getJyListCount(Map param);
}

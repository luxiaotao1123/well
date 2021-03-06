package org.wella.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by liuwen on 2017/6/8.
 */
@Repository
public interface CreditDao {

    /**
     * 申请授信额度
     * @param params 表单提交参数
     * @return 数据库表单提交记录数
     */
    int applyCreditLimit(Map<String, Object> params);

    List<Map<String,Object>> listCreditAttachUserinfoByConditions(Map<String, Object> params);

    int listCreditAttachUserinfoByConditionsCount(Map<String, Object> params);

    Map<String,Object> singleCreditByConditions(Map<String, Object> param);

    Map<String,Object> singleCreditInfoViewByPrimaryKey(long creditId);

    void updateByPrimaryKey(Map<String, Object> params);

    Map<String,Object> singleCreditByPrimaryKey(@Param("creditId") long creditId);

    List<Map<String,Object>> listCreditByConditions(Map params);

    int listCreditByConditionsCount(Map params);
}

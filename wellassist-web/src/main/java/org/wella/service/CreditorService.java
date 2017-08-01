package org.wella.service;

import org.wella.entity.CreditorAuthenticInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */
public interface CreditorService {
    /**
     * 先忽略放款方资质审核测试，以后改这个接口
     * @return
     */
    List<Map<String,Object>> findCreditorList();

    /**
     *放款方接受放款指派
     * test:暂定还款期为30天，免息期为7天，放款方利率=平台利率*0.9
     * @param paymentDays 还款期限
     * @return 0 失败 1成功
     */
    int acceptLoan(long loanId,long creditorUserId,int paymentDays,String operateIp);

    /**
     * 放款方资质状态
     * @param userId
     * @return 0:未通过，1：审核中，2：已通过
     */
    int findCreditorState(long userId);

    /**
     * 放款方提交资质审核
     * @param creditorAuthenticInfo
     */
    void qualityApply(CreditorAuthenticInfo creditorAuthenticInfo);

}

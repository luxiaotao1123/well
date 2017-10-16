package org.wella.service;

import com.wellapay.cncb.model.output.AccountTransQuery.AccountTransQueryOutputListEntity;
import org.wella.entity.UserSubAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;

/**
 * Created by liuwen on 2017/6/5.
 * 财务业务处理类
 */
public interface FinanceService {

    /**
     * 充值相关业务处理
     * 两种业务处理，线下支付与银行支付
     * @param map 操作参数
     * @return map返回充值处理结果，包含两个key值（state，content）
     */
    int recharge(Map<String,Object> map);


    /**
     * 提现相关业务处理
     * @param map
     * @return
     */
    int withdraw(Map<String,Object> map);

    /**
     * 关联wa_order表 单条贷款详细信息
     * @param loanId wa_loan 表主键
     * @return 关联wa_order表 单条贷款详细信息
     */
    Map<String,Object> getLoanOrderInfo(long loanId);

    /**
     * 贷款还款明细
     * @param loanId loanId
     * @return  贷款还款明细
     */
    Map<String,Object> getLoanRepayInfo(long loanId);

    /**
     * 查询用户余额
     * @param userId userId
     * @return 查询用户余额
     * @throws Exception
     */
    BigDecimal getBalance(long userId) throws Exception;

    /**
     * 得到用户的附属账户信息
     * @param userId wa_user表主键
     * @return wa_user_sub_account表记录
     */
    UserSubAccount getUserSubAccountByUserId(long userId);

    /**
     * 附属账户某日的交易流水
     * @param subAccNo 附属账户
     * @param date 日期
     * @return list
     */
    List<AccountTransQueryOutputListEntity> getSubAccountFlowByDate(String subAccNo,Date date);

    /**
     * 订单交易中转户某日的交易流水
     * @param date 日期
     * @return list
     */
    List<AccountTransQueryOutputListEntity> getOrderTransferAccountFlowByDate(Date date);

    /**
     * 还款中转户某日的交易流水
     * @param date 日期
     * @return list
     */
    List<AccountTransQueryOutputListEntity> getLoanTransferAccountFlowByDate(Date date);

    /**
     * 待贷款还清后从还款中转户还给放款账户
     * @param loanId wa_loan 主键
     */
    void handleLoanRepayoff(long loanId);
}

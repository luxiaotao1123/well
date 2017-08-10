package org.wella.service.impl;

import io.wellassist.utils.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wella.common.utils.ConvertUtil;
import org.wella.dao.*;
import org.wella.entity.CreditorAuthenticInfo;
import org.wella.service.CreditorService;
import org.wella.utils.DateUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/7/21.
 */

@Service("creditorServiceImpl")
public class CreditorServiceImpl implements CreditorService{

    @Autowired
    private WaUserDao waUserDao;

    @Autowired
    private LoanDao loanDao;

    @Autowired
    private LoanAssignInfoDao loanAssignInfoDao;

    @Autowired
    private CreditorAuthenticInfoDao creditorAuthenticInfoDao;

    @Autowired
    private RepayDao repayDao;




    /**
     * 得到已认证的放款方list
     * @return
     */
    @Override
    public List<Map<String, Object>> findCreditorList() {
        Map<String,Object> param=new HashMap();
        param.put("userType",2);
        param.put("userState",1);
        param.put("creditorState",2);
        List<Map<String,Object>> creditors=waUserDao.listUserAttachUserinfoByConditions(param);
        ConvertUtil.convertDataBaseMapToJavaMap(creditors);
        return creditors;
    }

    @Override
    @Transactional
    public int acceptLoan(long loanId, long creditorUserId, int paymentDays,String operateIp) {
        Date now=new Date();

        Map<String,Object> loan=loanDao.singleLoanByPrimaryKey(loanId);
        if(creditorUserId!=(long)loan.get("credit_user_id")){
            return 0;
        }

        // update table wa_loan
        Map<String,Object> updateloan=new HashMap();
        updateloan.put("loanId",loanId);
        updateloan.put("loanDate",now);
        updateloan.put("interestFreeDate", DateUtils.addDays(now,7));
        updateloan.put("paymentDate",DateUtils.addDays(now,7+paymentDays));
        BigDecimal lixiRate=(BigDecimal) loan.get("lixi_rate");
        BigDecimal lixiRateFkf=lixiRate.multiply(new BigDecimal(0.9));
        updateloan.put("lixiRateFkf",lixiRateFkf);
        updateloan.put("loanState",2);
        updateloan.put("remainRepayMoney",loan.get("loan_money"));
        loanDao.updateLoanByPrimaryKey(updateloan);

        //update table wa_loan_assign_info
        Map<String,Object> loanAssignInfo=getLoanAssignInfo(loanId, creditorUserId);

        Map<String,Object> updateloanAssignInfo=new HashMap<>();
        updateloanAssignInfo.put("loanAssignInfoId",loanAssignInfo.get("loanAssignInfoId"));
        updateloanAssignInfo.put("state",1);
        updateloanAssignInfo.put("operateDate",now);
        updateloanAssignInfo.put("operateIp",operateIp);
        loanAssignInfoDao.updateByPrimaryKey(updateloanAssignInfo);

        return 1;
    }

    @Override
    public int findCreditorState(long userId) {
        Map<String,Object> user=waUserDao.singleUserByPrimaryKey(userId);
        int creditorState=(int)user.get("creditor_state");
        return creditorState;
    }

    @Override
    @Transactional
    public void qualityApply(CreditorAuthenticInfo creditorAuthenticInfo) {
        creditorAuthenticInfoDao.save(creditorAuthenticInfo);
        Map<String,Object> updateuser=new HashMap<>();
        updateuser.put("userId",creditorAuthenticInfo.getUserId());
        updateuser.put("creditorState",1);
        waUserDao.updateUserByUserId(updateuser);
    }

    @Override
    public CreditorAuthenticInfo getAuthenticingInfo(long userId) {
        Map<String,Object> query=new HashMap<>();
        query.put("userId",userId);
        query.put("state",1);
        CreditorAuthenticInfo creditorAuthenticInfo=creditorAuthenticInfoDao.singlePoByConditons(query);
        return creditorAuthenticInfo;
    }

    @Override
    public List<Map<String,Object>> getAssignLoans(Map query) {
        query.put("loanState",1);
        List<Map<String, Object>> res=loanDao.listLoanOrderViewByConditions(query);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        return res;
    }

    @Override
    public int getAssignLoansCount(Map query) {
        query.put("loanState",1);
        int result=loanDao.listLoanByConditionsCount(query);
        return result;
    }

    @Override
    public Map<String, Object> getLoanAssignInfo(long loanId, long creditorUserId) {
        Map<String,Object> query=new HashMap<>();
        query.put("loanId",loanId);
        query.put("creditorId",creditorUserId);
        query.put("state",0);
        Map<String,Object> loanAssignInfo=loanAssignInfoDao.singleLoanAssignInfoByConditions(query);
        ConvertUtil.convertDataBaseMapToJavaMap(loanAssignInfo);
        return loanAssignInfo;
    }

    @Override
    @Transactional
    public void refuseLoan(long loanId, long creditorUserId, String ip) {
        Map<String,Object> updateloan=new HashMap<>();
        updateloan.put("loanId",loanId);
        updateloan.put("creditUserId",0);
        updateloan.put("loanState",0);
        loanDao.updateLoanByPrimaryKey(updateloan);

        Map<String,Object> loanAssignInfo=getLoanAssignInfo(loanId,creditorUserId);
        Map<String,Object> updateLoanAssignInfo=new HashMap<>();
        updateLoanAssignInfo.put("loanAssignInfoId",loanAssignInfo.get("loanAssignInfoId"));
        updateLoanAssignInfo.put("state",-1);
        updateLoanAssignInfo.put("operateDate",new Date());
        updateLoanAssignInfo.put("operateIp",ip);
        loanAssignInfoDao.updateByPrimaryKey(updateLoanAssignInfo);
    }

    @Override
    public List<Map<String, Object>> listLoanOrderViewByConditions(Map params) {
        List<Map<String, Object>> loans=loanDao.listLoanOrderViewByConditions(params);
        ConvertUtil.convertDataBaseMapToJavaMap(loans);
        return loans;
    }

    @Override
    public int listLoanCount(Map params) {
        int res=loanDao.listLoanByConditionsCount(params);
        return res;
    }

    @Override
    public Map<String, Object> getLatestRepay(long loanId) {
        Map<String,Object> query=new HashMap<>();
        query.put("loanId",loanId);
        query.put("orderBy","repay_date desc");
        Map<String,Object> res=repayDao.singleRepayByConditions(query);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        return res;
    }



    @Override
    public List<Map<String,Object>> repayOffList(Query query) {
        List<Map<String, Object>> loans=loanDao.listLoanOrderViewByConditions(query);
        ConvertUtil.convertDataBaseMapToJavaMap(loans);
        for (Map map:loans){
            Map<String,Object> repay=getLatestRepay((long)map.get("loanId"));
            map.put("latestRepayDate",repay.get("repayDate"));
        }
        return loans;
    }






}
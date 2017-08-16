package org.wella.service.impl;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wella.common.utils.ConvertUtil;
import org.wella.dao.*;
import org.wella.entity.*;
import org.wella.service.CustomerService;
import org.wella.service.RegionService;
import org.wella.service.WaOrderService;
import org.wella.utils.CommonUtil;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liuwen on 2017/5/10.
 */
@Service("customerServiceImpl")
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private OrderDao orderDao;
    @Autowired
    private UserinfoDao userinfoDao;
    @Autowired
    private ProdDao prodDao;
    @Autowired
    private OrderInfoDao orderInfoDao;
    @Autowired
    private RegionDao regionDao;
    @Autowired
    private VehicleInfoDao vehicleInfoDao;
    @Autowired
    private OrderLogisticsInfoDao orderLogisticsInfoDao;
    @Autowired
    private ProdUserDao prodUserDao;
    @Autowired
    private WaOrderService waOrderServiceImpl;
    @Autowired
    private ZorderDao zorderDao;
    @Autowired
    private LogisticsInfoDao logisticsInfoDao;
    @Autowired
    private VehicleGrabDao vehicleGrabDao;
    @Autowired
    private WaUserDao waUserDao;
    @Autowired
    private OrderHistoryTailDao orderHistoryTailDao;
    @Autowired
    private CreditDao creditDao;
    @Autowired
    private LoanDao loanDao;
    @Autowired
    private RepayDao repayDao;
    @Autowired
    private RegionService regionServiceImpl;
    @Autowired
    private MessageServiceImpl messageServicesk;




    /**
     * 需要进行事务控制
     *
     * @param map 表单中提交的内容(String):prodId，saleNum，danjia，saleMoney，isSelfCar，contacts，conTel，toRegionId
     *            toRegionAddr，orderData，deliverDate，receiveDate，customerExceptCarriage
     */
    @Override
    @Transactional
    public long order(Map map) {
        //获取与用户和产品相关联的信息写入订单表中
        long userId = (long) map.get("userId");
        Userinfo userinfo = userinfoDao.getOrderUserinfoByUserid(userId);
        long prodId = Long.parseLong((String) map.get("prodId"));
        Map<String, Object> prod = prodDao.singleProdByPrimaryKey(prodId);
        ConvertUtil.convertDataBaseMapToJavaMap(prod);

        Order order = new Order();
        order.setOrderNo(CommonUtil.genKey(4));
        order.setProdId(prodId);
        order.setProdName((String) prod.get("prodName"));
        order.setFromRegionId((Long) prod.get("prodRegionId"));
        order.setFromRegionAddr((String) prod.get("prodRegionAddr"));
        order.setSupplierId((Long) prod.get("userId"));

        order.setToRegionId(Long.parseLong((String) map.get("toRegionId")));
        order.setToRegionAddr((String) map.get("toRegionAddr"));

        order.setUserId(userId);
        order.setCompanyLxr(userinfo.getCompanyLxr());
        order.setCompanyLxrPhone(userinfo.getCompanyLxrPhone());

        order.setSaleNum(new BigDecimal((String) map.get("saleNum")));
        order.setSaleMoney(new BigDecimal((String) map.get("saleMoney")));
        order.setOrderDate(new Date());
        order.setDjModifyDate(new Date());
        order.setOrderIp((String) map.get("orderIp"));
        order.setIsSelfCar(Byte.parseByte((String) map.get("isSelfCar")));
        order.setCustomerExceptCarriage(new BigDecimal((String)map.get("customerExceptCarriage")));

        order.setOrderType((byte) 0);
        order.setOrderState((byte) 0);

        orderDao.createOrder(order);
        //生成一条wa_order_info记录
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderId(order.getOrderId());
        orderInfo.setInfoType(order.getOrderType());
        orderInfo.setMgrIp(order.getOrderIp());
        orderInfo.setMgrDate(order.getOrderDate());
        orderInfoDao.createOrderInfo(orderInfo);

        OrderLogisticsInfo orderLogisticsInfo = new OrderLogisticsInfo();
        Date ccDate = str2Date((String) map.get("deliverDate"), "yyyy-MM-dd HH:mm:ss");
        Date ddDate = str2Date((String) map.get("receiveDate"), "yyyy-MM-dd HH:mm:ss");

        orderLogisticsInfo.setOrderId(order.getOrderId());
        orderLogisticsInfo.setDeliverDate(ccDate);
        orderLogisticsInfo.setReceiveDate(ddDate);
        orderLogisticsInfo.setContacts((String) map.get("contacts"));
        orderLogisticsInfo.setConTel((String) map.get("conTel"));
        HashMap paramMap = new HashMap();
        paramMap.put("regionId", (String) map.get("toRegionId"));
        String addr = regionDao.getRegionDetailName(paramMap) + " " + (String) map.get("toRegionAddr");
        orderLogisticsInfo.setAddress(addr);
        orderLogisticsInfoDao.createOrderLogisticsInfoDao(orderLogisticsInfo);

        if (order.getIsSelfCar() == 0) {
            //自提则将司机信息写入wa_vehicle_info表中，将出发时间和到达时间写入wa_order_logistics_info表中
            ArrayList<Map<String, Object>> vehicleList = ConvertUtil.converJSONtoArrayListMap((String) map.get("orderData"));
            for (Map vehivleMap : vehicleList) {
                VehicleInfo vInfo = new VehicleInfo();
                vInfo.setOrderId(order.getOrderId());
                vInfo.setDriverName((String) vehivleMap.get("driverName"));
                vInfo.setDriverPhone((String) vehivleMap.get("driverPhone"));
                vInfo.setVehicleNo((String) vehivleMap.get("carCode"));
                vInfo.setVehicleHangingNo((String) vehivleMap.get("vehicleHangingNo"));
                vInfo.setVehicleSize(Double.parseDouble(vehivleMap.get("vehicleSize").toString()));
                vehicleInfoDao.createVehicleInfo(vInfo);
            }
        }
        //生成订单追踪信息
        /*OrderHistoryTail orderHistoryTail=new OrderHistoryTail();
        orderHistoryTail.setOrderId(order.getOrderId());
        orderHistoryTail.setOrderNo(order.getOrderNo());
        orderHistoryTail.setHistoryDate(order.getOrderDate());
        orderHistoryTail.setContent("买家下单。");
        orderHistoryTail.setTailDate(new Date());
        orderHistoryTailDao.createOrderHistoryTail(orderHistoryTail);*/

        return order.getOrderId();
    }


    /**
     * 字符串转Date
     *
     * @param dateStr
     * @param format
     * @return
     */
    public Date str2Date(String dateStr, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public void orderPay(int orderId) {

    }

    @Override
    public void cancelPay(int orderId) {

    }

    @Override
    public void processDeliveryOrder(int deliveryOrderId) {

    }

    @Override
    public void deliveryOrderPay(int deliveryOrderId) {

    }

    @Override
    public void confirmDelivery(int deliveryOrderId) {

    }

    @Override
    public void evaluate(int orderId) {

    }

    @Override
    public void loanApply(int customerId) {

    }

    @Override
    public void loanConfirm() {

    }

    @Override
    public List<Order> findOrderList() {
        return null;
    }

    @Override
    public List<Order> findOrderList(Map map) {
        return null;
    }

    @Override
    public void createOrder() {

    }

    @Override
    public void updateOrder(Order order) {

    }

    @Override
    public void deleteOrder(int orderId) {

    }

    @Override
    public List<Prod> findProdList(Map map) {
        return prodUserDao.getUserProdList(map);
    }


    /**
     * 获取产品信息，并将其中的prod_region_id(编码)转换成fromRegionName（字符串）
     *
     * @param param
     * @return
     */
    @Override
    public Map<String, Object> findProdById(Map param) {
        Map<String, Object> prodMap = prodDao.findProdById(param);
        ConvertUtil.convertDataBaseMapToJavaMap(prodMap);
        HashMap<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("regionId", (long) prodMap.get("prodRegionId"));
        String fromRegionName = regionDao.getRegionDetailName(paramMap);
        prodMap.put("fromRegionName", fromRegionName);
        return prodMap;
    }

    @Override
    public List<Map<String, Object>> getRegionList(Map param) {
        List resList = regionDao.getRegionList(param);
        ConvertUtil.convertDataBaseMapToJavaMap(resList);
        return resList;
    }

    @Override
    public String findZcAddress(Userinfo userinfo) {
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("regionId", userinfo.getZcRegionId());
        return regionDao.getRegionDetailName(paramMap) + " " + userinfo.getZcXxAddress();
    }

    @Override
    public Map<String, Object> getOrderDetailInfo(long orderId) {
        //得到订单基本信息
        Map<String, Object> res = orderDao.singleOrderAttachProdAttachOrderLogisticsInfo(orderId);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        int orderState = (int) res.get("orderState");
        if (orderState == 0) {
            return res;
        }
        //从wa_order_log表中得到订单价格和数量
        Map<String, Object> orderlog = waOrderServiceImpl.findNewestOrderLog(orderId);
        res.putAll(orderlog);
        if (orderState == 1 || orderState == 2) {
            return res;
        }
        //从wa_zorder表中得到收货量和成交额
        Map<String, Object> zorderDeliverCount = waOrderServiceImpl.findReceiveProdCount(orderId);
        res.putAll(zorderDeliverCount);
        List<Map<String, Object>> zorders = waOrderServiceImpl.findZorders(orderId);
        res.put("zorders", zorders);
        return res;
    }

    /**
     * 判断有没有发货完成order_state:4？在判断其他子订单有没有确认收货zorder_state:2?都通过则order_state->5
     *
     * @param zorderId
     * @return
     */
    @Override
    public int zorderConfirmReceive(long zorderId, String receiveComment) {
        int res = 0;
        /*Zorder zorder=new Zorder();
        zorder.setZorderId(zorderId);
        zorder.setZorderState((byte)2);
        zorder.setReceiveComment(receiveComment);
        res=zorderDao.updateByPrimaryKey(zorder);*/
        Map<String, Object> updateMap = new HashMap();
        updateMap.put("zorderId", zorderId);
        updateMap.put("zorderState", (byte) 2);
        updateMap.put("receiveComment", receiveComment);
        res = zorderDao.updateByPrimaryKey(updateMap);
        Map<String, Object> zo = zorderDao.singleZorderByPrimaryKey(zorderId);
        long orderId = (long) zo.get("order_id");
        Map<String, Object> order = orderDao.singleOrderByPrimaryKey(orderId);
        if ((int) order.get("order_state") != 4) {
            return res;
        }
        Map queryzorder = new HashMap();
        queryzorder.put("orderId", orderId);
        List<Map<String, Object>> zorders = zorderDao.listZordersByConditions(queryzorder);
        for (Map<String, Object> zor : zorders) {
            if ((int) zor.get("zorder_state") != 2) {
                return res;
            }
        }
        Map updateOrder = new HashMap();
        updateOrder.put("orderState", (byte) 5);
        updateOrder.put("orderId", orderId);
        BigDecimal saleSjNum = new BigDecimal(0);
        BigDecimal saleSjMoney = new BigDecimal(0);
        for (Map<String, Object> zor : zorders) {
            saleSjNum = saleSjNum.add((BigDecimal) zor.get("zorder_num"));
            saleSjMoney = saleSjMoney.add((BigDecimal) zor.get("zorder_money"));
        }
        updateOrder.put("saleSjNum", saleSjNum);
        updateOrder.put("saleSjMoney", saleSjMoney);
        res += orderDao.updateOrderByID(updateOrder);
        if ((int) order.get("is_self_car") != 1) {
            return res;
        }
        Map updateLogisticsInfoMap = new HashMap();
        updateLogisticsInfoMap.put("orderId", orderId);
        updateLogisticsInfoMap.put("state", 5);
        res += logisticsInfoDao.updateByConditions(updateLogisticsInfoMap);
        messageServicesk.handleReceiveProdMessage(orderId,zorderId);
        return res;
    }

    @Override
    public void zorderDoubtReceive(long orderId,long zorderId, String receiveComment) {
        Map<String, Object> updateMap = new HashMap();
        updateMap.put("zorderId", zorderId);
        updateMap.put("zorderState", (byte) 11);
        updateMap.put("receiveComment", receiveComment);
        zorderDao.updateByPrimaryKey(updateMap);
        Map<String,Object> updateOrder=new HashMap();
        updateOrder.put("orderId",orderId);
        updateOrder.put("hasQuestion",1);
        orderDao.updateOrderByID(updateOrder);
    }

    @Override
    public List<Map<String, Object>> logisticsInfoListInfo(Map param) {
        List<Map<String, Object>> res = logisticsInfoDao.customerLogisticsInfoListInfo(param);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        return res;
    }

    @Override
    public int logisticsInfoListInfoCount(Map param) {
        int res = logisticsInfoDao.customerLogisticsInfoListInfoCount(param);
        return res;
    }

    @Override
    public Map<String, Object> grabLogisticsListInfo(long liId) {
        Map<String,Object> info=new HashMap<>();
        Map query = new HashMap();
        query.put("logisticsInfoId", liId);
        query.put("grabState", 0);
        List<Map<String, Object>> vehicleGrabs = vehicleGrabDao.listVehicleGrabAttachUserinfoByConditions(query);
        ConvertUtil.convertDataBaseMapToJavaMap(vehicleGrabs);
        for (Map<String,Object> vehicleGrab:vehicleGrabs){
            String senderZcAddress=regionServiceImpl.getDetailAddress(Long.parseLong(vehicleGrab.get("zcRegionId").toString()),(String)vehicleGrab.get("zcXxAddress"));
            vehicleGrab.put("senderZcAddress",senderZcAddress);
        }
        info.put("vehicleGrabs",vehicleGrabs);
        Map<String,Object> logisticsInfoView=logisticsInfoDao.singleLogisticsInfoViewByPrimaryKey(liId);
        ConvertUtil.convertDataBaseMapToJavaMap(logisticsInfoView);
        //以后改写买家期望运费
        logisticsInfoView.put("customerExceptCarriage",0);
        info.put("logisticsInfoView",logisticsInfoView);
        return info;
    }

    /**
     * 选择物流 update wa_vehicle_grab 表 state，update wa_logistics_info 表 state 及其他
     *
     * @param param
     * @return
     */
    @Override
    @Transactional
    public int chooseGrab(Map param) {
        int res = 0;
        long logisticsInfoId = Long.parseLong((String) param.get("logisticsInfoId"));
        long vehicleGrabId = Long.parseLong((String) param.get("grabId"));
        //update wa_vehicle_grab 表 state
        Map queryVG = new HashMap();
        queryVG.put("logisticsInfoId", logisticsInfoId);
        List<Map<String, Object>> vehicleGrabs = vehicleGrabDao.listVehicleGrabByConditions(queryVG);
        ConvertUtil.convertDataBaseMapToJavaMap(vehicleGrabs);
        for (Map<String, Object> vehicleGrab : vehicleGrabs) {
            if (vehicleGrabId == (long) vehicleGrab.get("vehicleGrabId")) {
                vehicleGrab.put("grabState", (byte) 1);
            } else {
                vehicleGrab.put("grabState", (byte) -1);
            }
            res += vehicleGrabDao.updateByPrimaryKey(vehicleGrab);
        }

        Map<String, Object> vehicleGrab = vehicleGrabDao.singleVehicleGrabByPrimaryKey(vehicleGrabId);
        Map<String, Object> updateLogisticsInfo = new HashMap();
        updateLogisticsInfo.put("logisticsId", logisticsInfoId);
        updateLogisticsInfo.put("state", (byte) 2);
        updateLogisticsInfo.put("orderPrice", ((BigDecimal) vehicleGrab.get("grab_money")).doubleValue());
        updateLogisticsInfo.put("senderUserId", (long) vehicleGrab.get("sender_user_id"));
        updateLogisticsInfo.put("vehicleGrabId", vehicleGrabId);
        res += logisticsInfoDao.updateByPrimaryKey(updateLogisticsInfo);

        messageServicesk.handleChooseGrabMessage(logisticsInfoId);
        return res;
    }

    /**
     * update wa_logistics_info 表 state=3，pre_payment=order_price
     * update order表：如果order_state:11-->2;如果order_state:1-->12
     *
     * @param logisticsInfoId
     */
    @Override
    @Transactional
    public void testPayLogistics(long logisticsInfoId) {
        Map<String, Object> logisticsInfo = logisticsInfoDao.singleLogisticsInfoByPrimaryKey(logisticsInfoId);
        ConvertUtil.convertDataBaseMapToJavaMap(logisticsInfo);
        logisticsInfo.remove("state");
        logisticsInfo.put("state", (byte) 3);
        logisticsInfo.put("prePayment", (double) logisticsInfo.get("orderPrice"));
        logisticsInfoDao.updateByPrimaryKey(logisticsInfo);

        Map<String, Object> order = orderDao.singleOrderByPrimaryKey((long) logisticsInfo.get("orderId"));
        byte orderState = (int) order.get("order_state") == 1 ? (byte) 12 : (byte) 2;
        Map updateOrder = new HashMap();
        updateOrder.put("orderId", (long) order.get("order_id"));
        updateOrder.put("orderState", orderState);
        orderDao.updateOrderByID(updateOrder);
    }

    /**
     * update order表：如果notSelfCar:order_state:12-->2;如果order_state:1-->11
     * 如果isSelfCar:order_state:2
     *
     * @param orderId
     */
    @Override
    public int testPayOrder(long orderId) {
        int res = 0;
        Map<String, Object> order = orderDao.singleOrderByPrimaryKey(orderId);
        byte orderState = 0;
        if ((int) order.get("is_self_car") == 0) {
            orderState = 2;
        } else if ((int) order.get("is_self_car") == 1) {
            orderState = (int) order.get("order_state") == 1 ? (byte) 11 : (byte) 2;
        }
        Map updateOrder = new HashMap();
        updateOrder.put("orderId", (long) order.get("order_id"));
        updateOrder.put("orderState", orderState);
        res += orderDao.updateOrderByID(updateOrder);
        return res;
    }

    @Override
    public Map<String, Object> getPayOrderPageInfo(long orderId, long userId) {
        Map<String, Object> res = orderDao.singleOrderinfoByPrimaryKey(orderId);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        Map<String, Object> res1 = waUserDao.singleUserByPrimaryKey(userId);
        res.put("userMoney", res1.get("user_money"));
        res.put("userCreditMoney", res1.get("user_credit_money"));
        return res;
    }

    @Override
    public Map<String, Object> getPayLogisticsPageInfo(long logisticsInfoId, long userId) {
        Map<String, Object> res = logisticsInfoDao.singleLogisticsInfoViewByPrimaryKey(logisticsInfoId);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        Map<String, Object> res1 = waUserDao.singleUserByPrimaryKey(userId);
        res.put("userMoney", res1.get("user_money"));
        res.put("userCreditMoney", res1.get("user_credit_money"));
        return res;
    }

    @Override
    public boolean isBalanceEnough(long userId, BigDecimal payMoney, int zfMethod, BigDecimal balanceZfMoney, BigDecimal loanZfMoney) {
        Map<String, Object> user = waUserDao.singleUserByPrimaryKey(userId);
        BigDecimal userMoney = (BigDecimal) user.get("user_money");
        BigDecimal userCreditMoney = (BigDecimal) user.get("user_credit_money");
        if (zfMethod == 2) {
            if (userMoney.compareTo(payMoney) < 0) {
                return false;
            }
        } else if (zfMethod == 4) {
            if (userMoney.compareTo(balanceZfMoney) < 0 || userCreditMoney.compareTo(loanZfMoney) < 0) {
                return false;
            }
        } else if (zfMethod == 3) {
            if (userCreditMoney.compareTo(payMoney) < 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int applyCreditLimit(Map<String, Object> params) {
        params.put("other1", "");
        params.put("other2", "");
        params.put("other3", "");
        params.put("other4", "");
        return creditDao.applyCreditLimit(params);
    }

    @Override
    public Map<String, Object> getCurrentCredit(long userId) {
        Map param = new HashMap();
        param.put("orderBy", "credit_apply_date desc");
        param.put("userId", userId);
        Map<String, Object> res = creditDao.singleCreditByConditions(param);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        return res;
    }

    @Override
    public BigDecimal getUserCreditSjMoney(long userId) {
        Map param = new HashMap();
        param.put("creditState", 1);
        param.put("userId", userId);
        Map<String, Object> credit = creditDao.singleCreditByConditions(param);
        if (null != credit && credit.size() != 0 && null != credit.get("credit_sj_money")) {
            return (BigDecimal) credit.get("credit_sj_money");
        }
        return new BigDecimal(0);
    }

    @Override
    public Map<String, Object> getSjCredit(long userId) {
        Map param = new HashMap();
        param.put("creditState", 1);
        param.put("userId", userId);
        Map<String, Object> credit = creditDao.singleCreditByConditions(param);
        if (null != credit && credit.size() > 0) {
            ConvertUtil.convertDataBaseMapToJavaMap(credit);
            return credit;
        }
        Map<String, Object> param1 = new HashMap();
        param1.put("creditState", -2);
        param1.put("userId", userId);
        param1.put("orderBy", "credit_deadline desc");
        Map<String, Object> deadcredit = creditDao.singleCreditByConditions(param1);
        if (null != deadcredit && deadcredit.size() > 0) {
            ConvertUtil.convertDataBaseMapToJavaMap(deadcredit);
            return deadcredit;
        }
        return null;

    }

    @Override
    public void updateUserCreditMoney(long userId) {
        updateUserCreditMoney(userId, getUserCreditSjMoney(userId));
    }

    @Override
    public void updateUserCreditMoney(long userId, BigDecimal creditSjMoney) {
        Map<String, Object> updatemap = new HashMap<>();
        BigDecimal userCreditMoney = creditSjMoney.subtract(getLoansSum(userId));
        updatemap.put("userId", userId);
        updatemap.put("userCreditMoney", userCreditMoney);
        waUserDao.updateUserByUserId(updatemap);
    }

    @Override
    public Map<String, Object> findCreditAccountPageInfo(Long userId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> sjCredit = getSjCredit(userId);
        res.put("credit", sjCredit);
        Map<String, Object> user = waUserDao.singleUserByPrimaryKey(userId);
        ConvertUtil.convertDataBaseMapToJavaMap(user);
        res.put("user", user);
        return res;
    }

    @Override
    public boolean isCreditApplyAvailable(Long userId) {
        Map<String, Object> credit = getCurrentCredit(userId);
        if (null == credit || credit.size() == 0) {
            return true;
        }
        int creditState = (int) credit.get("creditState");
        if (creditState == 0) {
            return false;
        }
        return true;
    }

    @Override
    public Map<String, Object> findCreditApplyPageInfo(Long userId) {
        Map<String, Object> res = new HashMap<>();
        Map<String, Object> sjCredit = getSjCredit(userId);
        res.put("sjCredit", sjCredit);
        return res;
    }

    @Override
    public BigDecimal getLoansSum(Long userId) {
        BigDecimal sumLoans = loanDao.getLoansSum(userId);
        return null == sumLoans ? new BigDecimal(0) : sumLoans;
    }

    @Override
    @Transactional
    public int repayLoanByBalance(long userId, long loanId, BigDecimal principal, BigDecimal interest, String ip) {
        Map<String, Object> user = waUserDao.singleUserByPrimaryKey(userId);
        BigDecimal oldUserMoney = (BigDecimal) user.get("user_money");
        if (oldUserMoney.compareTo(principal.add(interest)) < 0) {
            return 0;
        }

        Map<String, Object> loan = loanDao.singleLoanByPrimaryKey(loanId);
        //update table wa_loan
        Map<String, Object> updateLoan = new HashMap<>();
        updateLoan.put("loanId", loanId);
        updateLoan.put("repayMoney", ((BigDecimal) loan.get("repay_money")).add(principal));
        updateLoan.put("repayLixi", ((BigDecimal) loan.get("repay_lixi")).add(interest));
        BigDecimal remainRepayMoney = ((BigDecimal) loan.get("remain_repay_money")).subtract(principal);
        updateLoan.put("remainRepayMoney", remainRepayMoney);
        updateLoan.put("remainLixiMoney", new BigDecimal(0));
        if (new BigDecimal(0).compareTo(remainRepayMoney) == 0) {
            updateLoan.put("loanState", 3);
        }
        loanDao.updateLoanByPrimaryKey(updateLoan);

        //insert table wa_repay
        Repay repay = new Repay();
        repay.setLoanId(loanId);
        repay.setUserId(userId);
        repay.setRepayMoney(principal);
        repay.setRepayInterestMoney(interest);
        repay.setRepayDate(new Date());
        repay.setRepayIp(ip);
        repayDao.createRepay(repay);

        Map<String, Object> updateuser = new HashMap<>();
        updateuser.put("userId", userId);
        updateuser.put("userMoney", oldUserMoney.subtract(principal.add(interest)));
        waUserDao.updateUserByUserId(updateuser);

        checkLoanRepayedOff(userId, loanId);

        return 1;
    }

    @Override
    public boolean isLoanRepayedOff(long loanId) {
        Map<String, Object> loan = loanDao.singleLoanByPrimaryKey(loanId);
        BigDecimal zero = new BigDecimal(0);
        if (zero.compareTo((BigDecimal) loan.get("remain_pay_money")) == 0 && zero.compareTo((BigDecimal) loan.get("remain_lixi_money")) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkLoanRepayedOff(long userId, long loanId) {
        Map<String, Object> loan = loanDao.singleLoanByPrimaryKey(loanId);
        if (3 == (int) loan.get("loan_state")) {
            updateUserCreditMoney(userId);
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> getLoansIndebt(Map params) {
        params.put("loanState", 2);
        List<Map<String, Object>> res = loanDao.listLoanByConditions(params);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        return res;
    }

    @Override
    public int getLoansIndebtCount(Map params) {
        params.put("loanState", 2);
        int count = loanDao.listLoanByConditionsCount(params);
        return count;
    }

    @Override
    public List<Map<String, Object>> getLoansRepayDetail(Map params) {
        params.put("inLoanState", "(2,3)");
        List<Map<String, Object>> res = loanDao.listLoanOrderViewByConditions(params);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        if (null != res && res.size() > 0) {
            for (Map<String, Object> loan : res) {
                Map<String, Object> query = new HashMap<>();
                query.put("loanId", loan.get("loanId"));
                List<Map<String, Object>> repays = repayDao.listRepayByConditions(query);
                ConvertUtil.convertDataBaseMapToJavaMap(repays);
                loan.put("repays", repays);
            }
        }
        return res;
    }

    @Override
    public int getLoansRepayDetailCount(Map params) {
        params.put("inLoanState", "(2,3)");
        int res = loanDao.listLoanByConditionsCount(params);
        return res;
    }

    @Override
    public List<Map<String, Object>> getCreditList(Map params) {
        params.put("orderBy", "credit_apply_date desc");
        List<Map<String, Object>> res = creditDao.listCreditByConditions(params);
        ConvertUtil.convertDataBaseMapToJavaMap(res);
        return res;
    }

    @Override
    public int getCreditListCount(Map params) {
        int res = creditDao.listCreditByConditionsCount(params);
        return res;
    }


}

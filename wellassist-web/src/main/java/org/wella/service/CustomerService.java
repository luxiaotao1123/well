package org.wella.service;

import org.wella.entity.Order;
import org.wella.entity.Prod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuwen on 2017/5/10.
 */
public interface CustomerService {
    /**
     * @param map 表单中提交的内容
     * 买方下订单操作,主要完成以下操作
     * 1.根据表单填写的内容，向表wa_order表中插入一条记录，此时订单的业务编发要根据相应的逻辑生成
     * 2.在表wa_order_info表中生成一条记录
     */
    void order(Map map);

    /**
     *订单支付
     * @param orderId
     *
     */
    void orderPay(int orderId);

    /**
     *订单取消支付
     * @param orderId
     */
    void cancelPay(int orderId);

    /**
     *物流订单处理，选择物流单位
     * @param deliveryOrderId
     *
     */
    void processDeliveryOrder(int deliveryOrderId);

    /**
     *物流订单支付处理
     * @param deliveryOrderId
     */
    void deliveryOrderPay(int deliveryOrderId);

    /**
     *确认发货
     * @param deliveryOrderId
     */
    void confirmDelivery(int deliveryOrderId);

    /**
     *评价对方
     * @param orderId
     */
    void evaluate(int orderId);

    /**
     *贷款申请
     * @param customerId
     */
    void loanApply(int customerId);

    /**
     *贷款到账确认
     */
    void loanConfirm();

    /**
     *查询订单列表
     * @return
     */
    List<Order> findOrderList();

    /**
     *查询订单列表
     * @param map
     * @return
     */
    List<Order> findOrderList(Map map);

    /**
     *创建订单
     */
    void createOrder();

    /**
     *更新订单
     * @param order
     */
    void updateOrder(Order order);

    /**
     *删除订单
     * @param orderId
     */
    void deleteOrder(int orderId);

    /**
     *获取产品列表
     * @return
     */
    List<Prod> findProdList();

    /**
     *获取产品列表
     * @param map
     * @return
     */
    List<Prod> findProdList(Map map);

}
package org.wella.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wellapay.cncb.model.ForceTransferBasicInfo;
import io.wellassist.utils.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.logging.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.wella.common.ctrl.BaseController;
import org.wella.common.utils.CommonUtil;
import org.wella.common.utils.ConstantUtil;
import org.wella.common.utils.ConvertUtil;
import org.wella.common.vo.MyInfo;
import org.wella.dao.*;
import org.wella.entity.*;
import org.wella.service.AdminSubAccountService;
import org.wella.service.FinanceService;
import org.wella.service.WaOrderService;
import org.wella.service.WaScheduleJobService;
import org.wella.service.impl.CustomerServiceImpl;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by ailing on 2017/5/11.
 */
@Controller
@RequestMapping(value = "/customer/")
public class CustomerController extends BaseController {

   private static org.apache.commons.logging.Log log = LogFactory.getLog(CustomerController.class);

   @Autowired
   private CustomerServiceImpl customerServiceImpl;

   @Autowired
   private WaUserDao userDao;

   @Autowired
   private ProdDao prodDao;

   @Autowired
   private OrderDao orderDao;

   @Autowired
   private UserinfoDao userinfoDao;

   @Autowired
   private WithdrawDAO withdrawDAO;

   @Autowired
   private WaOrderService waOrderServiceImpl;

   @Autowired
   private BankcardDao bankcardDao;

   @Autowired
   private TradeDAO tradeDao;

   @Autowired
   private FinanceService financeServiceImpl;

   @Autowired
   private BankOrderDao bankOrderDao;

   @Autowired
   private UserSubAccountDao userSubAccountDao;

   @Autowired
   private CncbTransDao cncbTransDao;

   @Autowired
   private LogisticsInfoDao logisticsInfoDao;

   @Autowired
   private UserMoneyDao userMoneyDao;

   @Autowired
   private OrderTransDao orderTransDao;

   @Autowired
   private AdminSubAccountService adminSubAccountServiceImpl;

   @Autowired
   private LoanDao loanDao;

   /**
    * 买家下订单
    * @param map 订单详情 (prodId，saleNum，danjia，saleMoney，isSelfCar，contacts，conTel，toRegionId
    *                 toRegionAddr，orderData，deliverDate，receiveDate，customerExceptCarriage)
    * @param response response
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping(value = "order", method = RequestMethod.POST)
   @ResponseBody
   public R order(@RequestParam Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
      User user = (User) HttpContextUtils.getHttpServletRequest().getSession().getAttribute("user");
      long userId = user.getUserId();
      map.put("userId", userId);
      String ip = getIpAddr(request);
      map.put("orderIp", ip);
      JSONObject res = new JSONObject();
      long orderId=0;
      try {
         orderId=customerServiceImpl.order(map);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
      return R.ok().put("orderId",orderId);
   }

   /**
    * 跳转下单页面
    * @param request request
    * @param response response
    * @param model model
    * @return view
    */
   @RequestMapping("makeOrder")
   public String makeOrder(HttpServletRequest request, HttpServletResponse response, Model model) {
      String prodId = CommonUtil.GetRequestParam(request, "prodId", "0");
      HashMap param = new HashMap();
      param.put("prodId", prodId);
      Map prodMap = customerServiceImpl.findProdById(param);

      HashMap param1 = new HashMap();
      param1.put("parentRegionId", "0");
      List shengRegionList = customerServiceImpl.getRegionList(param1);
      User user=(User)HttpContextUtils.getAttribute("user");
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("shengRegionList", shengRegionList);
      model.addAttribute("prod", prodMap);
      model.addAttribute("prodId", prodId);
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "1");
      return "views/front/customer/orderPage_new.jsp";
   }

   /**
    * 跳转下单成功页面
    * @param orderId 订单id
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping({"orderSuccess"})
   public String orderSuccess(@RequestParam("orderId")long orderId, HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      Map<String,Object> order=orderDao.singleOrderByPrimaryKey(orderId);
      model.addAttribute("username", user.getUserName());
      model.addAttribute("orderNo",order.get("order_no"));
      model.addAttribute("orderDate",order.get("order_date"));
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "1");
      return "views/front/customer/ordersheetresult.jsp";
   }

   /**
    * 跳转订单详情页面
    * @param orderId 订单id
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping("orderDetail")
   public String orderDetail(@RequestParam("orderId") String orderId, HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      Map<String, Object> orderDetail = customerServiceImpl.getOrderDetailInfo(Long.parseLong(orderId));
      model.addAttribute("info", orderDetail);
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "1");
      return "views/front/customer/order/orderDetail_new.jsp";
   }

   /**
    * 分批发货确认收货
    * @param zorderId wa_zorder表主键
    * @param receiveComment 收货备注
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping(value = "zorderConfirmReceive", method = RequestMethod.GET)
   @ResponseBody
   public R zorderConfirmReceive(@RequestParam("zorderId") String zorderId, @RequestParam("receiveComment") String receiveComment) {
      int res = customerServiceImpl.zorderConfirmReceive(Long.parseLong(zorderId), receiveComment);
      if (res > 0) {
         return R.ok();
      }
      return R.error();
   }

   /**
    * 子订单收货存疑
    * @param orderId wa_order表主键
    * @param zorderId wa_zorder表主键
    * @param receiveComment 收货意见
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping(value = "zorderDoubtReceive", method = RequestMethod.GET)
   @ResponseBody
   public R zorderDoubtReceive(@RequestParam("orderId")String orderId,@RequestParam("zorderId") String zorderId, @RequestParam("receiveComment") String receiveComment) {
      try {
         customerServiceImpl.zorderDoubtReceive(Long.parseLong(orderId),Long.parseLong(zorderId), receiveComment);
      } catch (NumberFormatException e) {
         e.printStackTrace();
         return R.error();
      }
      return R.ok();
   }


   /**
    * 跳转物流详情页面
    * @param orderId wa_order表主键
    * @param model model
    * @return view
    */
   @RequestMapping("logisticsDetail")
   public String logisticsDetail(HttpServletRequest request, @RequestParam("orderId") String orderId, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      Map<String, Object> info = waOrderServiceImpl.findOrderLogisticsInfo(Long.parseLong(orderId));
      model.addAttribute("info", info);
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "1");
      return "views/front/customer/order/expressDetail.jsp";
   }


   /**
    * 跳转买家物流订单界面
    * @param request requset
    * @param model model
    * @return view
    */
   @RequestMapping("logisticsInfoList")
   public String logisticsInfoList(HttpServletRequest request, Model model) {
      Map param = getConditionParam(request);
      User user = (User) request.getSession().getAttribute("user");
      long userId = user.getUserId();
      param.put("userId", userId);
      List<Map<String, Object>> info = customerServiceImpl.logisticsInfoListInfo(param);
      int totalCount = customerServiceImpl.logisticsInfoListInfoCount(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("info", info);
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "2");
      model.addAttribute("userName", user.getUserName());
      return "views/front/customer/order/wlOrderList.jsp";
   }

   /**
    * 跳转抢单信息页面
    * @param logisticsInfoId wa_logistics_info 表主键
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping("grabLogisticsList")
   public String grabLogisticsList(@RequestParam("logisticsInfoId") String logisticsInfoId, HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      long LIId = Long.parseLong(logisticsInfoId);
      Map<String, Object> info = customerServiceImpl.grabLogisticsListInfo(LIId);
      model.addAttribute("info", info);
      model.addAttribute("logisticsInfoId", LIId);
      model.addAttribute("username", user.getUserName());
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "2");
      return "views/front/customer/order/editQiangdan.jsp";
   }

   /**
    *买家选择物流抢单
    * @param param logisticsInfoId:wa_logistics_info表主键，vehicleGrabId：wa_vehicle_grab表主键
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping("chooseGrab")
   @ResponseBody
   public R chooseGrab(@RequestParam Map param) {
      try {
         customerServiceImpl.chooseGrab(param);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
      return R.ok();
   }

   /**
    * 买家支付订单的处理方法
    * @param request orderId:wa_order表主键，saleMoney:预付款价格，zfMethod：1、网银付款 2 余额付款 3。授信付款组合付款 4组合付款、, 5-线下支付
    *                balance：余额支付部分，loan：贷款支付部分，certificateImg：线下支付凭证（图片上传后回调的url）
    * @param response response:{status:1成功，-1失败，content:“操作成功”/“操作失败”}
    */
   @RequestMapping(
           value = {"payOrder"},
           method = {RequestMethod.POST}
   )
   public void payOrder(HttpServletRequest request, HttpServletResponse response) {
      String ret = "-1";
      JSONObject obj = new JSONObject();
      obj.put("content", ConstantUtil.MSG_PARAM_ERR);
      final String orderId = CommonUtil.GetRequestParam(request, "orderId", "0");
      final String saleMoney = CommonUtil.GetRequestParam(request, "saleMoney", "0.00");
      final String loan = CommonUtil.GetRequestParam(request, "loan", "0");
      final String balance = CommonUtil.GetRequestParam(request, "balance", "0");
      final String zfMethod = CommonUtil.GetRequestParam(request, "zfMethod", "2");
      String certificateImg = "";
      final String ip = IPUtils.getIpAddr(request);
      try {
         //资金不能从session里面拿！！！
         final User user = (User) request.getSession().getAttribute("user");
         if (!customerServiceImpl.isBalanceEnough(user.getUserId(), new BigDecimal(saleMoney), Integer.parseInt(zfMethod), new BigDecimal(balance), new BigDecimal(loan))) {
            obj.put("content", ConstantUtil.MSG_MONEY_ERR);
            obj.put("status", "-1");
            this.echo(response, obj);
            return;
         }
         if (zfMethod.equals("5")) {
            certificateImg = CommonUtil.GetRequestParam(request, "certificateImg", "");
            if ("".equals(certificateImg)) {
               obj.put("content", ConstantUtil.MSG_PARAM_ERR);
               obj.put("status", "-1");
               this.echo(response, obj);
               return;
            }
         }
         if (user != null && CommonUtil.getIntFromString(orderId) > 0) {
            Map orderObj = this.getMyOneSingBO("wa_order", "order_id", Long.parseLong(orderId));
            if (orderObj != null && orderObj.get("userId") != null && (long) orderObj.get("userId") == (user.getUserId()) && orderObj.get("orderState") != null && ((int) orderObj.get("orderState") == 1)) {

               int zfMethodi=Integer.parseInt(zfMethod);
               if (zfMethodi==2||zfMethodi==4){
                  BigDecimal Bbalance=new BigDecimal(balance);
                  if (zfMethodi==2){
                     Bbalance=new BigDecimal(saleMoney);
                  }
                  final BigDecimal finalBbalance = Bbalance;
                  final String finalCertificateImg = certificateImg;
                  final AdminSubAccount orderTransfer=adminSubAccountServiceImpl.findOrderTransferAccount();
                  new Thread(new Runnable() {
                     @Override
                     public void run() {
                        UserSubAccount userSubAccount=financeServiceImpl.getUserSubAccountByUserId(user.getUserId());
                        Map<String,String> paramss=new HashMap<>();
                        paramss.put("payAccNo",userSubAccount.getSubAccNo().toString());
                        paramss.put("recvAccNo",orderTransfer.getSubAccNo());
                        paramss.put("recvAccNm",orderTransfer.getSubAccNm());
                        paramss.put("tranAmt", finalBbalance.toString());
                        JSONObject memo=new JSONObject();
                        memo.put("orderId",orderId);
                        memo.put("type",1);
                        memo.put("content","预付款余额付款");
                        paramss.put("memo",memo.toString());
                        String result= null;
                        try {
                           result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
                        } catch (Exception e) {
                           e.printStackTrace();
                        }
                        R r= JSON.parseObject(result,R.class);
                        ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
                        CncbTrans cncbTrans=new CncbTrans();
                        cncbTrans.setXml(forceTransferBasicInfo.getXml());
                        cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                        cncbTrans.setTime(new Date());
                        cncbTrans.setType((byte)1);
                        JSONObject operationParamsObj=new JSONObject();
                        operationParamsObj.put("sql"," CALL khFukuanProcess(\'" + user.getUserId() + "\',\'" + orderId + "\',\'" + saleMoney + "\',\'" + zfMethod + "\',\'" + balance + "\',\'" + loan + "\',\'" + finalCertificateImg + "\',\'" + ip + "\')");
                        operationParamsObj.put("orderId",orderId);
                        cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                        cncbTransDao.create(cncbTrans);
                     }
                  }).start();
                  Map<String,Object> query=new HashMap<>();
                  query.put("orderId",Long.parseLong(orderId));
                  query.put("prodPayState",3);
                  orderDao.updateOrderByID(query);
                  ret="1";
                  obj.put("content","处理中...");
               }else {
                  String sql = "";
                  sql = " CALL khFukuanProcess(\'" + user.getUserId() + "\',\'" + orderId + "\',\'" + saleMoney + "\',\'" + zfMethod + "\',\'" + balance + "\',\'" + loan + "\',\'" + certificateImg + "\',\'" + ip + "\')";
                  HashMap queryParam = new HashMap();
                  queryParam.put("strsql", sql);
                  this.commonMapper.simpleSelectReturnList(queryParam);
                  waOrderServiceImpl.checkOrderRepayOff(Long.parseLong(orderId));
                  ret = "1";
                  obj.put("content", ConstantUtil.MSG_SUCCESS);
               }
            }
         }
      }catch (Exception var22) {
         ret = "-2";
         obj.put("content", ConstantUtil.MSG_FAILS);
      }
      obj.put("status", ret);
      this.echoJSON(response, obj);
   }

   /**
    * 物流预付款处理
    * @param params orderId：wa_order主键，logisticsInfoId：wa_logistics_info主键，grabMoney：抢单报价，zfMethod：1、网银付款 2 余额付款 3。授信付款组合付款 4组合付款、, 5-线下支付
    *               balance：余额支付部分，loan：贷款支付部分，certificateImg：线下支付凭证（图片上传后回调的url）
    * @param request request
    * @param response response
    * @return response:{status:1成功，-1失败，content:“操作成功”/“操作失败”}
    * @throws Exception
    */
   @RequestMapping(value = {"payLogistics"},
           method = {RequestMethod.POST})
   @ResponseBody
   public JSONObject payLogistics(@RequestParam final Map<String,Object> params, HttpServletRequest request, HttpServletResponse response) throws Exception {
      final JSONObject obj = new JSONObject();
      final String orderId = CommonUtil.GetRequestParam(request, "orderId", "0");
      final String logisticsInfoId = CommonUtil.GetRequestParam(request, "logisticsInfoId", "0");
      final String grabMoney = CommonUtil.GetRequestParam(request, "grabMoney", "0");
      final String rate = CommonUtil.GetRequestParam(request, "rate", "0");
      final String balance = CommonUtil.GetRequestParam(request, "balance", "0");
      final String loan = CommonUtil.GetRequestParam(request, "loan", "0");
      final String zfMethod = CommonUtil.GetRequestParam(request, "zfMethod", "2");
      String certificateImg = "";
      final String ip = IPUtils.getIpAddr(request);
      final User user = (User) request.getSession().getAttribute("user");
      /*if (!customerServiceImpl.isBalanceEnough(user.getUserId(), new BigDecimal(grabMoney), Integer.parseInt(zfMethod), new BigDecimal(balance), new BigDecimal(loan))) {
         obj.put("content", ConstantUtil.MSG_MONEY_ERR);
         obj.put("status", "-1");
         return obj;
      }*/
      if (zfMethod.equals("5")) {
         certificateImg = CommonUtil.GetRequestParam(request, "certificateImg", "");
         if ("".equals(certificateImg)) {
            obj.put("content", ConstantUtil.MSG_PARAM_ERR);
            obj.put("status", "-1");
            return obj;
         }
      }
      if (user != null && CommonUtil.getIntFromString(orderId) > 0) {
         Map orderObj = this.getMyOneSingBO("wa_order", "order_id", Long.parseLong(orderId));
         if (orderObj != null && orderObj.get("userId") != null && (long) orderObj.get("userId") == (user.getUserId()) && orderObj.get("orderState") != null && ((int) orderObj.get("orderState") == 1 || (int) orderObj.get("orderState") == 11) || (int) orderObj.get("orderState") == 13) {
            final String finalCertificateImg = certificateImg;
            final AdminSubAccount orderTransfer=adminSubAccountServiceImpl.findOrderTransferAccount();
            int zfMethodi = Integer.parseInt(zfMethod);
            if (zfMethodi == 2 || zfMethodi == 4) {
               BigDecimal Bbalance = new BigDecimal(balance);
               if (zfMethodi == 2) {
                  Bbalance = new BigDecimal(grabMoney);
               }
               final BigDecimal finalBbalance = Bbalance;
               new Thread(new Runnable() {
                  @Override
                  public void run() {
                     Map<String, Object> query = new HashMap<>();
                     query.put("userId", user.getUserId());
                     UserSubAccount userSubAccount = userSubAccountDao.singleQuery(query);
                     Map<String, String> paramss = new HashMap<>();
                     paramss.put("payAccNo", userSubAccount.getSubAccNo().toString());
                     paramss.put("recvAccNo",orderTransfer.getSubAccNo());
                     paramss.put("recvAccNm",orderTransfer.getSubAccNm());
                     paramss.put("tranAmt", finalBbalance.toString());
                     JSONObject memo=new JSONObject();
                     memo.put("orderId",orderId);
                     memo.put("logisticsInfoId",logisticsInfoId);
                     memo.put("type",3);
                     memo.put("content","物流预付款余额付款");
                     paramss.put("memo",memo.toString());
                     String result = null;
                     try {
                        result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL + "forceTransfer", paramss);
                     } catch (Exception e) {
                        e.printStackTrace();
                     }
                     R r = JSON.parseObject(result, R.class);
                     ForceTransferBasicInfo forceTransferBasicInfo = JSON.parseObject(r.get("forceTransferBasicInfo").toString(), ForceTransferBasicInfo.class);
                     CncbTrans cncbTrans = new CncbTrans();
                     cncbTrans.setXml(forceTransferBasicInfo.getXml());
                     cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                     cncbTrans.setTime(new Date());
                     cncbTrans.setType((byte) 3);
                     JSONObject operationParamsObj = new JSONObject();
                     operationParamsObj.put("sql", " CALL logisticsPayProcess(\'" + user.getUserId() + "\',\'" + orderId + "\',\'" + logisticsInfoId + "\',\'" + grabMoney + "\',\'" + zfMethod + "\',\'" + balance + "\',\'" + loan + "\',\'" + rate + "\',\'" + finalCertificateImg + "\',\'" + ip + "\')");
                     operationParamsObj.put("orderId", orderId);
                     cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                     cncbTransDao.create(cncbTrans);
                  }
               }).start();
               Map<String, Object> query1 = new HashMap();
               query1.put("orderId", Long.parseLong(orderId));
               query1.put("logisticsPayState", 3);
               orderDao.updateOrderByID(query1);
               obj.put("status", "1");
               obj.put("content", "处理中...");
               return obj;
            } else {
               String sql = "";
               sql = " CALL logisticsPayProcess(\'" + user.getUserId() + "\',\'" + orderId + "\',\'" + logisticsInfoId + "\',\'" + grabMoney + "\',\'" + zfMethod + "\',\'" + balance + "\',\'" + loan + "\',\'" + rate + "\',\'" + certificateImg + "\',\'" + ip + "\')";
               HashMap queryParam = new HashMap();
               queryParam.put("strsql", sql);
               ArrayList<Map<String, Object>> result = this.commonMapper.simpleSelectReturnList(queryParam);
               if ((int) result.get(0).get("result") == 1) {
                  waOrderServiceImpl.checkOrderRepayOff(Long.parseLong(orderId));
                  obj.put("content", ConstantUtil.MSG_SUCCESS);
                  obj.put("status", "1");
                  return obj;
               }
            }
         }
      }
      obj.put("content", ConstantUtil.MSG_FAILS);
      obj.put("status", "-1");
      return obj;
   }

   /**
    * 跳转订单付款页面
    * @param orderId wa_order表主键
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping("goPayOrder")
   public String goPayOrder(@RequestParam("orderId") String orderId, HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      Map<String, Object> orderInfo = customerServiceImpl.getPayOrderPageInfo(Long.parseLong(orderId), user.getUserId());
      model.addAttribute("orderInfo", orderInfo);
      model.addAttribute("user", user);
      model.addAttribute("userSumCredit",customerServiceImpl.getUserCreditSjMoney(user.getUserId()));
      model.addAttribute("parentMenuNo", "2");
      return "views/front/customer/order/editFukuan.jsp";
   }

   /**
    * 跳转物流订单付款
    * @param logisticsInfoId wa_logistics_info表主键
    * @param request requuest
    * @param model model
    * @return view
    */
   @RequestMapping("goPayLogistics")
   public String goPayLogistics(@RequestParam("logisticsInfoId") String logisticsInfoId, HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      Map<String, Object> logisticsInfo = customerServiceImpl.getPayLogisticsPageInfo(Long.parseLong(logisticsInfoId), user.getUserId());
      model.addAttribute("logisticsInfo", logisticsInfo);
      model.addAttribute("user", user);
      model.addAttribute("userSumCredit",customerServiceImpl.getUserCreditSjMoney(user.getUserId()));
      model.addAttribute("parentMenuNo", "2");
      return "views/front/customer/order/payLogistics.jsp";
   }

   /**
    * 得到子区域列表
    * @param request pid 父区域id
    * @param response response
    * @param model model
    */
   @RequestMapping(
           value = {"getRegionList"},
           method = {RequestMethod.POST}
   )
   public void getRegionList(HttpServletRequest request, HttpServletResponse response, Model model) {
      JSONObject obj = new JSONObject();
      String pid = CommonUtil.GetRequestParam(request, "pid", "0");
      Map param = this.getConditionParam(request);
      param.put("parentRegionId", pid);
      List regionList = customerServiceImpl.getRegionList(param);
      obj.put("regionList", regionList);
      this.echoJSON(response, obj);
   }

   /**
    * 跳转修改密码页面
    * @param model model
    * @return view
    */
   @RequestMapping("password")
   public String changePassword(Model model) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      model.addAttribute("parentMenuNo", "4");
      model.addAttribute("childMenuNo", "3");
      model.addAttribute("userName", user.getUserName());
      return "views/front/customer/company/changePass.jsp";
   }

   /**
    * 修改支付密码
    * @param map paynewpass新支付密码
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping("changePayPassword")
   @ResponseBody
   public R changePayPassword(@RequestParam Map<String, Object> map) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      String paynewpass = map.get("paynewpass").toString();
      //MD5加密
      String password = CommonUtil.MD5(paynewpass);
      user.setCzPass(password);
      Map<String, Object> updateMap = new HashedMap();
      updateMap.put("userId", user.getUserId());
      updateMap.put("czPassword", user.getCzPass());
      try {
         userDao.resetPassword(updateMap);
         return R.ok().put("state", 1);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error().put("state", 0);
      }
   }

   /**
    * 检验支付密码
    * 修改支付密码前要输入原有的支付密码，原有的支付密码从session中获取
    * @param map payoldpass 旧支付密码
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping("checkPayPassword")
   @ResponseBody
   public R checkPayPassword(@RequestParam Map<String, Object> map) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      String payoldpass = map.get("oldPass").toString();
      //MD5加密
      String password = CommonUtil.MD5(payoldpass);
      if (password.equals(user.getCzPass())) {
         return R.ok().put("state", 1);
      } else {
         return R.error().put("state", 0);
      }
   }

   /**
    * 修改登录密码
    * 1.修改session中的操作密码，2.修改数据库中session密码
    * @param map newpass新登录密码
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping("changeLoginPassword")
   @ResponseBody
   public R changeLoginPassword(@RequestParam Map<String, Object> map) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      String newpass = map.get("newpass").toString();
      //MD5加密
      String password = CommonUtil.MD5(newpass);
      user.setUserPass(password);
      Map<String, Object> updateMap = new HashedMap();
      updateMap.put("userId", user.getUserId());
      updateMap.put("userPassword", user.getUserPass());
      try {
         userDao.resetPassword(updateMap);
         return R.ok().put("state", 1);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error().put("state", 0);
      }
   }

   /**
    * 检验登录密码
    * 修改登录密码前要输入原有的登录密码，原有的登录密码可以从session中获取
    * @param map payoldpass旧登录密码
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping("checkLoginPassword")
   @ResponseBody
   public R checkLoginPassword(@RequestParam Map<String, Object> map) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      String payoldpass = map.get("oldPass").toString();
      //MD5加密
      String password = CommonUtil.MD5(payoldpass);
      if (password.equals(user.getUserPass())) {
         return R.ok().put("state", 1);
      } else {
         return R.error().put("state", 0);
      }
   }

   /**
    * 检验支付密码
    * @param oldPass 旧支付密码
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping("checkCzPassword")
   @ResponseBody
   public R checkWithdrawPassword(@RequestParam("pass") String oldPass, HttpServletResponse response) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      //MD5加密
      String password = CommonUtil.MD5(oldPass);
      if (password.equals(user.getCzPass())) {
         return R.ok();
      } else {
         return R.error().put("msg","支付密码错误");
      }
   }

   /**
    * 商品详情页跳转
    * @param prodId wa_prod表主键
    * @param model model
    * @return view
    */
   @RequestMapping("prodDetail")
   public String prodDetail(@RequestParam("prodId") String prodId, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("parentMenuNo", "5");
      return "views/front/customer/order/prodDetail.jsp";
   }

   /**
    * 买家绑定卖家提供的商品列表
    * @param map 分页参数
    * @param model model
    * @return view
    */
   @RequestMapping("prodList")
   public String prodList(@RequestParam Map<String, Object> map, Model model,HttpServletRequest request) {
      /*User user = (User) HttpContextUtils.getAttribute("user");*/
      User user=(User)request.getSession().getAttribute("user");
      map.put("userId", user.getSupplyId());
      map.put("prodState",2);
      List<Prod> prodList = prodDao.findProdByUserId(map);
      model.addAttribute("spList", prodList);
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("parentMenuNo", "5");
      model.addAttribute("childMenuNo", "0");
      return "views/front/customer/prod/prodList.jsp";
   }


   /**
    * 买方订单列表
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping("orderList")
   public String orderList(HttpServletRequest request, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      Map param = this.getConditionParam(request);
      param.put("userId", user.getUserId());
      ArrayList<Map<String, Object>> waOrderList = this.orderDao.getWaOrderList(param);
      ConvertUtil.convertDataBaseMapToJavaMap(waOrderList);
      for (Map<String, Object> waOrder : waOrderList) {
         Map<String, Object> orderlog = waOrderServiceImpl.findNewestOrderLog(Long.parseLong(waOrder.get("orderId").toString()));
         if (orderlog != null && orderlog.size() > 0) {
            waOrder.putAll(orderlog);
         }
      }
      model.addAttribute("waOrderList", waOrderList);
      int totalCount = this.orderDao.getWaOrderListCount(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("parentMenuNo", "1");
      model.addAttribute("childMenuNo", "1");
      return "views/front/customer/order/prodOrderList.jsp";
   }

   /**
    * 买方账户信息页面
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping("accountInfo")
   public String accountInfo(HttpServletRequest request, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      Map userInfo = this.getUserInfo(user.getUserId().toString());
      Map param = this.getConditionParam(request);
      param.put("userId", user.getUserId());
      param.put("jyState", "2");
      List list = this.userMoneyDao.getJyList(param);
      ConvertUtil.convertDataBaseMapToJavaMap(list);
      int totalCount = this.userMoneyDao.getJyListCount(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("parentMenuNo", "2");
      model.addAttribute("childMenuNo", "1");
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("userMoney", userInfo.get("userMoney"));
      model.addAttribute("list", list);
      return "views/front/customer/finance/accountInfo.jsp";
   }

   /*@RequestMapping({"messagePage"})
   public String news_list(HttpServletRequest request, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      Map param = this.getConditionParam(request);
      param.put("userId", user.getUserId());
      ArrayList newsList = this.newsMapper.getNewsList(param);
      ConvertUtil.convertDataBaseMapToJavaMap(newsList);
      model.addAttribute("newsList", newsList);
      int totalCount = this.newsMapper.getNewsListCount(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      param.put("strsql", "SELECT COUNT(*) FROM wa_info WHERE is_read = 0 AND user_id = " + user.getUserId());
      model.addAttribute("noReadCount", Integer.valueOf(this.commonMapper.simpleSelectReturnInt(param)));
      model.addAttribute("totalCount", Integer.valueOf(totalCount));
      model.addAttribute("parentMenuNo", "3");
      model.addAttribute("childMenuNo", "1");
      model.addAttribute("userName", user.getUserName());
      return "views/front/customer/news/xxList.jsp";
   }*/

   /**
    * 进入个人中心，查看企业信息
    * @param model
    * @return view
    */
   @RequestMapping("companyInfo")
   public String companyInfo(Model model) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      Map user = userDao.singleUserByPrimaryKey(((User) httpSession.getAttribute("user")).getUserId());
      ConvertUtil.convertDataBaseMapToJavaMap(user);
      Map userinfo = userinfoDao.singleUserinfoByPrimaryKey(((Userinfo) httpSession.getAttribute("userInfo")).getUserId());
      ConvertUtil.convertDataBaseMapToJavaMap(userinfo);
      model.addAttribute("user", user);
      model.addAttribute("userInfo", userinfo);
      model.addAttribute("parentMenuNo", "4");
      model.addAttribute("childMenuNo", "1");
      String pParam = userinfo.get("zcRegionId").toString().substring(0, 2) + "0000";
      String cParam = userinfo.get("zcRegionId").toString().substring(0, 4) + "00";
      //省列表
      model.addAttribute("provinceList", this.getChildRegionList(0));
      model.addAttribute("provinceId", pParam);
      //市列表
      model.addAttribute("cityList", this.getChildRegionList(CommonUtil.getIntFromString(pParam)));
      model.addAttribute("cityId", cParam);
      //区列表
      model.addAttribute("countyList", this.getChildRegionList(CommonUtil.getIntFromString(cParam)));
      model.addAttribute("userName", user.get("userName"));
      return "views/front/customer/company/companyInfo.jsp";
   }

   /**
    * @param model
    * @return
    */
   @RequestMapping("bankcardPage")
   public String bankcardPage(Model model) {
      HttpSession httpSession = HttpContextUtils.getHttpServletRequest().getSession();
      User user = (User) httpSession.getAttribute("user");
      Userinfo userinfo = (Userinfo) httpSession.getAttribute("userInfo");
      List<Bankcard> bankcardList = bankcardDao.getCardListByUserId(user.getUserId());
      model.addAttribute("user", user);
      model.addAttribute("userInfo", userinfo);
      model.addAttribute("parentMenuNo", "4");
      model.addAttribute("childMenuNo", "4");
      model.addAttribute("Cards", bankcardList);
      model.addAttribute("userName", user.getUserName());
      return "views/front/customer/company/bankcard.jsp";
   }


   @RequestMapping("withdraw")
   public String withdraw(HttpServletRequest request, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      String userId = user.getUserId().toString();
      Map param = this.getConditionParam(request);
      param.put("geTxState", "0");
      param.put("ltTxState", "3");
      param.put("userId", userId);
      List list = withdrawDAO.getWithdrawListByUserId(param);
      ConvertUtil.convertDataBaseMapToJavaMap(list);
      int totalCount = withdrawDAO.total(param);
      Map retInfo = withdrawDAO.getMoneyInfo(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("parentMenuNo", "2");
      model.addAttribute("childMenuNo", "6");
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("txMoney", retInfo.get("txMoney"));
      model.addAttribute("list", list);
      return "views/front/customer/finance/txList.jsp";
   }

   @RequestMapping("withdrawProcess")
   @ResponseBody
   public R withdrawProcess(@RequestParam Map<String, Object> params) {
      User user = (User) HttpContextUtils.getAttribute("user");
      params.put("userId", user.getUserId());
      params.put("withdrawIp", IPUtils.getIpAddr(HttpContextUtils.getHttpServletRequest()));
      try {
         int result = withdrawDAO.withdrawApply(params);
         return R.ok().put("state", 1).put("content", "请求已经受理");
      } catch (Exception e) {
         e.printStackTrace();
         return R.ok().put("state", -1).put("content", "系统错误");
      }
   }

   @RequestMapping({"rechargeRecord"})
   public String rechargeRecord(HttpServletRequest request, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      String userId = user.getUserId().toString();
      Map param = this.getConditionParam(request);
      param.put("userId", userId);
      List list = bankOrderDao.listPoByConditions(param);
      ConvertUtil.convertDataBaseMapToJavaMap(list);
      int totalCount = bankOrderDao.listPoByConditionsCount(param);
      /*Map retInfo = this.bankOrderMapper0.getCzMoneyInfo(param);*/
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("list", list);
      model.addAttribute("parentMenuNo", "2");
      model.addAttribute("childMenuNo", "5");
      model.addAttribute("userName", user.getUserName());
      /*model.addAttribute("zfMoney", retInfo.get("zfMoney"));*/
      return "views/front/customer/finance/czSqList.jsp";
   }

   @RequestMapping("rechargeApply")
   @ResponseBody
   public R rechargeApply(@RequestParam Map<String, Object> params) {
      int res = financeServiceImpl.recharge(params);
      if (res == 1) {
         return R.ok().put("state", 1).put("content", ConstantUtil.MSG_SUCCESS);
      } else {
         return R.error().put("state", 0).put("content", ConstantUtil.MSG_FAILS);
      }
   }

   @RequestMapping("withdrawRecordList")
   public String withdrawRecordList(HttpServletRequest request, Model model) {
      User user = (User) HttpContextUtils.getAttribute("user");
      Map<String, Object> param = this.getConditionParam(request);
      param.put("geTxState", "0");
      param.put("ltTxState", "3");
      param.put("userId", user.getUserId());
      List list = tradeDao.getWithdrawRecordList(param);
      ConvertUtil.convertDataBaseMapToJavaMap(list);
      int totalCount = tradeDao.getWithdrawRecordCount(param);
      Map retInfo = tradeDao.getWithdrawMoneyTotal(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("parentMenuNo", "2");
      model.addAttribute("childMenuNo", "2");
      model.addAttribute("userName", user.getUserName());
      model.addAttribute("withdrawMoney", retInfo.get("withdrawMoney"));
      model.addAttribute("list", list);
      return "views/front/customer/finance/txList.jsp";
   }


   /**
    * 添加银行卡的异步请求
    * @return
    */
   @RequestMapping("addBankcard")
   @ResponseBody
   public R addBankcard(@RequestParam Map<String, Object> map) {
      User user = (User) HttpContextUtils.getAttribute("user");
      long userId = user.getUserId();
      map.put("userId", userId);
      map.put("addTime", new Date());
      try {
         long key = bankcardDao.addCard(map);
         return R.ok().put("content", "添加成功");
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
   }

   /**
    * 添加银行卡的异步请求
    * @return
    */
   @RequestMapping("getCards")
   @ResponseBody
   public R getCards(@RequestParam Map<String, Object> map) {
      User user = (User) HttpContextUtils.getAttribute("user");
      long userId = user.getUserId();
      try {
         List<Bankcard> cards = bankcardDao.getCardListByUserId(userId);
         return R.ok().put("content", "添加成功").put("Cards", cards);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
   }

   /**
    * 添加银行卡的异步请求
    * @return
    */
   @RequestMapping("delBankcard")
   @ResponseBody
   public R delBankcard(@RequestParam Map<String, Object> map) {
      User user = (User) HttpContextUtils.getAttribute("user");
      long userId = user.getUserId();
      try {
         int count = bankcardDao.delCard(Long.parseLong(map.get("bankcardId").toString()));
         return R.ok().put("content", "添加成功").put("count", count);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
   }

   /**
    * 买家取消订单
    * @param request request
    * @return code:0成功/500异常 msg:异常信息
    */
   @RequestMapping("cancelOrder")
   @ResponseBody
   public Object cancelOrder(HttpServletRequest request) {
      JSONObject obj = new JSONObject();
      String orderId = CommonUtil.GetRequestParam(request, "orderId", "0");
      User user = (User) request.getSession().getAttribute("user");
      String ip = IPUtils.getIpAddr(request);
      String sql = "";
      sql = " CALL customerCancelOrder(\'" + user.getUserId() + "\',\'" + orderId + "\',\'" + ip + "\')";
      HashMap queryParam = new HashMap();
      queryParam.put("strsql", sql);
      ArrayList<Map<String, Object>> result = this.commonMapper.simpleSelectReturnList(queryParam);
      if ((int) result.get(0).get("result") == 1) {
         obj.put("content", ConstantUtil.MSG_SUCCESS);
         obj.put("status", 1);
         return obj;
      }
      obj.put("status", -1);
      return obj;
   }

   /**
    * 跳转授信账户页面
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping({"creditAccount"})
   public String creditAccount(HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      long userId = user.getUserId();
      Map<String, Object> info = customerServiceImpl.findCreditAccountPageInfo(userId);
      model.addAttribute("info", info);
      //已用额度
      BigDecimal sumLoans = customerServiceImpl.getLoansSum(userId);
      model.addAttribute("sumLoans", sumLoans);
      //待还款贷款
      Map param = this.getConditionParam(request);
      param.put("userId", userId);

      //loanlist
      List<Map<String, Object>> loans = customerServiceImpl.getLoansIndebt(param);
      int totalCount = customerServiceImpl.getLoansIndebtCount(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("loans", loans);

      model.addAttribute("parentMenuNo", "6");
      model.addAttribute("childMenuNo", "1");
      return "views/front/customer/finance/creditAccount_new.jsp";
   }

   /**
    * 跳转授信申请页面
    * @param request request
    * @param model model
    * @return view
    */
   @RequestMapping("creditApply")
   public String creditApply(HttpServletRequest request, Model model) {
      User user = (User) request.getSession().getAttribute("user");
      Map<String, Object> info = customerServiceImpl.findCreditApplyPageInfo(user.getUserId());
      model.addAttribute("info", info);
      return "views/front/customer/finance/sxsq_new.jsp";
   }

   /**
    * 提交授信额度申请表单提交
    * @param params 授信额度申请表单参数
    * @param request request
    * @return view
    */
   @RequestMapping("applyCreditLimit")
   public String applyCreditLimit(@RequestParam Map<String, Object> params, HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("user");
      params.put("creditMoney", Integer.parseInt((String) params.get("creditMoney")));
      params.put("userId", user.getUserId());
      params.put("ip", IPUtils.getIpAddr(request));
      customerServiceImpl.applyCreditLimit(params);
      return "views/front/customer/finance/applyCreditSuccess.jsp";
   }

   /**
    * 判断用户可否申请授信额度：如果已经提交了授信申请则需等待结果
    * @param request request
    * @return code:0成功/500异常 msg:异常信息
    */
   @RequestMapping("isCreditApplyAvailable")
   @ResponseBody
   public R isCreditApplyAvailable(HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("user");
      boolean flag = customerServiceImpl.isCreditApplyAvailable(user.getUserId());
      if (flag) {
         return R.ok();
      }
      return R.error("当前申请正在审核中");
   }

   /**
    * 跳转还款页面
    * @param loanId wa_loan 主键
    * @return view
    */
   @RequestMapping("goRepayLoan")
   public String goRepayLoan(@RequestParam("loanId") String loanId, Model model) {

      Map<String, Object> loan = financeServiceImpl.getLoanOrderInfo(Long.parseLong(loanId));
      model.addAttribute("loan", loan);

      model.addAttribute("parentMenuNo", "2");
      return "views/front/customer/finance/repayment.jsp";
   }

   /**
    * 还贷款处理方法
    * @param loanId wa_loan主键
    * @param repayMoney 还款总额
    * @param interest 还款利息
    * @param request request
    * @return code:0成功/500异常 msg:异常信息
    */
   @RequestMapping(value = "repayLoan",method = RequestMethod.POST)
   @ResponseBody
   public R repayLoan(@RequestParam(value = "loanId") long loanId, @RequestParam(value = "repayMoney",required = false,defaultValue = "0") BigDecimal repayMoney, @RequestParam(value = "interest",required = false,defaultValue = "0") BigDecimal interest,@RequestParam(value = "overdueFine",required = false,defaultValue = "0")BigDecimal overdueFine ,HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("user");
      long userId = user.getUserId();
      String ip = IPUtils.getIpAddr(request);
      BigDecimal zero=new BigDecimal(0);
      BigDecimal principal=zero;
      if (repayMoney.subtract(overdueFine).subtract(interest).compareTo(zero)>0){
         principal=repayMoney.subtract(overdueFine).subtract(interest);
      }
      try {
         int res = customerServiceImpl.beforeRepayLoanByBalance(userId, loanId, principal, overdueFine,interest, ip);
         if (res == 0) {
            return R.error("账户余额不足");
         }
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
      return R.ok();
   }

   /**
    * 跳转用户授信申请记录页面
    * @param model model
    * @param request request
    * @return view
    */
   @RequestMapping("creditApplys")
   public String creditApplys(Model model, HttpServletRequest request) {
      User user = (User) request.getSession().getAttribute("user");
      Map param = this.getConditionParam(request);
      param.put("userId", user.getUserId());
      List<Map<String, Object>> credits = customerServiceImpl.getCreditList(param);
      int totalCount = customerServiceImpl.getCreditListCount(param);
      this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
      model.addAttribute("credits", credits);
      model.addAttribute("parentMenuNo", "6");
      model.addAttribute("childMenuNo", "2");
      return "views/front/customer/finance/creditApplyRecords.jsp";
   }

   /**
    * 跳转买家授信中心：还款记录
    * @param model model
    * @param request request
    * @return view
    */
   @RequestMapping("loansRepayRecords")
   public String loansRepayRecords(Model model,HttpServletRequest request){
      User user=(User)request.getSession().getAttribute("user");
      Map param = this.getConditionParam(request,2);
      param.put("userId",user.getUserId());
      List<Map<String,Object>> loans=customerServiceImpl.getLoansRepayDetail(param);
      int totalCount=customerServiceImpl.getLoansRepayDetailCount(param);
      this.setPagenationInfo(request,totalCount,Integer.parseInt(param.get("page").toString()),2);
      model.addAttribute("loans",loans);
      model.addAttribute("parentMenuNo", "6");
      model.addAttribute("childMenuNo", "3");
      return "views/front/customer/finance/repayRecords.jsp";
   }

   /**
    * 跳转单条贷款还款记录详细页面
    * @param model model
    * @return view
    */
   @RequestMapping("loanRepayDetailPage")
   public String loanRepayDetailPage(Model model){
      model.addAttribute("parentMenuNo", "6");
      model.addAttribute("childMenuNo", "3");
      return "views/front/customer/finance/loanRepayDetail.jsp";
   }

   /**
    * 跳转账户设置报表管理
    * @param model model
    * @return view
    */
   @RequestMapping("reportManagement")
   public String reportManagement(Model model){
      model.addAttribute("parentMenuNo","4");
      model.addAttribute("childMenuNo","5");
      return "views/front/customer/company/reportFrom.jsp";
   }

   /**
    * 跳转结算页面
    * @param orderId orderId
    * @param model model
    * @return view
    */
   @RequestMapping(value = "goSecondPayOrder",method = RequestMethod.GET)
   public String goSecondPay(@RequestParam(value = "orderId")long orderId,Model model){
      model.addAttribute("parentMenuNo", "2");
      return "views/front/customer/order/secondPayOrder.jsp";
   }

   /**
    * 处理结算
    * @param orderId wa_order主键
    * @param secondPayMoney 结算金额：为负为退款，为正为补款
    * @param zfMethod 支付方式：暂只支持余额支付
    * @param balance 余额支付部分
    * @param loan 授信支付部分
    * @param certificateImg 线下支付文件上传回调url
    * @param request request
    * @return code:0成功/500异常 msg:异常信息
    * @throws Exception
    */
   @RequestMapping(value = "secondPayOrderSub",method = RequestMethod.POST)
   @ResponseBody
   @Transactional
   public R secondPayOrderSub(@RequestParam("orderId") final long orderId, @RequestParam("secondPayMoney") final BigDecimal secondPayMoney,
                              @RequestParam(value = "zfMethod",required = false,defaultValue = "2") final int zfMethod, @RequestParam(value = "balance",required = false,defaultValue = "0") final BigDecimal balance,
                              @RequestParam(value = "loan",required = false,defaultValue = "0") final BigDecimal loan, @RequestParam(value = "certificateImg",required = false,defaultValue = "") final String certificateImg,
                              HttpServletRequest request
                     ) throws Exception {
      BigDecimal zero=new BigDecimal(0);
      User user=(User)request.getSession().getAttribute("user");
      final long userId=user.getUserId();
      BigDecimal lixiRate=user.getLixiRate();
      final String ip=getIpAddr(request);
      //从数据库得到最新的余额和授信余额
      Map<String,Object> databaseUser=userDao.singleUserByPrimaryKey(userId);
      BigDecimal oldUserMoney=(BigDecimal)databaseUser.get("user_money");
      BigDecimal oldUserLockMoney=(BigDecimal)databaseUser.get("user_lock_money");
      BigDecimal oldUserCreditMoney=(BigDecimal)databaseUser.get("user_credit_money");
      BigDecimal oldUserLockCreditMoney=(BigDecimal)databaseUser.get("user_lock_credit_money");

      UserSubAccount userSubAccount=financeServiceImpl.getUserSubAccountByUserId(user.getUserId());
      final String subAccNo=userSubAccount.getSubAccNo();
      final String subAccNm=userSubAccount.getSubAccNm();
      final Map<String,Object> param=new HashMap<>();
      final AdminSubAccount orderTransfer=adminSubAccountServiceImpl.findOrderTransferAccount();
      final AdminSubAccount loanTransfer=adminSubAccountServiceImpl.findLoanTransferAccount();

      //得到order_trans记录
      param.put("orderId",orderId);
      param.put("transState",1);
      final Map<String,Object> orderTrans=orderTransDao.singlePoByConditions(param);
      long orderTransId=(long)orderTrans.get("order_trans_id");
      long moneyId=(long)orderTrans.get("money_id");
      BigDecimal zfMoney=(BigDecimal)orderTrans.get("zf_money");
      BigDecimal zfSjMoney=zfMoney.add(secondPayMoney);
      //多退
      if (secondPayMoney.compareTo(zero)<0){

         int firstZfmethod=(int)orderTrans.get("zf_method");
         if (firstZfmethod==2){
            param.clear();
            param.put("userId",userId);
            param.put("userMoney",oldUserMoney.subtract(secondPayMoney));
            param.put("userLockMoney",oldUserLockMoney.add(secondPayMoney));
            userDao.updateUserByUserId(param);

            new Thread(new Runnable() {
               @Override
               public void run() {
                  Map<String,String> paramss=new HashMap<>();
                  paramss.put("payAccNo",orderTransfer.getSubAccNo());
                  paramss.put("recvAccNo",subAccNo);
                  paramss.put("recvAccNm",subAccNm);
                  paramss.put("tranAmt",secondPayMoney.abs().toString());
                  JSONObject memo=new JSONObject();
                  memo.put("orderId",orderId);
                  memo.put("type",5);
                  memo.put("content","订单多退退回余额");
                  paramss.put("memo",memo.toString());
                  String result= null;
                  try {
                     result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  R r= JSON.parseObject(result,R.class);
                  ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
                  CncbTrans cncbTrans=new CncbTrans();
                  cncbTrans.setXml(forceTransferBasicInfo.getXml());
                  cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                  cncbTrans.setTime(new Date());
                  cncbTrans.setType((byte)5);
                  JSONObject operationParamsObj=new JSONObject();
                  operationParamsObj.put("orderId",orderId);
                  operationParamsObj.put("secondPayMoney",secondPayMoney);
                  operationParamsObj.put("zfMethod",zfMethod);
                  operationParamsObj.put("balance",balance);
                  operationParamsObj.put("loan",loan);
                  operationParamsObj.put("certificateImg",certificateImg);
                  cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                  cncbTransDao.create(cncbTrans);
               }
            }).start();
            param.clear();
            param.put("orderId",orderId);
            param.put("prod2ndpayState",3);
            orderDao.updateOrderByID(param);
            return R.ok().put("msg","结算中...");
         }else if(firstZfmethod==3){
            new Thread(new Runnable() {
               @Override
               public void run() {
                  Map<String,String> paramss=new HashMap<>();
                  paramss.put("payAccNo",orderTransfer.getSubAccNo());
                  paramss.put("recvAccNo",loanTransfer.getSubAccNo());
                  paramss.put("recvAccNm",loanTransfer.getSubAccNm());
                  paramss.put("tranAmt",secondPayMoney.abs().toString());
                  JSONObject memo=new JSONObject();
                  memo.put("orderId",orderId);
                  memo.put("type",8);
                  memo.put("content","订单多退退至贷款");
                  paramss.put("memo",memo.toString());
                  String result= null;
                  try {
                     result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  R r= JSON.parseObject(result,R.class);
                  ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
                  CncbTrans cncbTrans=new CncbTrans();
                  cncbTrans.setXml(forceTransferBasicInfo.getXml());
                  cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                  cncbTrans.setTime(new Date());
                  cncbTrans.setType((byte)8);
                  JSONObject operationParamsObj=new JSONObject();
                  operationParamsObj.put("orderId",orderId);
                  operationParamsObj.put("secondPayMoney",secondPayMoney);
                  operationParamsObj.put("orderTransId",orderTrans.get("order_trans_id"));
                  cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                  cncbTransDao.create(cncbTrans);
               }
            }).start();

            param.clear();
            param.put("orderId",orderId);
            param.put("prod2ndpayState",3);
            orderDao.updateOrderByID(param);
            return R.ok().put("msg","结算中...");
         }else if(firstZfmethod==4){



            BigDecimal balanceZfMoney1st=(BigDecimal) orderTrans.get("balance_zf_money");
            BigDecimal loanZfMoney1st=(BigDecimal)orderTrans.get("loan_zf_money");
            //需少退的余额和贷款
            final BigDecimal refundBalance=secondPayMoney.multiply(balanceZfMoney1st).divide(balanceZfMoney1st.add(loanZfMoney1st),1,BigDecimal.ROUND_HALF_DOWN);
            final BigDecimal refundLoan=secondPayMoney.multiply(loanZfMoney1st).divide(balanceZfMoney1st.add(loanZfMoney1st),1,BigDecimal.ROUND_HALF_DOWN);

            param.clear();
            param.put("userId",userId);
            param.put("userMoney",oldUserMoney.subtract(refundBalance));
            param.put("userLockMoney",oldUserLockMoney.add(refundBalance));
            userDao.updateUserByUserId(param);
            new Thread(new Runnable() {
               @Override
               public void run() {
                  Map<String,String> paramss=new HashMap<>();
                  paramss.put("payAccNo",orderTransfer.getSubAccNo());
                  paramss.put("recvAccNo",loanTransfer.getSubAccNo());
                  paramss.put("recvAccNm",loanTransfer.getSubAccNm());
                  paramss.put("tranAmt",refundLoan.abs().toString());
                  JSONObject memo=new JSONObject();
                  memo.put("orderId",orderId);
                  memo.put("type",9);
                  memo.put("content","订单多退退至贷款");
                  paramss.put("memo",memo.toString());
                  String result= null;
                  try {
                     result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  R r= JSON.parseObject(result,R.class);
                  ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
                  CncbTrans cncbTrans=new CncbTrans();
                  cncbTrans.setXml(forceTransferBasicInfo.getXml());
                  cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                  cncbTrans.setTime(new Date());
                  cncbTrans.setType((byte)9);
                  JSONObject operationParamsObj=new JSONObject();
                  operationParamsObj.put("orderId",orderId);
                  operationParamsObj.put("secondPayMoney",secondPayMoney);
                  operationParamsObj.put("orderTransId",orderTrans.get("order_trans_id"));
                  operationParamsObj.put("refundBalance",refundBalance);
                  operationParamsObj.put("refundLoan",refundLoan);
                  operationParamsObj.put("userId",userId);
                  cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                  cncbTransDao.create(cncbTrans);
               }
            }).start();

            param.clear();
            param.put("orderId",orderId);
            param.put("prod2ndpayState",3);
            orderDao.updateOrderByID(param);
            return R.ok().put("msg","结算中...");
         }

      }else if (secondPayMoney.compareTo(zero)==0){
         customerServiceImpl.handle2ndPayProd(orderId,secondPayMoney,zfMethod,balance,loan,certificateImg);
         return R.ok().put("msg","结算中...");
      }else if(secondPayMoney.compareTo(zero)>0){
         if(zfMethod==2){
            param.clear();
            param.put("userId",userId);
            param.put("userMoney",oldUserMoney.subtract(secondPayMoney));
            param.put("userLockMoney",oldUserLockMoney.add(secondPayMoney));
            userDao.updateUserByUserId(param);
            new Thread(new Runnable() {
               @Override
               public void run() {
                  Map<String,String> paramss=new HashMap<>();
                  paramss.put("payAccNo",subAccNo);
                  paramss.put("recvAccNo",orderTransfer.getSubAccNo());
                  paramss.put("recvAccNm",orderTransfer.getSubAccNm());
                  paramss.put("tranAmt",secondPayMoney.toString());
                  JSONObject memo=new JSONObject();
                  memo.put("orderId",orderId);
                  memo.put("type",5);
                  memo.put("content","订单少补余额支付");
                  paramss.put("memo",memo.toString());
                  String result= null;
                  try {
                     result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  R r= JSON.parseObject(result,R.class);
                  ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
                  CncbTrans cncbTrans=new CncbTrans();
                  cncbTrans.setXml(forceTransferBasicInfo.getXml());
                  cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                  cncbTrans.setTime(new Date());
                  cncbTrans.setType((byte)5);
                  JSONObject operationParamsObj=new JSONObject();
                  operationParamsObj.put("orderId",orderId);
                  operationParamsObj.put("secondPayMoney",secondPayMoney);
                  operationParamsObj.put("zfMethod",zfMethod);
                  operationParamsObj.put("balance",balance);
                  operationParamsObj.put("loan",loan);
                  operationParamsObj.put("certificateImg",certificateImg);
                  cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                  cncbTransDao.create(cncbTrans);
               }
            }).start();

            param.put("orderId",orderId);
            param.put("prod2ndpayState",3);
            orderDao.updateOrderByID(param);
            return R.ok().put("msg","结算中...");
         }else if (zfMethod==3){
            //update wa_user 授信余额
            param.clear();
            param.put("userId",userId);
            param.put("userCreditMoney",oldUserCreditMoney.subtract(loan));
            param.put("userLockCreditMoney",oldUserLockCreditMoney.add(loan));
            userDao.updateUserByUserId(param);
            //insert wa_loan
            param.clear();
            Loan newLoan=new Loan();
            newLoan.setMoneyId(moneyId);
            newLoan.setUserId(userId);
            newLoan.setLoanMoney(loan);
            newLoan.setApplyDate(new Date());
            newLoan.setLixiRate(lixiRate);
            newLoan.setLoanState((byte)0);
            newLoan.setLoanIp(getIpAddr(request));
            newLoan.setLoanType((byte)3);
            loanDao.create(newLoan);
            long loanId=newLoan.getLoanId();

            //将少补付款记录保存在wa_order_trans表中，并将trans_state=2 ：进行中
            param.clear();
            param.put("orderTransId",orderTransId);
            param.put("zfMethod2",3);
            param.put("balanceZfMoney2",new BigDecimal(0));
            param.put("loanZfMoney2",loan);
            param.put("loanId2",loanId);
            param.put("zfSjMoney",zfSjMoney);
            param.put("transState",2);
            orderTransDao.update(param);

            //wa_order update（prod2ndpayState-1-授信支付未到账）
            param.clear();
            param.put("orderId",orderId);
            param.put("prod2ndpayState",1);
            orderDao.updateOrderByID(param);
            return R.ok().put("msg","结算中...");

         }else if (zfMethod==4){
            //update wa_user 余额/授信余额
            param.clear();
            param.put("userId",userId);
            param.put("userCreditMoney",oldUserCreditMoney.subtract(loan));
            param.put("userLockCreditMoney",oldUserLockCreditMoney.add(loan));
            param.put("userMoney",oldUserMoney.subtract(balance));
            param.put("userLockMoney",oldUserLockMoney.add(balance));
            userDao.updateUserByUserId(param);

            //余额支付部分转入中转户
            new Thread(new Runnable() {
               @Override
               public void run() {
                  Map<String,String> paramss=new HashMap<>();
                  paramss.put("payAccNo",subAccNo);
                  paramss.put("recvAccNo",orderTransfer.getSubAccNo());
                  paramss.put("recvAccNm",orderTransfer.getSubAccNm());
                  paramss.put("tranAmt",balance.toString());
                  JSONObject memo=new JSONObject();
                  memo.put("orderId",orderId);
                  memo.put("type",11);
                  memo.put("content","订单少补余额支付");
                  paramss.put("memo",memo.toString());
                  String result= null;
                  try {
                     result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
                  } catch (Exception e) {
                     e.printStackTrace();
                  }
                  R r= JSON.parseObject(result,R.class);
                  ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
                  CncbTrans cncbTrans=new CncbTrans();
                  cncbTrans.setXml(forceTransferBasicInfo.getXml());
                  cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
                  cncbTrans.setTime(new Date());
                  cncbTrans.setType((byte)11);
                  JSONObject operationParamsObj=new JSONObject();
                  operationParamsObj.put("orderId",orderId);
                  operationParamsObj.put("secondPayMoney",secondPayMoney);
                  operationParamsObj.put("orderTransId",orderTrans.get("order_trans_id"));
                  operationParamsObj.put("zfMethod",zfMethod);
                  operationParamsObj.put("balance",balance);
                  operationParamsObj.put("loan",loan);
                  operationParamsObj.put("userId",userId);
                  operationParamsObj.put("ip",ip);
                  cncbTrans.setOperationParams(operationParamsObj.toJSONString());
                  cncbTransDao.create(cncbTrans);

               }
            }).start();

            param.clear();
            param.put("orderId",orderId);
            param.put("prod2ndpayState",3);
            orderDao.updateOrderByID(param);
            return R.ok().put("msg","结算中...");

         }
      }
      return R.ok();
   }

   /**
    * 物流订单结算处理
    * @param logisticsId wa_logistics_info 主键
    * @return code:0成功/500异常 msg:异常信息
    * @throws Exception
    */
   @RequestMapping(value = "secondPayLogisticsSub",method = RequestMethod.POST)
   @ResponseBody
   @Transactional
   public R secondPayLogisticsSub(@RequestParam("logisticsId") final long logisticsId) throws Exception {
      Map<String,Object> logisticsInfo=logisticsInfoDao.singleLogisticsInfoByPrimaryKey(logisticsId);
      final BigDecimal prePayment=(BigDecimal)logisticsInfo.get("pre_payment");
      final long senderUserId=(long)logisticsInfo.get("sender_user_id");
      final long orderId=(long)logisticsInfo.get("order_id");
      final AdminSubAccount orderTransfer=adminSubAccountServiceImpl.findOrderTransferAccount();
      new Thread(new Runnable() {
         @Override
         public void run() {
            UserSubAccount senderSubAccount=financeServiceImpl.getUserSubAccountByUserId(senderUserId);
            String subAccNo=senderSubAccount.getSubAccNo();
            String subAccNm=senderSubAccount.getSubAccNm();
            Map<String,String> paramss=new HashMap<>();
            paramss.put("payAccNo",orderTransfer.getSubAccNo());
            paramss.put("recvAccNo",subAccNo);
            paramss.put("recvAccNm",subAccNm);
            paramss.put("tranAmt",prePayment.toString());
            JSONObject memo=new JSONObject();
            memo.put("orderId",orderId);
            memo.put("logisticsInfoId",logisticsId);
            memo.put("type",7);
            memo.put("content","物流订单结算");
            paramss.put("memo",memo.toString());
            String result= null;
            try {
               result = CommonUtil.connectCNCBLocalServer(ConstantUtil.CNCB_SERVER_BASEURL+"forceTransfer",paramss);
            } catch (Exception e) {
               e.printStackTrace();
            }
            R r= JSON.parseObject(result,R.class);
            ForceTransferBasicInfo forceTransferBasicInfo=JSON.parseObject(r.get("forceTransferBasicInfo").toString(),ForceTransferBasicInfo.class);
            CncbTrans cncbTrans=new CncbTrans();
            cncbTrans.setXml(forceTransferBasicInfo.getXml());
            cncbTrans.setClientId(forceTransferBasicInfo.getClientID());
            cncbTrans.setTime(new Date());
            cncbTrans.setType((byte)7);
            JSONObject operationParamsObj=new JSONObject();
            operationParamsObj.put("logisticsId",logisticsId);
            operationParamsObj.put("senderUserId",senderUserId);
            operationParamsObj.put("orderId",orderId);
            operationParamsObj.put("zfSjMoney",prePayment);
            cncbTrans.setOperationParams(operationParamsObj.toJSONString());
            cncbTransDao.create(cncbTrans);
         }
      }).start();
      Map<String,Object> params=new HashMap<>();
      params.put("orderId",orderId);
      params.put("logistics2ndpayState",6);
      orderDao.updateOrderByID(params);
      return R.ok().put("mag","结算中...");
   }

   /**
    * 跳转可开发票申请页面
    * @param model model
    * @return view
    */
   @RequestMapping(value = "goBillApply",method = RequestMethod.GET)
   public String goBillApply(Model model){
      model.addAttribute("parentMenuNo",7);
      model.addAttribute("childMenuNo",1);
      return "views/front/customer/bill/goBillApply.jsp";
   }

   /**
    * 跳转发票管理页面
    * @param model model
    * @return view
    */
   @RequestMapping(value = "goOrderBills",method = RequestMethod.GET)
   public String goBillManage(Model model){
      model.addAttribute("parentMenuNo",7);
      model.addAttribute("childMenuNo",2);
      return "views/front/customer/bill/goOrderBills.jsp";
   }

   /**
    * 可开发票订单列表
    * @param params 分页参数
    * @param request request
    * @return 可开发票订单列表
    */
   @RequestMapping(value = "billAvaliableOrderList",method = RequestMethod.GET)
   @ResponseBody
   public R billAvaliableOrderList(@RequestParam Map params,HttpServletRequest request){
      User user=(User)request.getSession().getAttribute("user");
      long userId=user.getUserId();
      params.put("userId",userId);
      Query query = new Query(params);
      List<Map<String,Object>> list=customerServiceImpl.billAvaliableOrderList(query);
      int totalCount=customerServiceImpl.billAvaliableOrderListCount(query);
      PageUtils pageUtils=new PageUtils(list, totalCount, query.getLimit(), query.getPage());
      return R.ok().put("page",pageUtils);
   }

   /**
    * 可开发票物流订单列表
    * @param params 分页参数
    * @param request request
    * @return 可开发票物流订单列表
    */
   @RequestMapping(value ="billAvaliableLogisticsList",method = RequestMethod.GET)
   @ResponseBody
   public R billAvaliableLogisticsList(@RequestParam Map params,HttpServletRequest request){
      User user=(User)request.getSession().getAttribute("user");
      long userId=user.getUserId();
      params.put("customerUserId",userId);
      Query query = new Query(params);
      List<Map<String,Object>> list=customerServiceImpl.billAvaliableLogisticsList(query);
      int totalCount=customerServiceImpl.billAvaliableLogisticsListCount(query);
      PageUtils pageUtils=new PageUtils(list, totalCount, query.getLimit(), query.getPage());
      return R.ok().put("page",pageUtils);
   }

   /**
    * 跳转订单发票申请
    * @param model model
    * @return view
    */
   @RequestMapping(value = "goOrderBillRequest",method = RequestMethod.GET)
   public String goBillRequest(Model model){
      model.addAttribute("parentMenuNo",7);
      model.addAttribute("childMenuNo",1);
      return "views/front/customer/bill/goOrderBillRequest.jsp";
   }

   /**
    * 跳转物流订单发票申请
    * @param model model
    * @return view
    */
   @RequestMapping(value = "goLogisticsBillRequest",method = RequestMethod.GET)
   public String goLogisticsRequest(Model model){
      model.addAttribute("parentMenuNo",7);
      model.addAttribute("childMenuNo",1);
      return "views/front/customer/bill/goLogisticsBillRequest.jsp";
   }

   /**
    * 申请发票的订单信息
    * @param ids 订单id拼接的字符串
    * @return 申请发票的订单信息
    */
   @RequestMapping(value = "billOrders",method = RequestMethod.GET)
   @ResponseBody
   public R billOrders(@RequestParam("ids") String ids){
      List<Map<String,Object>> orders=customerServiceImpl.billOrders(ids);
      return R.ok().put("items",orders);
   }

   /**
    * 申请发票的订单信息
    * @param ids 物流订单id拼接的字符串
    * @return 申请发票的订单信息
    */
   @RequestMapping(value = "billLogisticss",method = RequestMethod.GET)
   @ResponseBody
   public R billLogisticss(@RequestParam("ids")String ids){
      List<Map<String,Object>> logisticss=customerServiceImpl.billLogisticss(ids);
      return R.ok().put("items",logisticss);
   }

   /**
    * 请求开具发票
    * @param bill 接收vo数据
    * @param request request
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping(value = "applyBill",method = RequestMethod.POST)
   @ResponseBody
   public R applyBill(@RequestBody Bill bill,HttpServletRequest request){
      User user=(User)request.getSession().getAttribute("user");
      long userId=user.getUserId();
      bill.setCustomerUserId(userId);
      /*bill.setCustomerUserId(92L);*/
      try {
         customerServiceImpl.applyBill(bill);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
      return R.ok();
   }

   /**
    * 已申请的商品订单发票列表数据
    * @param params 分页参数
    * @param request request
    * @return 已申请的商品订单发票列表数据
    */
   @RequestMapping(value = "applyOrderBillsList",method = RequestMethod.GET)
   @ResponseBody
   public R applyOrderBillsList(@RequestParam Map params, HttpServletRequest request){
      User user=(User)request.getSession().getAttribute("user");
      long userId=user.getUserId();
      params.put("customerUserId",userId);
      Query query=new Query(params);
      List<Map<String,Object>> list=customerServiceImpl.applyOrderBillsList(query);
      int totalCount=customerServiceImpl.applyOrderBillsListCount(query);
      PageUtils page=new PageUtils(list,totalCount,query.getLimit(),query.getPage());
      return R.ok().put("page",page);
   }

   /**
    * 跳转收发票界面
    * @param model model
    * @return view
    */
   @RequestMapping(value = "goReceiveBill",method = RequestMethod.GET)
   public String goReceiveBill(Model model){
      model.addAttribute("parentMenuNo",7);
      model.addAttribute("childMenuNo",2);
      return "views/front/customer/bill/goReceiveBill.jsp";
   }

   /**
    * 处理买家收到发票
    * @param billId 发票主键
    * @param flag true：确认收到
    * @return 成功：code：0；失败：code：500，msg：原因
    */
   @RequestMapping(value = "receiveBill",method = RequestMethod.POST)
   @ResponseBody
   public R confirmReceiveBill(@RequestParam("billId")long billId,@RequestParam(value = "flag",required = false,defaultValue = "true")boolean flag){
      try {
         customerServiceImpl.receiveBill(billId,flag);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
      return R.ok();
   }

   /**
    * 跳转发票地址管理界面
    * @param model model
    * @return view
    */
   @RequestMapping(value = "billAddressManage",method = RequestMethod.GET)
   public String billAddressManage(Model model){
      model.addAttribute("parentMenuNo",7);
      model.addAttribute("childMenuNo",4);
      return "views/front/customer/bill/billAddressManage.jsp";
   }

   /**
    * 保存或更新用户发票地址
    * @param billAddress wa_bill_address pojo
    * @return code:0成功/500异常 msg:异常信息
    */
   @RequestMapping(value = "saveOrUpdateBillAddress",method = RequestMethod.POST)
   @ResponseBody
   public R saveOrUpdateBillAddress(@RequestBody BillAddress billAddress){
      User user=((User)HttpContextUtils.getAttribute("user"));
      long userId=user.getUserId();
      billAddress.setUserId(userId);
      int res=0;
      try {
         res=customerServiceImpl.saveOrUpdateBillAddress(billAddress);
      } catch (Exception e) {
         e.printStackTrace();
         return R.error();
      }
      if (res==1){
         return R.ok().put("type",1).put("msg","保存成功");
      }else if (res==2){
         return R.ok().put("type",2).put("msg","更新成功");
      }
      return R.error();
   }

   /**
    * 查询用户发票地址
    * @return  code:0成功/500异常 msg:异常信息
    */
   @RequestMapping(value = "billAddress",method = RequestMethod.GET)
   @ResponseBody
   public R billAddress(){
      User user=(User) HttpContextUtils.getAttribute("user");
      long userId=user.getUserId();
      BillAddress billAddress=customerServiceImpl.findBillAddress(userId);
      if(null==billAddress){
         return R.error(-1,"无用户发票地址数据");
      }
      return R.ok().put("billAddress",billAddress);
   }

}
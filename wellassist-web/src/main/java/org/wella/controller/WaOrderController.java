package org.wella.controller;

import io.wellassist.utils.PageUtils;
import io.wellassist.utils.Query;
import io.wellassist.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.wella.common.utils.ConvertUtil;
import org.wella.dao.LoanDao;
import org.wella.dao.OrderDao;
import org.wella.platform.service.impl.MemberServiceImpl;
import org.wella.service.SellerService;
import org.wella.service.WaOrderService;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/2.
 */
@Controller
@RequestMapping(value = "/order/")
public class WaOrderController {

    @Autowired
    private WaOrderService waOrderServiceImpl;

    @RequestMapping("isZordersQuestion")
    @ResponseBody
    public R isZordersQuestion(@RequestParam("orderId")long orderId){
        boolean flag=waOrderServiceImpl.idZordersQuestion(orderId);
        return R.ok().put("flag",flag);
    }
}
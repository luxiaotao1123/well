package org.wella.service.impl;

import io.wellassist.utils.HttpContextUtils;
import io.wellassist.utils.IPUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wella.dao.TradeDAO;
import org.wella.entity.User;
import org.wella.service.FinanceService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by liuwen on 2017/6/5.
 */
@Service("financeServiceImpl")
public class FinanceServiceImpl implements FinanceService {
    @Autowired
    private TradeDAO tradeDAO;

    @Override
    public int recharge(Map<String, Object> map) {
        User user = (User) HttpContextUtils.getAttribute("user");
        map.put("userId", user.getUserId());
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        map.put("rechargeIp", IPUtils.getIpAddr(request));
        int result = tradeDAO.rechargeApply(map);
        return result;
    }

    @Override
    public int withdraw(Map<String, Object> map) {
        return 0;
    }


}
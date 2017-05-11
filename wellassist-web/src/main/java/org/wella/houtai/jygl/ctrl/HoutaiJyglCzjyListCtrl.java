package org.wella.houtai.jygl.ctrl;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wella.common.ctrl.BaseController;
import org.wella.common.utils.CommonUtil;
import org.wella.common.utils.ConstantUtil;
import org.wella.common.utils.ConvertUtil;
import org.wella.common.vo.MyInfo;
import org.wella.houtai.jygl.mapper.HoutaiJyglCzjyMapper;

@Controller
public class HoutaiJyglCzjyListCtrl extends BaseController {
    @Autowired
    private HoutaiJyglCzjyMapper jyglCzjyMapper;

    public HoutaiJyglCzjyListCtrl() {
    }

    @RequestMapping({"houtai/jygl/HoutaiJyglCzjyListCtrl-getCzjyList"})
    public String getCzjyList(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map param = this.getConditionParam(request);
        param.put("jyType", "0");
        ArrayList list = this.jyglCzjyMapper.getJyglCzjyList(param);
        ConvertUtil.convertDataBaseMapToJavaMap(list);
        int totalCount = this.jyglCzjyMapper.getJyglCzjyListCount(param);
        model.addAttribute("list", list);
        model.addAttribute("start", param.get("start"));
        this.setPagenationInfo(request, totalCount, Integer.parseInt(param.get("page").toString()));
        return "views/houtai/jygl/czjyList.jsp";
    }

    @RequestMapping({"houtai/jygl/HoutaiJyglCzjyListCtrl-czQd"})
    public void czQd(HttpServletRequest request, HttpServletResponse response, Model model) {
        JSONObject res = new JSONObject();
        String orderId = CommonUtil.GetRequestParam(request, "orderId", "0");
        String orderIp = this.getIpAddr(request);
        MyInfo myInfo = this.getMyInfo(request);
        String userId = myInfo.getUserId();
        String sql = "";

        try {
            sql = " CALL czClProcess(\'" + orderId + "\',\'" + orderIp + "\',\'" + userId + "\')";
            HashMap e = new HashMap();
            e.put("strsql", sql);
            this.commonMapper.simpleUpdate(e);
            res.put("state", "1");
            res.put("content", ConstantUtil.MSG_SUCCESS);
        } catch (Exception var11) {
            var11.printStackTrace();
            res.put("state", "2");
            res.put("content", ConstantUtil.MSG_FAILS);
        }

        this.echo(response, res);
    }
}
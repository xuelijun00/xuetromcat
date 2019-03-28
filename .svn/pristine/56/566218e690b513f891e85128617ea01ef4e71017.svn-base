package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.service.trackmore.*;
import com.yks.bigdata.web.vo.FilterDto;
import com.yks.bigdata.web.vo.GridModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

@Controller
@RequestMapping("/LogisticsDetails")
public class LogisticsDetailsController {

    @Autowired
    IErpOrdersService  erpOrdersService;

    @Autowired
    ITrackInfoDetailService trackInfoDetailService;

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public GridModel get(FilterDto filterDto,String erpOrdersId1,String ordersMailCode1){
        ErpOrders erpOrder = new ErpOrders();
        if (erpOrdersId1 != null && erpOrdersId1.length() > 0) {
            String[] arr = erpOrdersId1.split("\n");
            erpOrder.setErpOrdersId1("'" + StringUtils.join(arr, "','") + "'");
        }
        if (ordersMailCode1 != null && ordersMailCode1.length() > 0) {
            String[] arr = ordersMailCode1.split("\n");
            erpOrder.setOrdersMailCode("'" + StringUtils.join(arr, "','") + "'");
        }

        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<ErpOrders> phy = erpOrdersService.selectErpOrdersDetails(erpOrder);
        GridModel gridModel = new GridModel(new PageInfo<>(phy));
        gridModel.setPagesize(filterDto.getRows());
        return gridModel;
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String view(HttpServletRequest request,String erpOrdersId1,String ordersMailCode1){
        ErpOrders erpOrder = new ErpOrders();
        if (erpOrdersId1 != null && erpOrdersId1.length() > 0) {
            String[] arr = erpOrdersId1.split("\r\n");
            erpOrder.setErpOrdersId1("'" + StringUtils.join(arr, "','") + "'");
        }
        if (ordersMailCode1 != null && ordersMailCode1.length() > 0) {
            String[] arr = ordersMailCode1.split("\r\n");
            erpOrder.setOrdersMailCode("'" + StringUtils.join(arr, "','") + "'");
        }

        PageHelper.startPage(1, 10, true);
        List<ErpOrders> phy = erpOrdersService.selectErpOrdersDetails(erpOrder);
        request.setAttribute("table",new GridModel(new PageInfo<>(phy)));
        erpOrder.setErpOrdersId1(erpOrdersId1);
        erpOrder.setOrdersMailCode(ordersMailCode1);
        request.setAttribute("channelParam",erpOrder);
        return "trackmore/logistics_detail";
    }


    @RequestMapping(value = "/detailedview", method = RequestMethod.GET)
    public String updateView(String ordersMailCode, HttpServletRequest request) {
        request.setAttribute("detailedInfo",  trackInfoDetailService.selectStatusInformation(ordersMailCode));
        return "trackmore/logistics_detailed_popup";
    }
    
    
}

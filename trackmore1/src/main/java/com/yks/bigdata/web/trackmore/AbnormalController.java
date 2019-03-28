package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.Abnormal;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.Physical;
import com.yks.bigdata.service.trackmore.IAbnormalService;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.ILogisticsReportService;
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
@RequestMapping("/Abnormal")
public class AbnormalController {

    @Autowired
    IAbnormalService abnormalService;

    @Autowired
    IErpOrdersService  erpOrdersService;
    

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public GridModel update(ErpOrders erpOrder, FilterDto filterDto, HttpServletRequest request){
		String[] platforms = request.getParameterValues("platforms[]");
		String[] country = request.getParameterValues("country[]");
		String[] channelNames = request.getParameterValues("channelNames[]");

		if (platforms != null && platforms.length > 0) {
			erpOrder.setPlatform("'" + StringUtils.join(platforms, "','") + "'");
		}
		if (country != null && country.length > 0) {
			erpOrder.setBuyerCountry("'" + StringUtils.join(country, "','") + "'");
		}
		if (channelNames != null && channelNames.length > 0) {
			erpOrder.setChannelName("'" + StringUtils.join(channelNames, "','") + "'");
		}
    	
        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<ErpOrders> phy = erpOrdersService.selectExceptionErpOrders(erpOrder);
        GridModel gridModel = new GridModel(new PageInfo<>(phy));
        gridModel.setPagesize(filterDto.getRows());
        return gridModel;
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String view(ErpOrders erpOrder, HttpServletRequest request, String[] platforms, String[] country,String[] channelNames){
		if (platforms != null && platforms.length > 0) {
			erpOrder.setPlatform("'" + StringUtils.join(platforms, ",") + "'");
		}
		if (country != null && country.length > 0) {
			erpOrder.setBuyerCountry("'" + StringUtils.join(country, "','") + "'");
		}
		if (channelNames != null && channelNames.length > 0) {
			erpOrder.setChannelName("'" + StringUtils.join(channelNames, ",") + "'");
		}
    	
        PageHelper.startPage(1, 10, true);
        List<ErpOrders> phy = erpOrdersService.selectExceptionErpOrders(erpOrder);
        request.setAttribute("table",new GridModel(new PageInfo<>(phy)));
        request.setAttribute("erporderParam",erpOrder);
        request.setAttribute("platform", erpOrdersService.selectplatform());
		request.setAttribute("country", erpOrdersService.selectbuyerCountry());
		request.setAttribute("channelNames", erpOrdersService.selectchannelName());
		request.setAttribute("platformParam", platforms);
		request.setAttribute("countryParam", country);
		request.setAttribute("channelNameParam", channelNames);
        
        return "trackmore/abnormal";
    }


}

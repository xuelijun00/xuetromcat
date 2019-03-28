package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import com.yks.bigdata.dto.trackmore.RequestTask;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.util.CsvUtils;
import com.yks.bigdata.web.vo.ChannelExcelTemp;
import com.yks.bigdata.web.vo.FilterDto;
import com.yks.bigdata.web.vo.GridModel;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

@Controller
@RequestMapping("/ErpOrders")
public class ErpOrdersController {

    @Autowired
    IErpOrdersService  erpOrdersService;

    @Autowired
    IRequestTaskService requestTaskService;


    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public GridModel getData(ErpOrders channel, FilterDto filterDto, HttpServletRequest request){
        String[] country = request.getParameterValues("country[]");
        if(country != null && country.length > 0){
            channel.setBuyerCountry("'" + StringUtils.join(country,"','") + "'");
        }
        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<ErpOrders> logistics_channels = erpOrdersService.selectErpOrders(channel);
        GridModel gridModel = new GridModel(new PageInfo<>(logistics_channels));
        gridModel.setPagesize(filterDto.getRows());
        return gridModel;
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String view(ErpOrders channel,String[] country, HttpServletRequest request){
        if(country != null && country.length > 0){
            channel.setBuyerCountry("'" + StringUtils.join(country,"','") + "'");
        }
        PageHelper.startPage(1, 10, true);
        List<ErpOrders> logistics_channels = erpOrdersService.selectErpOrders(channel);
        request.setAttribute("table",new GridModel(new PageInfo<>(logistics_channels)));
        request.setAttribute("country",erpOrdersService.selectbuyerCountry());
        request.setAttribute("erporderParam",channel);
        request.setAttribute("countryParam",country);
        return "trackmore/order";
    }

    @RequestMapping(value = "/RequestTask/view",method = RequestMethod.GET)
    public String requestTaskview(String erpOrdersIds, HttpServletRequest request){
        if(StringUtils.isEmpty(erpOrdersIds)){
            request.setAttribute("erpOrdersIds","订单号不能为空！");
            return "trackmore/request_task_report";
        }
        StringBuffer stringBuffer = new StringBuffer();
        String[] split = erpOrdersIds.split("\r\n");
        for (int i=0;i<split.length;i++){
            stringBuffer.append(split[i]).append(",");
        }
        try{
            List<RequestTask> tasks = requestTaskService.selectByErpOrderIdBatch(stringBuffer.deleteCharAt(stringBuffer.length() - 1 ).toString());
            request.setAttribute("erpOrdersIds",erpOrdersIds);
            request.setAttribute("tasks",tasks);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "trackmore/request_task_report";
    }


}
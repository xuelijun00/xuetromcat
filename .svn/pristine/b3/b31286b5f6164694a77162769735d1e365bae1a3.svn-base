package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.LogisticsPickup;
import com.yks.bigdata.service.trackmore.ILogisticsPickupService;
import com.yks.bigdata.service.trackmore.IRequestTaskService;
import com.yks.bigdata.web.vo.FilterDto;
import com.yks.bigdata.web.vo.GridModel;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/7/4.
 * 物流取件 */
@Controller
@RequestMapping("/pickup")
public class LogisticPickupController {

    private static Logger log = LoggerFactory.getLogger(LogisticPickupController.class);

    @Autowired
    ILogisticsPickupService pickupService;

    @Autowired
    IRequestTaskService taskService;

    @ResponseBody
    @RequestMapping(value = "/aggration",method = RequestMethod.GET)
    public MessageVo aggration(){
        taskService.aggregation();
        return new MessageVo(200,"聚合成功!");
    }

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public GridModel update(LogisticsPickup pickup, FilterDto filterDto){
        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<LogisticsPickup> pickupList = pickupService.selectLogisticsPickup(pickup);
        return new GridModel(new PageInfo<>(pickupList));
    }

    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public MessageVo upload(MultipartFile file){
        long d1 = System.currentTimeMillis();
        log.info("上传文件开始----");
        try{
            String lines = IOUtils.toString(file.getInputStream(), "GBK");
            if(lines == null || lines.trim().length() <= 0){
                return new MessageVo(HttpStatus.SC_BAD_REQUEST,"不能上传空文件");
            }
            List<LogisticsPickup> pickupList = splitCsv(lines);
            if(CollectionUtils.isEmpty(pickupList)){
                return new MessageVo(HttpStatus.SC_BAD_REQUEST,"不能上传空文件");
            }
            Integer integer = pickupService.insertBatch(pickupList);
            log.info("上传文件end,一共耗时"+(System.currentTimeMillis()-d1));
            return new MessageVo(HttpStatus.SC_OK,"上传成功,总数：" + integer);
        }catch(Exception e){
            e.printStackTrace();
            return new MessageVo(HttpStatus.SC_BAD_REQUEST,e.getMessage());
        }
    }


    /**
     * 将上传的csv文件组合成为Logistics_pickup
     *1、校验是否存在pickup表中
     * 2、校验导入数据是否存在重复单
     * @return
     */
    private List<LogisticsPickup> splitCsv(String lines) {
        List<LogisticsPickup> pickupList = new ArrayList<LogisticsPickup>();
        List<String> tempId = new ArrayList<>();
        String[] linesArray = lines.split("\n");
        for (String line:linesArray){
            String[] fileds = line.split(",");
            if(fileds != null && fileds.length < 1){
                continue;
            }
            LogisticsPickup pickup = new LogisticsPickup();
            String orderId = parseTrackNumber(fileds[0].trim());
            if(fileds.length == 2){
                pickup.setAccount(fileds[1].trim());
            }
            try{
                Long.parseLong(orderId);
            }catch (Exception ex){
                continue;
            }
            //检查是否存在order 不存在侧插入
            if(pickupService.selectLogisticsPickupByOrderId(orderId) != null){
                pickup.setStatus(1);
                continue;
            }
            pickup.setStatus(0);
            pickup.setOrderId(orderId);
            if(tempId.contains(orderId)){//检查是否包含
                continue;
            }
            tempId.add(orderId);
            pickupList.add(pickup);
        }
        return pickupList;
    }

    private String parseTrackNumber(String filed){
        try{
            return String.valueOf(Long.parseLong(filed));
        }catch (Exception e){
            return filed;
        }
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String view(LogisticsPickup pickup, HttpServletRequest request){
        PageHelper.startPage(1, 10, true);
        List<LogisticsPickup> pickupList = pickupService.selectLogisticsPickup(pickup);
        request.setAttribute("table",new GridModel(new PageInfo<>(pickupList)));
        request.setAttribute("pickupParam",pickup);
        return "trackmore/logistics_taking";
    }

}

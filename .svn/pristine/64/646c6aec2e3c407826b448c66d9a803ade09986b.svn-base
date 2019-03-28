package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.LogisticsStatus;
import com.yks.bigdata.service.trackmore.IStatusInformationService;
import com.yks.bigdata.web.vo.FilterDto;
import com.yks.bigdata.web.vo.GridModel;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.apache.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
@Controller
@RequestMapping("/StatusInformation")
public class StatusInformationController {

    @Autowired
    IStatusInformationService statusService;

    @RequestMapping(value = "/view", method = RequestMethod.GET)
    public String view(LogisticsStatus status , FilterDto filterDto, HttpServletRequest request) {
        PageHelper.startPage(filterDto.getPage() <= 0?1:filterDto.getPage(), filterDto.getRows()<=0?10:filterDto.getRows(), true);
        List<LogisticsStatus> logistics_status = statusService.selectStatusInformation(status);
        request.setAttribute("table", new GridModel(new PageInfo<>(logistics_status)));
        request.setAttribute("channelParams", statusService.selectLogisticsStatusChannel());
        request.setAttribute("statusParam", status);
        return "trackmore/logistics_status";
    }

    @ResponseBody
    @RequestMapping(value = "/getInformation", method = RequestMethod.GET)
    public GridModel selectstate(LogisticsStatus status, FilterDto filterDto) {
        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<LogisticsStatus> accounts = statusService.selectStatusInformation(status);
        return new GridModel(new PageInfo<>(accounts));
    }

    @RequestMapping(value = "/updateview", method = RequestMethod.GET)
    public String updateView(LogisticsStatus channel, HttpServletRequest request) {
        request.setAttribute("statusInfo", statusService.selectStatusInformation(channel));
        return "trackmore/logistics_status_insert_or_edit";
    }

    @RequestMapping(value = "/inserteview", method = RequestMethod.GET)
    public String inserteview(HttpServletRequest request) {
        request.setAttribute("channels", statusService.selectLogisticsStatusChannel());
        return "trackmore/logistics_status_insert_or_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public MessageVo insert(String[] status, String[] channel, String[] excludeStatus) {
        try {
            if(channel != null && channel.length <= 0){
                return new MessageVo(HttpStatus.SC_BAD_REQUEST, "请选择物流渠道");
            }
            for (int j=0;j<channel.length;j++){
                LogisticsStatus logisticsStatus = new LogisticsStatus();
                logisticsStatus.setChannel(channel[j]);
                List<LogisticsStatus> logisticsStatuses = statusService.selectStatusInformation(logisticsStatus);
                if(!CollectionUtils.isEmpty(logisticsStatuses)){
                    continue;
                }
                List<LogisticsStatus> records = new ArrayList<>();
                for (int i = 0; i < status.length; i++) {
                    LogisticsStatus status2 = new LogisticsStatus();
                    status2.setChannel(channel[j]);
                    status2.setLogisticsStatus(i + 1);
                    status2.setKeyword(status[i]);
                    status2.setExcludeKeyword(excludeStatus[i]);
                    status2.setSort(i + 1);
                    records.add(status2);
                }
                statusService.insertBatch(records);
            }
        } catch (Exception e) {
            return new MessageVo(HttpStatus.SC_BAD_REQUEST, "新增物流状态失败");
        }
        return new MessageVo(HttpStatus.SC_OK, "新增物流状态成功");
    }

    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public MessageVo update(Integer[] id,String[] status, String channel, String[] excludeStatus) {
        try {
            if(id == null || status == null || id.length != status.length){
                return new MessageVo(HttpStatus.SC_BAD_REQUEST, "更新物流状态失败");
            }
            for (int i = 0; i < id.length; i++) {
                LogisticsStatus status2 = new LogisticsStatus();
                status2.setId(id[i]);
                status2.setChannel(channel);
                status2.setLogisticsStatus(i + 1);
                status2.setKeyword(status[i]);
                status2.setExcludeKeyword(excludeStatus[i]);
                status2.setSort(i + 1);
                statusService.update(status2);
            }
        } catch (Exception ex) {
            return new MessageVo(HttpStatus.SC_BAD_REQUEST, "更新物流状态失败");
        }
        return new MessageVo(HttpStatus.SC_OK, "更新物流状态成功");
    }

}

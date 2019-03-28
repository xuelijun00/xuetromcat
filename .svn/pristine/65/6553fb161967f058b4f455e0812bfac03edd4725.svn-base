package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import com.yks.bigdata.service.trackmore.ILogisticsChannelService;
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
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/30.
 */

@Controller
@RequestMapping("/LogisticChannel")
public class LogisticChannelController {

    @Autowired
    ILogisticsChannelService logisticsChannelService;

    @ResponseBody
    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public String insert(LogisticsChannel channel){
        int id = logisticsChannelService.insert(channel);
        if(id > 0){
            return new MessageVo(HttpStatus.SC_OK,"新增渠道成功").toString();
        }else{
            return new MessageVo(HttpStatus.SC_BAD_REQUEST,"新增渠道失败").toString();
        }
    }
    @RequestMapping(value = "/updateview",method = RequestMethod.GET)
    public String updateView(LogisticsChannel channel, HttpServletRequest request){
        request.setAttribute("channel",logisticsChannelService.selectLogisticsChannelById(channel.getId()));
        return "trackmore/channel_management_add_or_edit";
    }

    @ResponseBody
    @RequestMapping(value = "/update/{id}",method = RequestMethod.POST)
    public String update(@PathVariable("id") Integer id, LogisticsChannel channel, String status, HttpServletRequest request){
        try{logisticsChannelService.update(channel);}catch (Exception ex){
            return new MessageVo(HttpStatus.SC_BAD_REQUEST,"更新渠道失败").toString();
        }
        return new MessageVo(HttpStatus.SC_OK,"更新渠道成功").toString();
    }

    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public GridModel update(LogisticsChannel channel, FilterDto filterDto){
        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<LogisticsChannel> logistics_channels = logisticsChannelService.selectLogisticsChannels(channel);
        GridModel gridModel = new GridModel(new PageInfo<>(logistics_channels));
        gridModel.setPagesize(filterDto.getRows());
        return gridModel;
    }

    @ResponseBody
    @RequestMapping(value = "/trackmorecodes",method = RequestMethod.GET)
    public List<String> trackmoreCode(){
        return logisticsChannelService.selectLogisticsChannelCode();
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String view(LogisticsChannel channel , FilterDto filterDto, HttpServletRequest request){
        PageHelper.startPage(filterDto.getPage() <= 0?1:filterDto.getPage(), filterDto.getRows()<=0?10:filterDto.getRows(), true);
        List<LogisticsChannel> logistics_channels = logisticsChannelService.selectLogisticsChannels(channel);
        request.setAttribute("table",new GridModel(new PageInfo<>(logistics_channels)));
        request.setAttribute("channels",logisticsChannelService.selectLogisticsChannelCode());
        request.setAttribute("channelParam",channel);
        return "trackmore/channel_management";
    }

    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public MessageVo upload(MultipartFile file ){
        try{
            String dataStr = IOUtils.toString(file.getInputStream(), Charset.forName("GBK"));
            if(StringUtils.isEmpty(dataStr)){
                return new MessageVo(HttpStatus.SC_BAD_REQUEST,"上传数据为空,请检查！");
            }
            List<Object> data = CsvUtils.parseCsv(dataStr, CSVFormat.DEFAULT, ChannelExcelTemp.class);
            List<LogisticsChannel> channels = new ArrayList<>();
            StringBuffer stringBuffer = new StringBuffer("以下数据添加失败：");
            stringBuffer.append("\n");
            for (int i = 0;i<data.size();i++) {
                LogisticsChannel logisticsChannel = ((ChannelExcelTemp) data.get(i)).toDto();
                LogisticsChannel con = new LogisticsChannel();
                con.setYksChannelName(logisticsChannel.getYksChannelName());
                if(StringUtils.isNotEmpty(logisticsChannel.getShortCode())//带*号字段不能为空,不能存在已有数据
                        && StringUtils.isNotEmpty(logisticsChannel.getYksChannelCode())
                        && StringUtils.isNotEmpty(logisticsChannel.getYksChannelName())
                        && logisticsChannelService.selectLogisticsChannels(con).size() < 1){//查询数据库是否存在渠道，存在则不插入
                    channels.add(logisticsChannel);
                }else{
                    stringBuffer.append(String.format("第%d行数据\n",i + 1));
                }
            }
            logisticsChannelService.insertBatch(channels);
            if(stringBuffer.length() > 10)
                return new MessageVo(HttpStatus.SC_OK,"上传成功！\n" + stringBuffer.toString());
            else
                return new MessageVo(HttpStatus.SC_OK,"上传成功！");
        }catch(Exception ex){
            return new MessageVo(HttpStatus.SC_BAD_REQUEST,"上传失败,请按模板上传！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/disable/{id}",method = RequestMethod.GET)
    public MessageVo disable(@PathVariable("id")Integer id){
        logisticsChannelService.disableOrEnabledChannel(id,0);
        return new MessageVo(HttpStatus.SC_OK,"禁用成功！");
    }

    @ResponseBody
    @RequestMapping(value = "/enabled/{id}",method = RequestMethod.GET)
    public MessageVo enabled(@PathVariable("id") Integer id){
        logisticsChannelService.disableOrEnabledChannel(id,1);
        return new MessageVo(HttpStatus.SC_OK,"启用成功！");
    }

}

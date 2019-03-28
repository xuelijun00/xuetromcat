package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.TrackmoreAccount;
import com.yks.bigdata.service.scheduled.AsyncTask;
import com.yks.bigdata.service.trackmore.ITrackmoreAccountService;
import com.yks.bigdata.web.vo.FilterDto;
import com.yks.bigdata.web.vo.GridModel;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by zh on 2017/6/30.
 */
@Controller
@RequestMapping("/account")
public class TrackMoreAccountController {

    @Autowired
    ITrackmoreAccountService accountService;

    @RequestMapping("/")
    public String index(){
        return "trackmore/account";
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST , value = "/insert")
    public MessageVo insert(TrackmoreAccount account){
        account.setDataCount(1500000);
        account.setCreateTime(new Date());
        account.setStatus(1);
        int id = accountService.insert(account);
        if(id > 0){
            return new MessageVo(200,"新增账号成功");
        }else{
            return new MessageVo(400,"新增账号失败");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public MessageVo update(TrackmoreAccount account){
        accountService.update(account);
        return new MessageVo(200,"更新成功");
    }

    @RequestMapping(value = "/updateview/{id}",method = RequestMethod.GET)
    public String updateView(@PathVariable("id")Integer id, HttpServletRequest request){
        request.setAttribute("account",accountService.selectByPrimaryKey(id));
        return "trackmore/add_account";
    }

    @ResponseBody
    @RequestMapping(value = "/enable",method = RequestMethod.POST)
    public MessageVo enable(TrackmoreAccount account){
        if (account.getId() == null)
            return new MessageVo(400,"启用失败!id为null");
        accountService.enable(account);
        return new MessageVo(200,"启用成功");
    }

    @ResponseBody
    @RequestMapping(value = "/disable",method = RequestMethod.POST)
    public MessageVo disable(TrackmoreAccount account){
        if (account.getId() == null)
            return new MessageVo(400,"禁用失败!id为null");
        accountService.disable(account);
        return new MessageVo(200,"禁用成功");
    }


    @ResponseBody
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public GridModel update(TrackmoreAccount account , FilterDto filterDto){
        PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
        List<TrackmoreAccount> accounts = accountService.selectTrackmoreAccount(account);
        return new GridModel(new PageInfo<>(accounts));
    }

    @RequestMapping(value = "/view",method = RequestMethod.GET)
    public String view( TrackmoreAccount account , HttpServletRequest request){
        PageHelper.startPage(1, 10, true);
        List<TrackmoreAccount> trackmoreAccounts = accountService.selectTrackmoreAccount(account);
        request.setAttribute("table",new GridModel(new PageInfo<>(trackmoreAccounts)));
        request.setAttribute("accountParam",account);
        return "trackmore/account";
    }

    @Autowired
    AsyncTask asyncTask;

    @RequestMapping(value = "/view1",method = RequestMethod.GET)
    public String view1(HttpServletRequest request,String key,String erpOrderIds){
        try{
            request.setAttribute("accounts", accountService.selectTrackmoreAccount(null));
            if(StringUtils.isEmpty(key) || StringUtils.isEmpty(erpOrderIds)){
                request.setAttribute("responseData","key和订单号都不能为空");
                return "trackmore/account_test";
            }
            List<Long> orderId = new ArrayList<>();
            String[] split = erpOrderIds.split("\r\n");
            for (int i=0;i<split.length;i++){
                try{orderId.add(Long.parseLong(split[i]));}catch (Exception ex){}
            }
            Future<String> stringFuture = asyncTask.doTask(orderId, key);
            request.setAttribute("key",key);
            request.setAttribute("erpOrderIds",erpOrderIds);
            request.setAttribute("responseData",stringFuture.get());
        }catch (Exception ex){
            ex.printStackTrace();
            request.setAttribute("key",key);
            request.setAttribute("erpOrderIds",erpOrderIds);
            request.setAttribute("responseData",ex.getLocalizedMessage());
        }
        return "trackmore/account_test";
    }

}

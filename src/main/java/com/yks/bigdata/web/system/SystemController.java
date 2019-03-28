package com.yks.bigdata.web.system;

import com.yks.bigdata.dto.system.SystemUser;
import com.yks.bigdata.service.system.SystemUserService;
import com.yks.bigdata.web.vo.MessageVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/6/24.
 */

@Controller
@RequestMapping("/")
public class SystemController {

    @Autowired
    private SystemUserService systemUserService;

    @RequestMapping("/")
    public String root(){
        return "login";
    }

    @RequestMapping("/index")
    public String index(){
        return "index";
    }

    @RequestMapping("/common")
    public String common(String path, HttpServletRequest request){
        Enumeration<String> attributeNames = request.getAttributeNames();
        while(attributeNames.hasMoreElements()){
            String name = attributeNames.nextElement();
            request.setAttribute(name,request.getAttribute(name));
        }
        return path;
    }

    @RequestMapping(method = RequestMethod.POST , value = "/login")
    public String login(String username, String password , HttpServletRequest request){
        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            request.setAttribute("loginError","请输入账号或密码！");
            return "login";
        }
        SystemUser loginUser = systemUserService.login(username, password);
        if(loginUser != null){
            HttpSession session = request.getSession();
            session.setAttribute("systemUser",loginUser);
            return "redirect:index";                                            //重定向
        }else{
            request.setAttribute("loginError","账号或密码错误！");
            return "login";
        }
    }

    @RequestMapping(method = RequestMethod.GET , value = "/logout")
    public String login(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute("systemUser");
        return "login";
    }


}

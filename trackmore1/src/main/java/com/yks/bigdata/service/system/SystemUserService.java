package com.yks.bigdata.service.system;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yks.bigdata.dto.system.SystemUser;
import com.yks.bigdata.util.HttpRequestUtils;
import com.yks.bigdata.util.MD5Util;
import com.yks.bigdata.util.ResponseData;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by liuxing on 2017/5/5.
 */
@Service
public class SystemUserService {

    private static Logger log = LoggerFactory.getLogger(SystemUserService.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 用户登入
     *
     * @param username
     * @param password
     * @return
     * @throws JsonProcessingException 
     * @throws IOException
     */
    public SystemUser login(String username, String password){
        SystemUser user = null;
        String url = "http://sso.youkeshu.com/?c=of_base_sso_api&a=ticket&name=trackmore&callback=callback";
        ResponseData responseData = HttpRequestUtils.sendHttpGet(url);
        String responseText = responseData.getResponseText();
        String ticket = "";
        try {
            if (StringUtils.isNotEmpty(responseText)) {
                String json = responseText.substring(responseText.indexOf("(") + 1, responseText.lastIndexOf(")"));
                JsonNode jsonNode = objectMapper.readTree(json);
                if (jsonNode.get("state").asInt() == 200) {
                    ticket = jsonNode.get("ticket").asText();
                } else {
                    log.info("请求无法获取token！");
                    return user;
                }
            }
            StringBuffer sb = new StringBuffer("http://sso.youkeshu.com/?c=of_base_sso_api&a=check");
            sb.append("&space=default&name=trackmore&role=1");
            sb.append("&user=" + username);
            sb.append("&pwd=" + password);
            sb.append("&ticket=" + ticket);
            String md5 = "of_base_sso_apicheckdefaulttrackmore1" + username + password + ticket + "123456";
            sb.append("&md5=" + MD5Util.encode(md5).toLowerCase());
            ResponseData responseData1 = HttpRequestUtils.sendHttpGet(sb.toString());
            System.out.println(responseData1.getResponseText());
            JsonNode jsonNode = objectMapper.readTree(responseData1.getResponseText());
            if (jsonNode.get("state").asInt() == 200) {
                user = new SystemUser();
                user.setId(jsonNode.get("user").asInt());
                user.setUsername(jsonNode.get("nike").asText());
                if (jsonNode.get("role").get("allow").get("pack") != null) {
                    List<String> role = new ArrayList<String>();
                    List<String> competence = new ArrayList<String>();
                    Iterator<String> jn = jsonNode.get("role").get("allow").get("pack").fieldNames();//角色
                    while (jn.hasNext()) {
                        String key = jn.next();
                        role.add(key);
                        Iterator<String> jn1 = jsonNode.get("role").get("allow").get("pack").get(key).get("func").fieldNames();//权限
                        while (jn1.hasNext()) {
                            competence.add(jn1.next());
                        }
                    }
                    user.setRole(role);
                    user.setCompetence(competence);
                }
                log.info("登入成功：" + jsonNode.get("nike").asText());
            } else {
                log.info("登入失败：" + jsonNode.get("msg").asText());
            }
        }catch (Exception e){
            log.error("登入异常：" + e.getLocalizedMessage());
        }
        return user;
    }

}

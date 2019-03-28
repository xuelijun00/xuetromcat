package com.yks.bigdata.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;

/**
 * Created by liuxing on 2017/7/11.
 */
public class CustomDateDeserialize extends JsonDeserializer<Date> {

    private final static Logger LOG = LoggerFactory.getLogger(CustomDateDeserialize.class);

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext ctxt)throws IOException, JsonProcessingException {
        Date date = null;
        if(StringUtils.isNotEmpty(jp.getText())){
            try {
                date = DateUtils.formmatStr(jp.getText());
            }catch (Exception ex){
                LOG.error("时间参数有误："+ jp.getText() +"------------" + ex.getMessage());
            }
        }
        return date;
    }
}

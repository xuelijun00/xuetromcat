package com.yks.bigdata.util;

import com.yks.bigdata.common.ExceptionEnum;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.io.IOUtils;
import org.apache.poi.util.Internal;
import org.junit.Ignore;
import org.junit.Test;

import com.yks.bigdata.web.vo.ChannelExcelTemp;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Administrator on 2017/7/4.
 */
@Ignore
public class CsvUtilsTest {

    @Test
    public void parseCsv() throws Exception {
        File file = new File("C:\\Users\\Administrator\\Desktop\\channel_templater.csv");
        List<Object> objects = CsvUtils.parseCsv(file, ChannelExcelTemp.class);
        System.out.println(objects.size());
    }

    @Test
    public void test1() throws Exception {
        File file = new File("G:\\projects\\trackmore\\src\\main\\resources\\static\\excel\\channel_templater.csv");
        String str = IOUtils.toString(new FileInputStream(file), Charset.forName("utf-8"));
        CSVParser csvParse = CSVParser.parse(file, Charset.forName("utf-8"), CSVFormat.DEFAULT);
        Map<String, Integer> headerMap = csvParse.getHeaderMap();
        for (Map.Entry<String,Integer> entry:headerMap.entrySet()) {
            System.out.println(entry.getKey() + "--------------" + entry.getValue());
        }
    }

    @Test
    public void test2() throws Exception {
        List<List<Integer>> list = new ArrayList<>();
        ArrayList<Integer> objects = new ArrayList<>();
        for (int i = 0;i<1000000;i++){
            objects.add(i);
            if(objects.size() == 1000){
                list.add(objects);
                objects = new ArrayList<>();
            }
        }
        if(objects.size() > 0){
            list.add(objects);
        }
        System.out.println(list.size() * 1000);
    }

}
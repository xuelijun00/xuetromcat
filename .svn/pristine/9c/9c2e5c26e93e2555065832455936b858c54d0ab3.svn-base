package com.yks.bigdata.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by Administrator on 2017/7/4.
 */
public class CsvUtils {

    private static Logger log = LoggerFactory.getLogger(CsvUtils.class);

    /**
     * 解析csv
     * @param file
     * @param chartset
     * @param format
     * @param cls
     * @return
     * @throws Exception
     */
    public static List<Object> parseCsv(File file,String chartset,CSVFormat format,Class<?> cls) throws Exception{
        CSVParser csvParse = CSVParser.parse(file, Charset.forName(chartset), format);
        List<CSVRecord>  csvRecords = csvParse.getRecords();
        return getCsvData(csvParse,cls);
    }

    public static List<Object> parseCsv(File file,Class<?> cls) throws Exception{
        return parseCsv(file,"utf-8",CSVFormat.DEFAULT,cls);
    }
    public static List<Object> parseCsv(String source, CSVFormat format, Class<?> cls) throws Exception{
        CSVParser parse = CSVParser.parse(source, format);
        return getCsvData(parse,cls);
    }
    private static List<Object> getCsvData(CSVParser csvParse,Class<?> cls)throws Exception{
        Map<Integer, String> columns = CsvUtils.generateColumn(cls, "set");
        List<CSVRecord>  csvRecords = csvParse.getRecords();
        List<Object> records = new ArrayList<Object>();
        for (int i = 1; i < csvRecords.size(); i++) {
            Object obj = CsvUtils.newInstance(cls);
            for (Map.Entry<Integer, String> entry : columns.entrySet()) {
                String value = csvRecords.get(i).get(entry.getKey());
                CsvUtils.setFileValue(obj, entry.getValue(), value);
            }
            records.add(obj);
        }
        log.info("解析数据成功：" + records.size());
        return records;
    }

    /**
     * 设置字段值 只支持标准been
     * @param cls
     * @param methodName
     * @param value
     */
    public static boolean setFileValue(Object cls,String methodName,Object value){
        //使用该方法要指定参数类型
        //Method method = ReflectionUtils.findMethod(ExcelPojo.class, "setOrdersNumber",String.class);
        Method[] method = ReflectionUtils.getAllDeclaredMethods(cls.getClass());
        for (int i = 0; i < method.length; i++) {
            if(method[i].getName().equals(methodName)){
                Class<?>[] paramTypes = method[i].getParameterTypes();
                try{
                    if("java.lang.Integer".equals(paramTypes[0].getName())){
                        value = Integer.parseInt(value.toString().replaceAll(",", ""));
                    }else if("java.lang.Float".equals(paramTypes[0].getName())){
                        value = Float.parseFloat(value.toString().replaceAll(",", ""));
                    }else if("java.lang.Double".equals(paramTypes[0].getName())){
                        value = Double.parseDouble(value.toString().replaceAll(",", ""));
                    }
                }catch(Exception ex){
                    log.error("设置value值失败,类型不能转换,methodName:" + methodName + " value:" + value);
                    return false;
                }
                ReflectionUtils.invokeMethod(method[i], cls, value);
                break;
            }
        }
        return true;
    }

    /**
     * 获取值
     * @param cls
     * @param methodName
     * @return
     */
    public static Object getFileValue(Object cls,String methodName){
        //使用该方法要指定参数类型
        //Method method = ReflectionUtils.findMethod(ExcelPojo.class, "setOrdersNumber",String.class);
        Method[] method = ReflectionUtils.getAllDeclaredMethods(cls.getClass());
        for (int i = 0; i < method.length; i++) {
            if(method[i].getName().equals(methodName)){
                return ReflectionUtils.invokeMethod(method[i], cls);
            }
        }
        return null;
    }

    /**
     * 实例化对象
     * @param class1
     * @return
     * @throws Exception
     */
    public static Object newInstance(Class<?> class1) throws Exception{
        Constructor<?> constructor = class1.getConstructor();
        return constructor.newInstance();
    }

    /**
     * 获取bean中的set方法
     * 并按照 字母排序 和 columnIndex 对应
     * @param cls
     * @return
     */
    public static Map<Integer,String> generateColumn(Class<?> cls, String prefix, int...columnIndex){
        Method[] method = ReflectionUtils.getAllDeclaredMethods(cls);
        List<String> methodNames = new ArrayList<String>();
        for (Method method2 : method) {
            if(method2.getName().startsWith(prefix) && method2.getName().indexOf("_") > -1){
                methodNames.add(method2.getName());
            }
        }
        Collections.sort(methodNames);
        int i = 0;
        Map<Integer,String> columns = new HashMap<Integer,String>();
        for (String string : methodNames) {
            if(columnIndex.length > 0){
                columns.put(columnIndex[i], string);
            }else{
                columns.put(i, string);
            }
            i++;
        }
        log.info("生成excel列和字段对应关系");
        return columns;
    }

}

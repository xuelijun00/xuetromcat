package com.yks.bigdata.service.trackmore.impl;

import com.yks.bigdata.dto.trackmore.LogisticReport;
import com.yks.bigdata.service.trackmore.ILogisticsReportService;
import com.yks.bigdata.util.KylinUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.List;

/**
 * Created by zh on 2017/6/30.
 */
@Service
public class LogisticsReportServiceImpl implements ILogisticsReportService{

    private static Logger log = LoggerFactory.getLogger(LogisticsReportServiceImpl.class);

    @Autowired
    KylinUtils kylinUtils;

    @Override
    public List<LogisticReport> getKylinReportData(LogisticReport report) {
        String sql = "select a.platform,a.buyer_country as buyerCountry,a.channel_name as channelName,a.paymentcount,b.shipcount, cast(b.shipcount as double)/cast(paymentcount as double) as orderexecutionPercent\n" +
                ",c.internetcount as internetcount,cast(c.internetcount as double)/cast(b.shipcount as double) as internetPercent,cast(c.internet_date as double)/cast(c.internetcount as double) as internetDate\n" +
                ",d.balecount as balecount,cast(d.balecount as double)/cast(b.shipcount as double) as balePercent,cast(d.bale_date as double)/cast(d.balecount as double) as baleDate\n" +
                ",e.trafficcount as trafficcount,cast(e.trafficcount as double)/cast(b.shipcount as double) as trafficPercent,cast(e.traffic_date as double)/cast(e.trafficcount as double) as trafficDate\n" +
                ",f.landingcount as landingcount,cast(f.landingcount as double)/cast(b.shipcount as double) as landingPercent,cast(f.landing_date as double)/cast(f.landingcount as double) as landingDate\n" +
                ",g.deliveredcount as deliveredcount,cast(g.deliveredcount as double)/cast(b.shipcount as double) as deliveredPercent,cast(g.delivered_date as double)/cast(g.deliveredcount as double) as deliveredDate\n" +
                ",b.heavi as heavi,b.heavi/b.shipcount as avgheavi\n" +
                ",b.fee as fee,b.fee/b.shipcount as avgprice\n" +
                "from (\n" +
                "select platform,buyer_country,channel_name,count(1) as paymentcount\n" +
                "\tfrom erp_orders_process\n where 1=1";
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }

        sql +=  "\tgroup by platform,buyer_country,channel_name\n" +
                ") a INNER JOIN (\n" +
                "select platform,buyer_country,channel_name,count(1) as shipcount,sum(heavi) as heavi,sum(fee) as fee\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere orders_status = 5\n";
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by platform,buyer_country,channel_name) b on a.platform = b.platform and a.buyer_country = b.buyer_country and a.channel_name = b.channel_name\n" +
                "LEFT JOIN (\n" +
                "\tselect platform,buyer_country,channel_name,count(1) as internetcount \n" +
                "\t\t,sum(shangwangdate) as internet_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 1 or track_status = 2 or track_status = 3 or track_status = 4 or track_status = 5\n";
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by platform,buyer_country,channel_name) c on a.platform = c.platform and a.buyer_country = c.buyer_country and a.channel_name = c.channel_name\n" +
                "LEFT JOIN(\n" +
                "select platform,buyer_country,channel_name,count(1) as balecount \n" +
                "\t\t,sum(fengfadate) bale_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 2 or track_status = 3 or track_status = 4 or track_status = 5\n" ;
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by platform,buyer_country,channel_name) d on a.platform = d.platform and a.buyer_country = d.buyer_country and a.channel_name = d.channel_name\n" +
                "left join(\n" +
                "select platform,buyer_country,channel_name,count(1) as trafficcount \n" +
                "\t\t,sum(jiaohangdate) traffic_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 3 or track_status = 4 or track_status = 5\n";
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by platform,buyer_country,channel_name) e on a.platform = e.platform and a.buyer_country = e.buyer_country and a.channel_name = e.channel_name\n" +
                "left join(\n" +
                "select platform,buyer_country,channel_name,count(1) as landingcount \n" +
                "\t\t,sum(luodidate) landing_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 4 or track_status = 5\n";
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by platform,buyer_country,channel_name) f on a.platform = f.platform and a.buyer_country = f.buyer_country and a.channel_name = f.channel_name\n" +
                "left join(\n" +
                "select platform,buyer_country,channel_name,count(1) as deliveredcount \n" +
                "\t\t,sum(tuotoudate) delivered_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 5\n";
        if(report.getPlatform() != null){
            sql += "and platform in (" + report.getPlatform() + ") ";
        }
        if(report.getBuyerCountry() != null){
            sql += "and buyer_country in (" + report.getBuyerCountry() + ") ";
        }
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by platform,buyer_country,channel_name) g on a.platform = g.platform and a.buyer_country = g.buyer_country and a.channel_name = g.channel_name";
        QueryRunner queryRunner = new QueryRunner();
        List<LogisticReport> query = null;
        try{
            Connection connection = kylinUtils.getConnection();
            query = (List<LogisticReport>) queryRunner.query(connection, sql, new BeanListHandler(LogisticReport.class));
            kylinUtils.destory(connection);
        }catch (Exception ex){
            log.error("kylin查询失败：" +  ex.getLocalizedMessage());
        }
        return query;
    }

    @Override
    public List<LogisticReport> getKylinReportDataGroupChannel(LogisticReport report) {
        String sql = "select a.channel_name as channelName,a.paymentcount,b.shipcount, cast(b.shipcount as double)/cast(paymentcount as double) as orderexecutionPercent\n" +
                ",c.internetcount as internetcount,cast(c.internetcount as double)/cast(b.shipcount as double) as internetPercent,cast(c.internet_date as double)/cast(c.internetcount as double) as internetDate\n" +
                ",d.balecount as balecount,cast(d.balecount as double)/cast(b.shipcount as double) as balePercent,cast(d.bale_date as double)/cast(d.balecount as double) as baleDate\n" +
                ",e.trafficcount as trafficcount,cast(e.trafficcount as double)/cast(b.shipcount as double) as trafficPercent,cast(e.traffic_date as double)/cast(e.trafficcount as double) as trafficDate\n" +
                ",f.landingcount as landingcount,cast(f.landingcount as double)/cast(b.shipcount as double) as landingPercent,cast(f.landing_date as double)/cast(f.landingcount as double) as landingDate\n" +
                ",g.deliveredcount as deliveredcount,cast(g.deliveredcount as double)/cast(b.shipcount as double) as deliveredPercent,cast(g.delivered_date as double)/cast(g.deliveredcount as double) as deliveredDate\n" +
                ",b.heavi as heavi,b.heavi/b.shipcount as avgheavi\n" +
                ",b.fee as fee,b.fee/b.shipcount as avgprice\n" +
                "from (\n" +
                "select channel_name,count(1) as paymentcount\n" +
                "\tfrom erp_orders_process\n where 1=1" ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
                sql += "\tgroup by channel_name\n" +
                ") a INNER JOIN (\n" +
                "select channel_name,count(1) as shipcount,sum(heavi) as heavi,sum(fee) as fee\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere orders_status = 5\n"  ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by channel_name\n" +
                ") b on  a.channel_name = b.channel_name\n" +
                "LEFT JOIN (\n" +
                "\tselect channel_name,count(1) as internetcount \n" +
                "\t\t,sum(shangwangdate) as internet_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 1 or track_status = 2 or track_status = 3 or track_status = 4 or track_status = 5\n"  ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by channel_name\n" +
                ") c on a.channel_name = c.channel_name\n" +
                "LEFT JOIN(\n" +
                "select channel_name,count(1) as balecount \n" +
                "\t\t,sum(fengfadate) bale_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 2 or track_status = 3 or track_status = 4 or track_status = 5\n"  ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql +=  "\tgroup by channel_name\n" +
                ") d on a.channel_name = d.channel_name\n" +
                "left join(\n" +
                "select channel_name,count(1) as trafficcount \n" +
                "\t\t,sum(jiaohangdate) traffic_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 3 or track_status = 4 or track_status = 5\n"  ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by channel_name\n" +
                ") e on a.channel_name = e.channel_name\n" +
                "left join(\n" +
                "select channel_name,count(1) as landingcount \n" +
                "\t\t,sum(luodidate) landing_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 4 or track_status = 5\n"  ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by channel_name\n" +
                ") f on a.channel_name = f.channel_name\n" +
                "left join(\n" +
                "select channel_name,count(1) as deliveredcount \n" +
                "\t\t,sum(tuotoudate) delivered_date\n" +
                "\tfrom erp_orders_process \n" +
                "\twhere track_status = 5\n" ;
        if(report.getChannelName() != null){
            sql += "and channel_name in (" + report.getChannelName() + ") ";
        }
        if(report.getStartTime() != null){
            sql += "and orders_out_time >= '" + report.getStartTime() + "' ";
        }
        if(report.getEndTime() != null){
            sql += "and orders_out_time <= '" + report.getEndTime() + "' ";
        }
        sql += "\tgroup by channel_name\n" +
                ") g on a.channel_name = g.channel_name";

        QueryRunner queryRunner = new QueryRunner();
        List<LogisticReport> query = null;
        try{
            Connection connection = kylinUtils.getConnection();
            query = (List<LogisticReport>) queryRunner.query(connection, sql, new BeanListHandler(LogisticReport.class));
            kylinUtils.destory(connection);
        }catch (Exception ex){
            log.error("kylin查询失败：" +  ex.getLocalizedMessage());
        }
        return query;
    }
}

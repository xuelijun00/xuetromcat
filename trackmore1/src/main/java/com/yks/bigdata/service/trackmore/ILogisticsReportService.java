package com.yks.bigdata.service.trackmore;

import com.yks.bigdata.dto.trackmore.ErpOrders;
import com.yks.bigdata.dto.trackmore.LogisticReport;
import com.yks.bigdata.dto.trackmore.Physical;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by zh on 2017/6/29.
 */
public interface ILogisticsReportService {

    List<LogisticReport> getKylinReportData(LogisticReport report);

    List<LogisticReport> getKylinReportDataGroupChannel(LogisticReport report);
 
}

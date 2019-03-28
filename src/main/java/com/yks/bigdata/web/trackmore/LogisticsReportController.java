package com.yks.bigdata.web.trackmore;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yks.bigdata.dto.trackmore.LogisticReport;
import com.yks.bigdata.dto.trackmore.LogisticsChannel;
import com.yks.bigdata.service.trackmore.IErpOrdersService;
import com.yks.bigdata.service.trackmore.ILogisticsChannelService;
import com.yks.bigdata.service.trackmore.ILogisticsReportService;
import com.yks.bigdata.service.trackmore.IStatusInformationService;
import com.yks.bigdata.service.trackmore.impl.ErpOrdersProcessService;
import com.yks.bigdata.web.vo.FilterDto;
import com.yks.bigdata.web.vo.GridModel;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Administrator on 2017/6/30.
 */

@Controller
@RequestMapping("/LogisticsReport")
public class LogisticsReportController {

	@Autowired
	IStatusInformationService statusService;

	@Autowired
	ILogisticsChannelService logisticsChannelService;

	@Autowired
	IErpOrdersService erpOrdersService;

	@Autowired
	ILogisticsReportService iLogisticsReportService;

	@ResponseBody
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public GridModel getPageData(FilterDto filterDto, HttpServletRequest request) {
		LogisticReport logisticReport = new LogisticReport();
		String[] platform = request.getParameterValues("platform[]");
		String[] country = request.getParameterValues("country[]");
		String[] channel = request.getParameterValues("channel[]");
		if (platform != null && platform.length > 0) {
			logisticReport.setPlatform("'" + StringUtils.join(platform, "','") + "'");
		}
		if (country != null && country.length > 0) {
			logisticReport.setBuyerCountry("'" + StringUtils.join(country, "','") + "'");
		}
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}
		List<LogisticReport> logisticReports = null;
		if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
			PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
			logisticReports = erpOrdersService.selectReportDataAndDate(logisticReport);
		}else{
			PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
			logisticReports = erpOrdersService.selectReportData(logisticReport);
		}

		GridModel gridModel = new GridModel(new PageInfo<>(logisticReports));
		gridModel.setPagesize(filterDto.getRows());
		return gridModel;
	}

	@RequestMapping(value = "/view", method = RequestMethod.GET)
	public String view(HttpServletRequest request, String[] platform, String[] country,String[] channel) {
		LogisticReport logisticReport = new LogisticReport();
		if (platform != null && platform.length > 0) {
			logisticReport.setPlatform("'" + StringUtils.join(platform, "','") + "'");
		}
		if (country != null && country.length > 0) {
			logisticReport.setBuyerCountry("'" + StringUtils.join(country, "','") + "'");
		}
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}
		List<LogisticReport> logisticReports = null;
		if(StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)){
			PageHelper.startPage(1, 10, true);
			logisticReports = erpOrdersService.selectReportDataAndDate(logisticReport);
		}else{
			PageHelper.startPage(1, 10, true);
			logisticReports = erpOrdersService.selectReportData(logisticReport);
		}
		request.setAttribute("table", new GridModel(new PageInfo<>(logisticReports)));
		request.setAttribute("platform", erpOrdersService.selectplatform());
		request.setAttribute("country", erpOrdersService.selectbuyerCountry());
		request.setAttribute("channel", erpOrdersService.selectchannelName());

		request.setAttribute("platformParam", platform);
		request.setAttribute("countryParam", country);
		request.setAttribute("channelParam", channel);
		request.setAttribute("startTime", request.getParameter("startTime"));
		request.setAttribute("endTime", request.getParameter("endTime"));
		return "trackmore/logistics_report";
	}

	@RequestMapping(value = "/kylin/view1", method = RequestMethod.GET)
	public String view1kylin(HttpServletRequest request, String[] platform, String[] country,String[] channel) {
		LogisticReport logisticReport = new LogisticReport();
		if (platform != null && platform.length > 0) {
			logisticReport.setPlatform("'" + StringUtils.join(platform, "','") + "'");
		}
		if (country != null && country.length > 0) {
			logisticReport.setBuyerCountry("'" + StringUtils.join(country, "','") + "'");
		}
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}

		FilterDto filterDto = new FilterDto();
		filterDto.setPage(1);
		filterDto.setRows(10);

		List<LogisticReport> logisticReports = iLogisticsReportService.getKylinReportData(logisticReport);
		GridModel gridModel = new GridModel();
		if(!CollectionUtils.isEmpty(logisticReports)){
			gridModel.setPagesize(filterDto.getRows());
			gridModel.setTotal(logisticReports.size()%filterDto.getRows() ==0?logisticReports.size()/filterDto.getRows():logisticReports.size()/filterDto.getRows() + 1);
			gridModel.setPage(filterDto.getPage());
			gridModel.setRecords(logisticReports.size());
			gridModel.setRows(pageData(filterDto,logisticReports));
		}

		request.setAttribute("table", gridModel);
		request.setAttribute("platform", erpOrdersService.selectplatform());
		request.setAttribute("country", erpOrdersService.selectbuyerCountry());
		request.setAttribute("channel", erpOrdersService.selectchannelName());

		request.setAttribute("platformParam", platform);
		request.setAttribute("countryParam", country);
		request.setAttribute("channelParam", channel);
		request.setAttribute("startTime", request.getParameter("startTime"));
		request.setAttribute("endTime", request.getParameter("endTime"));
		return "trackmore/logistics_report1";
	}

	@ResponseBody
	@RequestMapping(value = "/kylin/get1", method = RequestMethod.GET)
	public GridModel getkylinPageData1(FilterDto filterDto, HttpServletRequest request) {
		LogisticReport logisticReport = new LogisticReport();
		String[] platform = request.getParameterValues("platform[]");
		String[] country = request.getParameterValues("country[]");
		String[] channel = request.getParameterValues("channel[]");
		if (platform != null && platform.length > 0) {
			logisticReport.setPlatform("'" + StringUtils.join(platform, "','") + "'");
		}
		if (country != null && country.length > 0) {
			logisticReport.setBuyerCountry("'" + StringUtils.join(country, "','") + "'");
		}
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}

		List<LogisticReport> logisticReports = iLogisticsReportService.getKylinReportData(logisticReport);
		GridModel gridModel = new GridModel();
		if(logisticReports == null || logisticReports.size() <= 0){
			gridModel.setPagesize(filterDto.getRows());
			gridModel.setTotal(0);
			gridModel.setPage(filterDto.getPage());
			gridModel.setRecords(0);
			gridModel.setRows(null);
		}else if(filterDto.getRows() != 0&& filterDto.getPage() != 0){
			gridModel.setPagesize(filterDto.getRows());
			gridModel.setTotal(logisticReports.size()%filterDto.getRows() ==0?logisticReports.size()/filterDto.getRows():logisticReports.size()/filterDto.getRows() + 1);
			gridModel.setPage(filterDto.getPage());
			gridModel.setRecords(logisticReports.size());
			gridModel.setRows(pageData(filterDto,logisticReports));
		}else{
			gridModel.setRows(logisticReports);//导出
		}
		return gridModel;
	}

	@RequestMapping(value = "/channel/kylin/view", method = RequestMethod.GET)
	public String channelkylinView(HttpServletRequest request,String[] channel) {
		LogisticReport logisticReport = new LogisticReport();
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}

		List<LogisticReport> logisticReports = iLogisticsReportService.getKylinReportDataGroupChannel(logisticReport);
		GridModel gridModel = new GridModel();
		if(!CollectionUtils.isEmpty(logisticReports)){
			FilterDto filterDto = new FilterDto();
			filterDto.setPage(1);
			filterDto.setRows(10);

			gridModel.setPagesize(filterDto.getRows());
			gridModel.setTotal(logisticReports.size()%filterDto.getRows() ==0?logisticReports.size()/filterDto.getRows():logisticReports.size()/filterDto.getRows() + 1);
			gridModel.setPage(filterDto.getPage());
			gridModel.setRecords(logisticReports.size());
			gridModel.setRows(pageData(filterDto,logisticReports));
		}

		request.setAttribute("table", gridModel);
		request.setAttribute("channel", erpOrdersService.selectchannelName());

		request.setAttribute("channelParam", channel);
		request.setAttribute("startTime", request.getParameter("startTime"));
		request.setAttribute("endTime", request.getParameter("endTime"));
		return "trackmore/logistics_report_channel";
	}

	@ResponseBody
	@RequestMapping(value = "/channel/kylin/get", method = RequestMethod.GET)
	public GridModel getkylinPageDataChannel(FilterDto filterDto, HttpServletRequest request) {
		LogisticReport logisticReport = new LogisticReport();
		String[] channel = request.getParameterValues("channel[]");
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}

		List<LogisticReport> logisticReports = iLogisticsReportService.getKylinReportDataGroupChannel(logisticReport);
		GridModel gridModel = new GridModel();
		if(logisticReports == null || logisticReports.size() <= 0){
			gridModel.setPagesize(filterDto.getRows());
			gridModel.setTotal(0);
			gridModel.setPage(filterDto.getPage());
			gridModel.setRecords(0);
			gridModel.setRows(null);
		}else if(filterDto.getRows() != 0&& filterDto.getPage() != 0){
			gridModel.setPagesize(filterDto.getRows());
			gridModel.setTotal(logisticReports.size()%filterDto.getRows() ==0?logisticReports.size()/filterDto.getRows():logisticReports.size()/filterDto.getRows() + 1);
			gridModel.setPage(filterDto.getPage());
			gridModel.setRecords(logisticReports.size());
			gridModel.setRows(pageData(filterDto,logisticReports));
		}else{
			gridModel.setRows(logisticReports);//导出
		}
		return gridModel;
	}

	@ResponseBody
	@RequestMapping(value = "/channel/get1", method = RequestMethod.GET)
	public GridModel getPageDataChannel1(FilterDto filterDto, HttpServletRequest request) {
		LogisticReport logisticReport = new LogisticReport();
		String[] channel = request.getParameterValues("channel[]");
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}

		PageHelper.startPage(filterDto.getPage(), filterDto.getRows(), true);
		List<LogisticReport> logisticReports = erpOrdersService.selectReportDataChannel(logisticReport);
		GridModel gridModel = new GridModel(new PageInfo<>(logisticReports));
		gridModel.setPagesize(filterDto.getRows());
		return gridModel;
	}

	@RequestMapping(value = "/channel/view1", method = RequestMethod.GET)
	public String channelView1(HttpServletRequest request,String[] channel) {
		LogisticReport logisticReport = new LogisticReport();
		if (channel != null && channel.length > 0) {
			logisticReport.setChannelName("'" + StringUtils.join(channel, "','") + "'");
		}
		String startTime = request.getParameter("startTime");
		if(startTime != null && startTime.trim().length() > 0){
			logisticReport.setStartTime(startTime);
		}
		String endTime = request.getParameter("endTime");
		if(endTime != null && endTime.trim().length() > 0){
			logisticReport.setEndTime(endTime);
		}

		PageHelper.startPage(1, 10, true);
		List<LogisticReport> logisticReports = erpOrdersService.selectReportDataChannel(logisticReport);
		request.setAttribute("table", new GridModel(new PageInfo<>(logisticReports)));
		request.setAttribute("channel", erpOrdersService.selectchannelName());

		request.setAttribute("channelParam", channel);
		request.setAttribute("startTime", request.getParameter("startTime"));
		request.setAttribute("endTime", request.getParameter("endTime"));
		return "trackmore/logistics_report_channel1";
	}


	@Autowired
	ErpOrdersProcessService erpOrdersProcessService;

	@ResponseBody
	@RequestMapping(value = "/channel/get1/redis", method = RequestMethod.GET)
	public Map<String,Object> getPageDataChannelRedis(String channel,String startTime,String endTime) {
		if (StringUtils.isEmpty(channel)) {
			channel = "139渠道";
		}
		if(StringUtils.isEmpty(startTime)){
			startTime = DateFormatUtils.format(new Date(System.currentTimeMillis() - 15*24*60*60*1000),"yyyy-MM-dd");
		}
		if(StringUtils.isEmpty(endTime)){
			endTime = DateFormatUtils.format(new Date(),"yyyy-MM-dd");
		}
		List<LogisticReport> redisReportData = erpOrdersProcessService.getRedisReportData(channel, startTime, endTime);
		Map<String,Object> map = new HashMap<>();
		map.put("nodes",statusService.selectByChannel(channel));
		map.put("values",redisReportData);
		return map;
	}

	@RequestMapping(value = "/channel/view1/redis", method = RequestMethod.GET)
	public String channelViewRedis(HttpServletRequest request,String channel,String startTime,String endTime) {
		if (StringUtils.isEmpty(channel)) {
			channel = "139渠道";
		}
		if(StringUtils.isEmpty(startTime)){
			startTime = DateFormatUtils.format(new Date(System.currentTimeMillis() - 15*24*60*60*1000),"yyyy-MM-dd");
		}
		if(StringUtils.isEmpty(endTime)){
			endTime = DateFormatUtils.format(new Date(),"yyyy-MM-dd");
		}

		List<LogisticReport> redisReportData = erpOrdersProcessService.getRedisReportData(channel, startTime, endTime);
		request.setAttribute("table", redisReportData);
		LogisticsChannel channel1 = new LogisticsChannel();
		channel1.setStatus(1);
		List<LogisticsChannel> logisticsChannels = logisticsChannelService.selectLogisticsChannels(channel1);
		List<String> channelName = new ArrayList<>();
		for(LogisticsChannel channel2:logisticsChannels){
			channelName.add(channel2.getYksChannelName());
		}
		request.setAttribute("channel", channelName);
		request.setAttribute("LogisticsStatus", statusService.selectByChannel(channel));

		request.setAttribute("channelParam", channel);
		request.setAttribute("startTime", startTime);
		request.setAttribute("endTime", endTime);
		return "trackmore/logistics_report_channel_redis";
	}

	private List<LogisticReport> pageData(FilterDto filterDto, List<LogisticReport> reports){
		List<LogisticReport>  logisticReports = new ArrayList<>();
		int start = 0;
		int end = 0;
		if(filterDto.getPage() == 1){
			start = 0;
			if(reports.size() < filterDto.getRows()){
				end = reports.size();
			}else{
				end = filterDto.getPage() * filterDto.getRows();
			}
		}else if(filterDto.getPage() == (reports.size()%filterDto.getRows() ==0?reports.size()/filterDto.getRows():reports.size()/filterDto.getRows() + 1)){
			start = (filterDto.getPage() - 1) * filterDto.getRows();
			end = reports.size();
		}else{
			start = (filterDto.getPage() - 1) * filterDto.getRows();
			end = filterDto.getPage() * filterDto.getRows();
		}
		for (int i=start;i<end;i++) {
			logisticReports.add(reports.get(i));
		}
		return logisticReports;
	}

}

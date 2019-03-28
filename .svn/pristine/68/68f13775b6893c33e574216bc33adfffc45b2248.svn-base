package com.yks.bigdata.service.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.yks.bigdata.common.Constants;
import com.yks.bigdata.util.EsJsonParseUtils;
import com.yks.bigdata.util.EsUtils;
import com.yks.bigdata.util.JdbcUtils;
import com.yks.bigdata.vo.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zh on 2017/6/22.
 * 测试 往es中添加order 信息数据
 */
@Service
public class BulkLoadOrdersService {


    @Autowired
    AccountService accountService;

    @Autowired
    ChannelService channelService;

    @Autowired
    PickUpService pickUpService;




    /**
     * 导入orders 到es中
     * @throws Exception
     */
    public void bulkLoadOrders()throws Exception{
        List<Orders> list = queryResult();
        bulkLoadOrders(list);
    }

    public boolean checkQuJian(List<PickUp> pickUpList)throws Exception{
        HashSet<Long> erpIdsSet = new HashSet<Long>();
        HashSet<String> yesSet  = new HashSet<String>();
        for (PickUp pickUp:pickUpList){
            erpIdsSet.add(pickUp.getErp_orders_id());
            yesSet.add(pickUp.getFreightcode());
        }
        Iterator<Long> erpIdIt = erpIdsSet.iterator();
        while (erpIdIt.hasNext()){
            Long id = erpIdIt.next();
            if (!exsitsOrder(id)){
                System.out.println("erp_orders_id 为 "+id +"在es中不存在!请核对之后再进行录入");
                return false;
            }
        }

        Iterator<String> yesIdIt = yesSet.iterator();
        while (yesIdIt.hasNext()){
            String id = yesIdIt.next();
            // TODO: 2017/6/28  
//            if (!exsitsOrder(id)){
//                System.out.println("erp_orders_id 为 "+id +"在es中不存在!请核对之后再进行录入");
//                return false;
//            }
        }

        return true;

    }

    /**
     * 将 取件表中的 orders id 进行更新
     *  同时产生 对应的account和物流渠道表 channel
     *   标记状态为
     * @param pickUpList
     * @throws Exception
     */
    public void processOrders(List<PickUp> pickUpList)throws Exception{
        StringBuilder sb =new StringBuilder();
        int count=0;
        for (int i=0;i<pickUpList.size();i++){
            String updateStr = updateOrderJsonStr(pickUpList.get(i));
            sb.append(updateStr);
            count++;
            if (i % 10000 == 0 && i != 0) {
                count=0;
                batchUpdateOrders(sb.toString());
                sb = new StringBuilder();
                //账号额度变动1W
                accountService.increaseAccountQuota();
            }
        }
        if (StringUtils.isNotEmpty(sb.toString())){
            batchUpdateOrders(sb.toString());
            //账号额度变动
            accountService.increaseAccountQuota(count);
        }
    }

    /**
     * 使用rest api bulk的方式,将数据更新es中
     * @param json
     */
    private void batchUpdateOrders(String json)throws Exception{
        String url = Constants.full_index_type_erp_orders+"/"+Constants.bulk;
        EsUtils.post(url,json);
    }

    /**
     * 依据 pickup 更新 orders中的accountinner channel track_status
     * @param pickUp
     */
    public String updateOrderJsonStr(PickUp pickUp)throws Exception{

        //更新的id
        String id = queryOrderIdByOrdersErpID(pickUp.getErp_orders_id());

        //组织json
        String updateJson = organizationAccountAndChannel(pickUp);

        StringBuilder resultStr=new StringBuilder("{\"update\":{\"_id\":\""+id+"\"}}\n");
        resultStr.append(updateJson);
        return resultStr.toString();
    }


    /**
     * 判断 order是否存在
     * @param erp_orders_id
     * @return
     * @throws Exception
     */
    public boolean exsitsOrder(Long erp_orders_id)throws Exception{
        String id = queryOrderIdByOrdersErpID(erp_orders_id);
        if (id == null )
            return false;
        return true;
    }

    /**
     * 获取 es中 erp_orders_id对应的_id
     * @param erp_orders_id
     * @return
     */
    private String queryOrderIdByOrdersErpID(Long erp_orders_id)throws Exception{
        String url = Constants.full_index_type_erp_orders+"/"+Constants.search;
        String json = "{  \"query\": {    \"match\": {      \"erp_orders_id\": "+erp_orders_id+"    }  }}";
        String response = EsUtils.get(url, json);
        JSONArray hits = EsJsonParseUtils.parseHits(response, "在es上未查询到 erp_orders_id 为" + erp_orders_id + " 的数据");
        return (String)hits.getJSONObject(0).get("_id");
    }


    /**
     * 获取这个
     * {"doc": {"accountinner":{"id":"AVzscXEDZhTtqpKq2ush","account": "1107473538@qq.com","api_key": "c7cdeb1d-7fa2-4fe2-97a6-73a7c64768a7","create_time": 1498615476035,"data_count": 10000,"status": 1}}}
     * @param pickUp
     * @return
     */
    public String organizationAccountAndChannel(PickUp pickUp)throws Exception{
        Trackmore_account account = accountService.getAvailableEsAccount();
        Logistics_channel channel = channelService.getAvailableChannel();
        String accountJsonStr = JSON.toJSONString(account);
        String channelJsonStr = JSON.toJSONString(channel);
        StringBuilder sb = new StringBuilder();
        sb.append("{\"doc\": {");
        sb.append("\"accountinner\":").append(accountJsonStr).append(",");
        sb.append("\"chanel\":").append(channelJsonStr).append(",");
        sb.append("\"track_status\":11").append(",");
        sb.append("\"branchLogistics\":").append("\""+pickUp.getPath()+"\"");
        sb.append("}}\n");
        return sb.toString();
    }




    /**
     * jdbc 获取orders信息
     *
     * @return
     * @throws Exception
     */
    public List<Orders> queryResult() throws Exception {
        QueryRunner queryRunner = new QueryRunner();
        String sql = "select erp_orders_id,warehouseid,buyer_city,buyer_state,buyer_country," +
                "shipping_method,sales_account,orders_type,orders_status,orders_export_time," +
                "fee,heavi,size,orders_out_time,orders_mail_code," +
                "orders_mail_time,erp_post_office,orders_print_time,ebay_counycode,freightcode," +
                "yksid from erp_orders where orders_status=5 limit 100000";
        return (List<Orders>) queryRunner.query(JdbcUtils.getConnection(), sql, new BeanListHandler(Orders.class));
    }

    /**
     * 向order中添加account信息
     *
     * @param order
     * @throws Exception
     */
    private void addAccount(Orders order) throws Exception {
        order.setAccountinner(accountService.getAccount());
    }




    /**
     * 批量往es中导入orders数据
     *
     * @param orders
     * @throws Exception
     */
    public void bulkLoadOrders(List<Orders> orders) throws Exception {
        String url = Constants.full_index_type_erp_orders + "/"+Constants.bulk;
        Integer count = orders.size();
        String index = Constants.index_erp;
        String type = Constants.type_erp_orders;
        int cache = 0;
        //account = getAvailableEsAccount();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            Orders order = orders.get(i);
            //初始状态
            order.setTrack_status(0);
            //addAccount(order);

            String json = JSON.toJSONString(order);

            sb.append("{ \"index\" : {} }\n");
            sb.append(json);
            sb.append("\n");

            cache++;
            // 每10000条提交一次
            if (i % 10000 == 0 && i != 0) {
                cache = 0;
                System.out.println("begin");
                EsUtils.post(url, sb.toString());
                sb = new StringBuilder();
                //increaseAccountQuota();
                //account = getAvailableEsAccount();
                System.out.println("end");
            }
        }
        if (cache != 0) {
            EsUtils.post(url, sb.toString());
            //increaseAccountQuota(cache);
        }

    }


}

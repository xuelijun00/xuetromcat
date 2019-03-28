package com.yks.bigdata.service.es;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yks.bigdata.common.Constants;
import com.yks.bigdata.exception.UpdateExeption;
import com.yks.bigdata.util.EsJsonParseUtils;
import com.yks.bigdata.util.EsUtils;
import com.yks.bigdata.vo.Trackmore_account;
import com.yks.bigdata.vo.Trackmore_accountPut;
import org.springframework.stereotype.Service;


/**
 * Created by zh on 2017/6/26.
 * <p>
 * 测试添加track more账号
 */
@Service
public class AccountService {

    public static void main(String[] args) throws Exception {
        String url = Constants.full_index_type_account;
        String s = JSON.toJSONString(new Trackmore_accountPut("1107473538@qq.com", "c7cdeb1d-7fa2-4fe2-97a6-73a7c64768a7", System.currentTimeMillis(), 0, 1));
        EsUtils.post(url, s);
    }


    private Integer accountVersion;
    private Trackmore_account account;

    public Integer getAccountVersion() {
        return accountVersion;
    }

    public void setAccountVersion(Integer accountVersion) {
        this.accountVersion = accountVersion;
    }

    public Trackmore_account getAccount() {
        return account;
    }

    public void setAccount(Trackmore_account account) {
        this.account = account;
    }

    /**
     * es中的account 用户额度增加sum数值
     *
     * @param sum
     * @throws Exception
     */
    public void increaseAccountQuota(Integer sum) throws Exception {
        String url = Constants.full_index_type_account + "/" + account.getId() + "/_update?version=" + accountVersion;
        int dataCount = account.getData_count() + sum;
        if (dataCount > 1500000) {
            System.out.printf("account额度异常,出现大于150W的数据");
            return;
        }
        String json = "{  \"doc\": {    \"data_count\":" + dataCount + "  }}";
        String respons = EsUtils.post(url, json);
        if (respons.contains("error")) {
            throw new UpdateExeption(url);
        }
        EsUtils.handleReflashIndex(Constants.index_erp);
    }

    /**
     * 对trace account账号额度新增1W
     */
    public void increaseAccountQuota() throws Exception {
        increaseAccountQuota(10000);
    }

    /**
     * 从es中获取可用的esacccount
     *
     * @return
     * @throws Exception
     */
    public Trackmore_account getAvailableEsAccount() throws Exception {
        String url = Constants.full_index_type_account + "/_search";
        String json = "{  \"query\": {    \"range\": {      \"data_count\": {        \"gte\": 0,        \"lte\": 1490000      }    }  },\"version\": true}";
        String response = EsUtils.get(url, json);

        String errorMsg = "可用account账号数目不足";
        JSONArray hits = EsJsonParseUtils.parseHits(response, errorMsg);

        int size = hits.size();
        for (int i = 0; i < size; i++) {
            JSONObject hit = hits.getJSONObject(i);

            JSONObject source = hit.getJSONObject("_source");
            Integer data_count = (Integer) source.get("data_count");

            accountVersion = (Integer) hit.get("_version");
            System.out.println("change version " + accountVersion);
            account = new Trackmore_account((String) hit.get("_id"), (String) source.get("account"), (String) source.get("api_key"), (Long) source.get("create_time"), data_count, (Integer) source.get("status"));
            return account;
        }
        return null;
    }

}

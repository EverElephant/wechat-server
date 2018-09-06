package com.kodgames.gmtools;

import com.kodgames.gmtools.dao.wechat.WechatDataDao;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping(value = "html/wechat")
public class WechatController {
    @Resource
    private WechatDataDao wechatDataDao;

    @PostMapping(value = "compare_order.do")
    public HashMap<String,Object>  compareOrder(MultipartFile order_list, Date start, Date end) {
        HashMap<String, Object> result  = new HashMap<>();
        if (order_list == null) {
            result.put("result", -2);
            return result;
        }

        int wechatOrderNum = 0;
        int financeOrderNum = 0;
        double wechatRmbCount = 0;
        double financeRmbCount = 0;
        int wechatOrderError = 0;
        int financeOrderError = 0;
        double wechatRmbError = 0;
        double financeRmbError = 0;
        HashMap<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        List<HashMap<String, Object>> dbOrders = wechatDataDao.queryWechatOrder(params);
        wechatOrderNum = dbOrders.size();
        List<HashMap<String, String>> compareResultList = new ArrayList<>();
        HashMap<String, HashMap<String, Object>> compareMap = new HashMap<>();
        for (HashMap<String, Object> order : dbOrders) {
            compareMap.put(order.get("billing_order_id").toString()+"|"+order.get("update_status").toString(), order);
            wechatRmbCount += (double) order.get("rmb");
        }
        try {
            List<String> lines = IOUtils.readLines(order_list.getInputStream(), "GBK");
            financeOrderNum = lines.size() - 1;
            int timeIndex = -1;
            int channelOrderIdIndex = -1;
            int orderIdIndex = -1;
            int orderTypeIndex = -1;
            int rmbIndex = -1;
            boolean isFirst = true;
            for (String line : lines) {
                String[] cells = line.split(",");
                if (isFirst) {
                    for (int i = 0; i < cells.length; i++) {
                        if (StringUtils.contains(cells[i], "记账时间")) {
                            timeIndex = i;
                        }else if (StringUtils.contains(cells[i], "微信支付业务单号")) {
                            channelOrderIdIndex = i;
                        }if (StringUtils.contains(cells[i], "业务凭证号")) {
                            orderIdIndex = i;
                        }if (StringUtils.contains(cells[i], "收支金额(元)")) {
                            rmbIndex = i;
                        }if (StringUtils.contains(cells[i], "收支类型")) {
                            orderTypeIndex = i;
                        }
                    }
                    if (timeIndex == -1 || channelOrderIdIndex == -1 || orderIdIndex == -1 || rmbIndex == -1||orderTypeIndex == -1) {
                        result.put("result", 1);
                        result.put("data", "-3");               //文件不符合规范
                        return result;
                    }
                    isFirst = false;
                }else {
                    String orderId = cells[orderIdIndex].replaceAll("红包订单号：","").trim();
                    String orderType = cells[orderTypeIndex].replaceAll("支出","4").replaceAll("收入","6").trim();
                    String channelOrderId = cells[channelOrderIdIndex].replaceAll("\"","").replaceAll("`","").replaceAll("'","").trim();
                    double rmb = Math.abs(Double.parseDouble(cells[rmbIndex].trim()));
                    String time = cells[timeIndex].replaceAll("\"","").replaceAll("/","-").trim();
                    financeRmbCount += rmb;

                    HashMap<String, Object> order = compareMap.get(orderId+"|"+orderType);
                    if (order == null) {
                        //数据库中没有的,加入到返回结果中
                        financeOrderError += 1;
                        financeRmbError += rmb;
                        HashMap<String, String> compareResult = new LinkedHashMap<>();
                        compareResult.put("id", (compareResultList.size() + 1) + "");
                        compareResult.put("time", time);
                        compareResult.put("channel_order_id", channelOrderId);
                        compareResult.put("order_id", orderId);
                        compareResult.put("rmb", Double.toString(rmb));
                        compareResult.put("area", getAreaNameByOrderId(orderId));
                        compareResult.put("order_type", orderType);
                        compareResult.put("note", "财务账单多出");
                        compareResultList.add(compareResult);
                    }else if (!order.get("channel_order_id").equals(channelOrderId) || Double.parseDouble(order.get("rmb").toString()) != rmb) {
                        financeOrderError += 1;
                        financeRmbError += rmb;

                        wechatOrderError += 1;
                        wechatRmbError += Double.parseDouble(order.get("rmb").toString());
                        //异常情况
                        HashMap<String, String> compareResult = new LinkedHashMap<>();
                        compareResult.put("id", (compareResultList.size() + 1) + "");
                        compareResult.put("time", time);
                        compareResult.put("channel_order_id", channelOrderId);
                        compareResult.put("order_id", orderId);
                        compareResult.put("rmb", Double.toString(rmb));
                        compareResult.put("area", getAreaNameByOrderId(orderId));
                        compareResult.put("order_type", orderType);
                        compareResult.put("note", "财务账单差异");
                        compareResultList.add(compareResult);

                        HashMap<String, String> compareResult2 = new LinkedHashMap<>();
                        compareResult2.put("id", (compareResultList.size() + 1) + "");
                        compareResult2.put("time", new DateTime(order.get("update_time")).toString("yyyy-MM-dd HH:mm:ss"));
                        compareResult2.put("channel_order_id", order.get("channel_order_id").toString());
                        compareResult2.put("order_id", orderId);
                        compareResult2.put("rmb", order.get("rmb").toString());
                        compareResult2.put("area", getAreaNameByOrderId(orderId));
                        compareResult.put("order_type", orderType);
                        compareResult2.put("note", "GMT账单差异");
                        compareResultList.add(compareResult2);
                    }
                    compareMap.remove(orderId+"|"+orderType);
                }
            }

            for (Map.Entry<String, HashMap<String, Object>> entry : compareMap.entrySet()) {
                //只有数据库拥有
                wechatOrderError += 1;
                wechatRmbError += Double.parseDouble(entry.getValue().get("rmb").toString());
                HashMap<String, String> compareResult = new LinkedHashMap<>();
                compareResult.put("id", (compareResultList.size() + 1) + "");
                compareResult.put("time",new DateTime(entry.getValue().get("update_time")).toString("yyyy-MM-dd HH:mm:ss"));
                compareResult.put("channel_order_id", entry.getValue().get("channel_order_id").toString());
                compareResult.put("order_id", entry.getKey().substring(0,entry.getKey().length()-2));
                compareResult.put("rmb", entry.getValue().get("rmb").toString());
                compareResult.put("area", getAreaNameByOrderId(entry.getKey()));
                compareResult.put("order_type", entry.getValue().get("update_status").toString());
                compareResult.put("note", "GMT账单多出");
                compareResultList.add(compareResult);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        result.put("result",1);
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> count = new HashMap<>();
        count.put("wechat_order_count", wechatOrderNum);
        count.put("wechat_rmb_count", wechatRmbCount);
        count.put("wechat_order_error", wechatOrderError);
        count.put("wechat_rmb_error", wechatRmbError);
        count.put("finance_order_count", financeOrderNum);
        count.put("finance_rmb_count", financeRmbCount);
        count.put("finance_order_error", financeOrderError);
        count.put("finance_rmb_error", financeRmbError);
        data.put("list", compareResultList);
        data.put("count", count);
        result.put("data", data);
        return result;
    }
    String getAreaNameByOrderId(String orderId){
        return "贵阳";
    }
}

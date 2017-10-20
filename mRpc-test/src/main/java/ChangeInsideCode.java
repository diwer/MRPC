import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.SkuReadService.Sku;
import com.jd.open.api.sdk.request.ware.SkuReadSearchSkuListRequest;
import com.jd.open.api.sdk.request.ware.WareSkuUpdateRequest;
import com.jd.open.api.sdk.response.ware.SkuReadSearchSkuListResponse;
import com.jd.open.api.sdk.response.ware.WareSkuUpdateResponse;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author ming
 * @date
 */
public class ChangeInsideCode {
    private static String prefix = "处理失败";
    private static Logger logger = Logger.getLogger(ChangeInsideCode.class.toString());
    private static String jdurl = "https://api.jd.com/routerjson";
    private DefaultJdClient client;

    public ChangeInsideCode(String appkey, String appsercet, String sessionkey) {
        client = new DefaultJdClient(jdurl, sessionkey, appkey, appsercet);
    }

    public List<DealItem> items;
    WareSkuUpdateRequest updateRequest = new WareSkuUpdateRequest();
    SkuReadSearchSkuListRequest getSkusRequest = new SkuReadSearchSkuListRequest();

    public Map<String, List<Sku>> wareSkus = new HashMap<String, List<Sku>>();
    private static String trueCode = "0";

    private void getWareSkus() {
        try {
            getSkusRequest.setField("wareId,skuId,status,saleAttrs,jdPrice,outerId,barCode,categoryId,imgTag,logo,skuName,stockNum,wareTitle,modified,created");
            for (DealItem item : items) {
                SkuReadSearchSkuListResponse rsp;
                getSkusRequest.setWareId(item.getNumId());
                int count = 0;
                do {
                    rsp = client.execute(getSkusRequest);

                    if (!rsp.getCode().equals(trueCode)) {
                        logger.warning("为查询到结果");

                    }
                    count++;
                } while ((rsp.getPage() == null || rsp.getPage().getData() == null) && count < 3);
                List<Sku> skus = rsp.getPage().getData();
                for (Sku sku : skus) {
                    String wareId = sku.getWareId().toString();
                    if (wareSkus.containsKey(wareId)) {
                        if (!StringUtils.isEmpty(sku.getOuterId())) {
                            List<Sku> list = wareSkus.get(wareId);
                            list.add(sku);
                        }
                    } else {
                        if (!StringUtils.isEmpty(sku.getOuterId())) {
                            List<Sku> list = new ArrayList<Sku>();
                            list.add(sku);
                            wareSkus.put(wareId, list);
                        }
                    }
                }

            }
        } catch (JdException e) {
            logger.warning(e.getErrMsg());
            for (DealItem item : items) {
                item.setType(4);
                logger.warning(prefix + item.getNumId());
            }
        } catch (Exception ex) {
            logger.warning(ex.getMessage());
        }


    }

    public void deal() {
        getWareSkus();
        logger.info("-------------begin");
        for (DealItem item : items) {
            if (item.getType() == 4) {
                continue;
            }
            try {
                List<Sku> skuList = new ArrayList<Sku>();
                updateRequest.setWareId(item.getNumId());
                List<Sku> list = wareSkus.get(item.getNumId());
                Map<String, Sku> skuMap = new HashMap<String, Sku>(400);
                for (Sku sku : list) {
                    if (!skuMap.containsKey(sku.getSkuId())) {
                        skuMap.put(sku.getSkuId().toString(), sku);
                    }
                }
                for (Map.Entry<String, String> sku : item.getInsideDict().entrySet()) {
                    if (skuMap.containsKey(sku.getKey())) {
                        updateRequest.setOuterId(sku.getValue());
                        updateRequest.setSkuId(sku.getKey());
                        updateRequest.setJdPrice(skuMap.get(sku.getKey()).getJdPrice().toString());
                        updateRequest.setStockNum(skuMap.get(sku.getKey()).getStockNum().toString());
                        Thread.sleep(100);
                        WareSkuUpdateResponse rsp = client.execute(updateRequest);
                        if (!rsp.getCode().equals(trueCode)) {
                            item.setType(4);
                            logger.warning(prefix + item.getNumId());
                            break;
                        }
                    } else {
                        logger.warning("不存在该条码" + sku.getKey());
                    }
                }
            } catch (Exception ex) {
                logger.warning(prefix + item.getNumId());
                logger.warning(ex.getMessage());
                continue;
            }
            logger.info("SUCCESS:" + item.getNumId());
        }

        logger.info("-sudo xcodebuild -license------------end");
    }


    public List<DealItem> getItems() {
        return items;
    }

    public void setItems(List<DealItem> items) {
        this.items = items;
    }
}

    
    
    
    
    
    
    
    
    
    
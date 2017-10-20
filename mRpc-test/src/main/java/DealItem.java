import java.util.HashMap;
import java.util.Map;

public class DealItem {
    public String shopName;
    public String numId;
    public String failReason;
    public int type;// 0 未处理 1 success, 2 淘宝无商品 3 sku信息 不全 4 处理过程中失败
    public Map<String, String> insideDict = new HashMap<String, String>();

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getNumId() {
        return numId;
    }

    public void setNumId(String numId) {
        this.numId = numId;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Map<String, String> getInsideDict() {
        return insideDict;
    }

    public void setInsideDict(Map<String, String> insideDict) {
        this.insideDict = insideDict;
    }


}

    
    
    
    
    
    
    
    
    
    
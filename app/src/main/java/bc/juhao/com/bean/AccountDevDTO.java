package bc.juhao.com.bean;

/**
 * Created by gamekonglee on 2018/7/30.
 */

public class AccountDevDTO {
    String iotId;
    String productKey;
    String name;
    String deviceName;
    String productImage;
    String productModel;
    String categoryImage;
    String nickName;
    String type;
    String thingType;
    String nodeType;
    String  status;
    Byte owned;
    String identityAlias;
    String gmtModified;

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AccountDevDTO(String iotId, String productKey, String name, String deviceName, String type, String status) {
        this.iotId = iotId;
        this.productKey = productKey;
        this.name = name;
        this.deviceName = deviceName;
        this.type = type;
        this.status = status;
    }
}

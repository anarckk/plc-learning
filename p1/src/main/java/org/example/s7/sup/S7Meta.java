package org.example.s7.sup;

/**
 * Created by fh on 2020/11/9
 * plc 数据的元地址
 */
public class S7Meta {
    /**
     * ip地址
     */
    protected String host;
    /**
     * 数据地址
     */
    protected String address;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

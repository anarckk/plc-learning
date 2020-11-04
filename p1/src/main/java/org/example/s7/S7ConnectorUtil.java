package org.example.s7;

/**
 * Created by fh on 2020/11/4
 */
public class S7ConnectorUtil {
    /**
     * %DB100.DBX3.0
     * 地址转换
     *
     * @param address 地址
     * @return [DB, DBX, bit偏移]
     */
    public static int[] addressConvert(String address) {
        String addressCopy = address.toUpperCase();
        int[] r = new int[3];
        String db = addressCopy.substring(addressCopy.indexOf("DB") + 2, addressCopy.indexOf("."));
        String byteOffset = addressCopy.substring(addressCopy.indexOf("DBX") + 3, addressCopy.lastIndexOf("."));
        String bitOffset = addressCopy.substring(addressCopy.lastIndexOf(".") + 1);
        r[0] = Integer.parseInt(db);
        r[1] = Integer.parseInt(byteOffset);
        r[2] = Integer.parseInt(bitOffset);
        return r;
    }
}

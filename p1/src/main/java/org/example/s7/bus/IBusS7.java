package org.example.s7.bus;

import org.example.s7.device.IS7Device;

/**
 * Created by fh on 2020/11/4
 * business s7
 * 职责
 * 1. 进行逻辑地址的转换，%DB1.DBX0.1 转换成底层地址
 * 2. 缓存数据，将字节缓存下来，下次直接从已缓存的数据中读取字节偏移位上的值
 */
public interface IBusS7 {
    /**
     * 获得持有的底层设备对象
     */
    IS7Device getDevice();
}

package org.example.s7.bus;

import org.example.s7.device.IS7Device;
import org.example.s7.device.S7DeviceException;

/**
 * Created by fh on 2020/11/4
 * business s7
 * 职责
 * 1. 进行逻辑地址的转换，%DB1.DBX0.1 转换成底层地址
 * 2. 缓存数据，将字节缓存下来，下次直接从已缓存的数据中读取字节偏移位上的值
 */
public interface IS7BusDevice {
    /**
     * 连接
     *
     * @throws S7DeviceException 连接异常
     */
    void connect() throws S7DeviceException;

    /**
     * 断开连接
     *
     * @throws S7DeviceException 抛出异常
     */
    void disconnect() throws S7DeviceException;

    /**
     * 获得持有的底层设备对象
     */
    IS7Device getDevice();

    /**
     * 清理缓存
     */
    void clearCache();

    /**
     * 读取plc内存字节, 8bit
     *
     * 在一次查询中会缓存DBX过程中取得的字节数据，下一次将直接访问缓存
     *
     * @param address 例子：DB1.DBX18.0
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    byte readDBX(S7Address address) throws S7DeviceException;

    /**
     * 读取plc内存字节, 8bit
     *
     * @param address 例子：DB1.DBB19
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    byte readDBB(S7Address address) throws S7DeviceException;

    /**
     * 读取plc内存字节, 16bit
     *
     * @param address 例子：DB1.DBW0
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    short readDBW(S7Address address) throws S7DeviceException;

    /**
     * 读取plc内存字节, 32bit
     *
     * @param address 例子：DB1.DBD6
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    int readDBD(S7Address address) throws S7DeviceException;
}

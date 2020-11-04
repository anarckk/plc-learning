package org.example.s7.device;

/**
 * Created by fh on 2020/11/4
 */
public interface IS7DeviceMethods {
    /**
     * 读取plc内存字节, 8bit
     *
     * @param area       DB块地址
     * @param byteOffset 字节偏移量
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    byte readDBB(int area, int byteOffset) throws S7DeviceException;

    /**
     * 读取plc内存字节, 16bit
     *
     * @param area       DB块地址
     * @param byteOffset 字节偏移量
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    short readDBW(int area, int byteOffset) throws S7DeviceException;

    /**
     * 读取plc内存字节, 32bit
     *
     * @param area       DB块地址
     * @param byteOffset 字节偏移量
     * @return 数值
     * @throws S7DeviceException 读取异常
     */
    int readDBD(int area, int byteOffset) throws S7DeviceException;
}

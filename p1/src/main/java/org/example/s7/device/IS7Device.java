package org.example.s7.device;

import com.github.s7connector.api.S7Connector;

/**
 * Created by fh on 2020/11/4
 * siemens s7 connector interface
 * 负责持有连接对象，在需要时自动的启动连接，由外界手动的进行关闭。
 * 在构造本接口时指定好主机地址，在整个生命周期中不再改变。
 */
public interface IS7Device extends IS7DeviceMethods {
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
     * 本对象对应的主机地址
     * @return eg: 192.168.10.101
     */
    String getHost();

    /**
     *
     * @return get is connected.
     */
    boolean isConnected();
}

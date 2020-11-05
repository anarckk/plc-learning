package org.example.s7.device;

import com.github.s7connector.api.DaveArea;
import com.github.s7connector.api.S7Connector;
import com.github.s7connector.api.factory.S7ConnectorFactory;
import com.github.s7connector.exception.S7Exception;

import java.io.IOException;

/**
 * Created by fh on 2020/11/4
 */
public class S7Device implements IS7Device {
    private String host;
    private boolean isConnected;
    private S7Connector connector;

    public S7Device(String host) {
        this.host = host;
    }

    @Override
    public void connect() throws S7DeviceException {
        try {
            connector = S7ConnectorFactory
                    .buildTCPConnector()
                    .withHost(host)
                    .build();
            isConnected = true;
        } catch (S7Exception e) {
            throw new S7DeviceException(String.format("ip:%s plc连接不上！", host), e);
        }
    }

    @Override
    public void disconnect() throws S7DeviceException {
        if (connector == null) return;
        try {
            connector.close();
            connector = null;
            isConnected = false;
        } catch (IOException e) {
            throw new S7DeviceException(String.format("ip:%s 关闭Plc连接失败！", host), e);
        }
    }

    @Override
    public S7Connector getConnector() throws S7DeviceException {
        if (!isConnected) connect();
        return connector;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public boolean isConnected() {
        return isConnected;
    }

    @Override
    public byte readDBB(int area, int byteOffset) throws S7DeviceException {
        if (isConnected) {
            try {
                byte[] bs = connector.read(DaveArea.DB, area, 1, byteOffset);
                return bs[0];
            } catch (Exception e) {
                throw new S7DeviceException(String.format("ip:%s areaNumber:%d offset:%d 请求失败！", host, area, byteOffset), e);
            }
        } else {
            throw new S7DeviceException(String.format("ip:%s plc还未连接！", host));
        }
    }

    @Override
    public short readDBW(int area, int byteOffset) throws S7DeviceException {
        if (isConnected) {
            try {
                byte[] bs = connector.read(DaveArea.DB, area, 2, byteOffset);
                short r = (short) ((bs[0] & 0xff) << 8);
                r = (short) (r | bs[1]);
                return r;
            } catch (Exception e) {
                throw new S7DeviceException(String.format("ip:%s areaNumber:%d offset:%d 请求失败！", host, area, byteOffset), e);
            }
        } else {
            throw new S7DeviceException(String.format("ip:%s plc还未连接！", host));
        }
    }

    @Override
    public int readDBD(int area, int byteOffset) throws S7DeviceException {
        if (isConnected) {
            try {
                byte[] bs = connector.read(DaveArea.DB, area, 4, byteOffset);
                int r = (bs[0] & 0xff) << 24;
                r = r | ((bs[1] & 0xff) << 16);
                r = r | ((bs[2] & 0xff) << 8);
                r = r | bs[3];
                return r;
            } catch (Exception e) {
                throw new S7DeviceException(String.format("ip:%s areaNumber:%d offset:%d 请求失败！", host, area, byteOffset), e);
            }
        } else {
            throw new S7DeviceException(String.format("ip:%s plc还未连接！", host));
        }
    }
}

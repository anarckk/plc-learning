package org.example.s7.sup;

import org.example.s7.bus.IS7BusDevice;
import org.example.s7.bus.S7BusDevice;
import org.example.s7.device.IS7Device;
import org.example.s7.device.S7Device;
import org.example.s7.device.S7DeviceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by fh on 2020/11/9
 */
public class S7ConnectHolder {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    /**
     * connection holder
     * [the String of ip address, the connect of plc]
     */
    private Map<String, IS7BusDevice> holder = new ConcurrentHashMap<>();
    /**
     * 记录已经本次被使用过的连接集合
     */
    private Set<String> usedRecord = null;
    // holder的写锁，如果已经有线程在发起连接了，下一个线程就不要重复发起连接了
    private ReentrantLock holderLock = new ReentrantLock();

    /**
     * 查询开始
     */
    public void start() {
        usedRecord = new CopyOnWriteArraySet<>();
    }

    /**
     * 获取plc的连接，如果连接不存在则进行连接，如果连接不上，则报告异常！
     *
     * @param ipAddress ip地址
     * @return plc的业务连接对象
     * @throws S7DeviceException 如果连接不上，则报告此异常
     */
    public IS7BusDevice getBusDevice(String ipAddress) throws S7DeviceException {
        usedRecord.add(ipAddress);
        IS7BusDevice busDevice;
        if (holder.containsKey(ipAddress)) {
            busDevice = holder.get(ipAddress);
        } else {
            holderLock.lock();
            try {
                if (holder.containsKey(ipAddress)) { // 假如是等待线程，那么上一个线程可能就已经将连接建立好了，我这边获取就行了
                    busDevice = holder.get(ipAddress);
                } else {
                    IS7Device device = new S7Device(ipAddress);
                    busDevice = new S7BusDevice(device);
                    busDevice.connect();
                    holder.put(ipAddress, busDevice);
                }
            } finally {
                holderLock.unlock();
            }
        }
        return busDevice;
    }

    /**
     * 查询结束
     */
    public void finish() {
        holder.entrySet()
                .stream()
                .filter(entry -> usedRecord.stream().noneMatch(usedIp -> Objects.equals(usedIp, entry.getKey())))
                .forEach(entry -> {
                    String ip = entry.getKey();
                    IS7BusDevice s7 = entry.getValue();
                    try {
                        s7.disconnect();
                        LOGGER.info("【{}】连接未使用，已断开", ip);
                    } catch (S7DeviceException e) {
                        LOGGER.error("【{}】断开连接失败", ip, e);
                    }
                    holder.remove(ip);
                });
        usedRecord = null;
    }
}

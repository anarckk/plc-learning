package org.example.s7.sup;

import org.example.s7.bus.IS7BusDevice;
import org.example.s7.bus.S7Address;
import org.example.s7.device.S7DeviceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by fh on 2020/11/9
 * 通过ip分组+线程池轮询的方式
 */
public class S7IpGroupQueryer implements IS7Queryer {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private S7ConnectHolder holder = new S7ConnectHolder();
    // 创建一个固定大小的线程池:
    private final ExecutorService es;

    public S7IpGroupQueryer() {
        es = Executors.newFixedThreadPool(8);
    }

    public S7IpGroupQueryer(int nThreads) {
        es = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * 通过ip分组+线程池轮询的方式
     *
     * @param s7Metas 要查询的s7元地址集合
     * @return 查询结果
     */
    public List<PlcModel> query(List<S7Meta> s7Metas) {
        List<Callable<List<PlcModel>>> callableList = new ArrayList<>();
        // 通过ip分组
        Map<String, List<S7Meta>> ipMap = new HashMap<>();
        for (S7Meta s7Meta : s7Metas) {
            String host = s7Meta.getHost();
            if (ipMap.containsKey(host)) {
                ipMap.get(host).add(s7Meta);
            } else {
                List<S7Meta> list = new ArrayList<>();
                list.add(s7Meta);
                ipMap.put(host, list);
            }
        }
        Set<Map.Entry<String, List<S7Meta>>> set = ipMap.entrySet();
        for (Map.Entry<String, List<S7Meta>> entry : set) {
            String ip = entry.getKey();
            List<S7Meta> list = entry.getValue();
            callableList.add(() -> {
                IS7BusDevice s7;
                try {
                    s7 = holder.getBusDevice(ip);
                } catch (S7DeviceException e) {
                    LOGGER.error("ip: [{}] 连接失败！", ip, e);
                    return list.stream().map(s7Meta -> {
                        PlcModel plcModel = new PlcModel();
                        plcModel.setPlcMeta(s7Meta);
                        plcModel.setValue(null);
                        return plcModel;
                    }).collect(Collectors.toList());
                }
                List<PlcModel> plcModelList = new ArrayList<>();
                for (S7Meta s7Meta : list) //noinspection DuplicatedCode
                {
                    String address = s7Meta.getAddress();
                    PlcModel plcModel = new PlcModel();
                    plcModel.setPlcMeta(s7Meta);
                    try {
                        S7Address s7Address = new S7Address(address);
                        Object result = null;
                        switch (s7Address.getType()) {
                            case BIT:
                                result = s7.readDBX(s7Address);
                                break;
                            case BYTE:
                                result = s7.readDBB(s7Address);
                                break;
                            case WORD:
                                result = s7.readDBW(s7Address);
                                break;
                            case DWORD:
                                result = s7.readDBD(s7Address);
                                break;
                            default:
                                break;
                        }
                        plcModel.setValue(result);
                    } catch (Exception e) {
                        LOGGER.error("ip: [{}], address: [{}] 查询报错！", ip, address, e);
                        plcModel.setValue(null);
                    }
                    plcModelList.add(plcModel);
                }
                return plcModelList;
            });
        }
        List<PlcModel> results = new ArrayList<>();
        holder.start();
        try {
            List<Future<List<PlcModel>>> futures = es.invokeAll(callableList);
            for (Future<List<PlcModel>> future : futures) {
                List<PlcModel> model = future.get();
                results.addAll(model);
            }
        } catch (Exception e) {
            LOGGER.error("线程池执行出错!", e);
        }
        holder.finish();
        return results;
    }

    @Override
    public void shutdown() {
        es.shutdown();
    }
}

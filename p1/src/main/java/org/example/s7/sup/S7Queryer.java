package org.example.s7.sup;

import org.example.s7.bus.IS7BusDevice;
import org.example.s7.bus.S7Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by fh on 2020/11/9
 */
public class S7Queryer {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private S7ConnectHolder holder = new S7ConnectHolder();
    // 创建一个固定大小的线程池:
    private final ExecutorService es = Executors.newFixedThreadPool(8);

    public static class PlcModel {
        private S7Meta plcMeta;
        private Object value;

        public S7Meta getPlcMeta() {
            return plcMeta;
        }

        public void setPlcMeta(S7Meta plcMeta) {
            this.plcMeta = plcMeta;
        }

        public Object getValue() {
            return value;
        }

        public void setValue(Object value) {
            this.value = value;
        }
    }

    /**
     * 使用多线程的方式请求plc的数据
     *
     * @param supplier 要查询的s7元地址集合
     * @return 查询结果
     */
    public List<PlcModel> query(Supplier<List<S7Meta>> supplier) {
        List<S7Meta> waitQuery = supplier.get();
        List<PlcModel> results = null;
        holder.start();
        try {
            List<Future<PlcModel>> futures = es.invokeAll(waitQuery.stream().map(wq -> (Callable<PlcModel>) () -> {
                String ip = wq.getHost();
                String address = wq.getAddress();
                IS7BusDevice s7 = holder.getBusDevice(ip);
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
                PlcModel plcModel = new PlcModel();
                plcModel.setPlcMeta(wq);
                plcModel.setValue(result);
                return plcModel;
            }).collect(Collectors.toList()));
            results = futures.stream()
                    .map(future -> {
                        PlcModel model = null;
                        try {
                            model = future.get();
                        } catch (Exception e) {
                            LOGGER.error("future处理出错", e);
                        }
                        return model;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (InterruptedException e) {
            LOGGER.error("线程池执行出错", e);
        }
        holder.finish();
        return results;
    }
}

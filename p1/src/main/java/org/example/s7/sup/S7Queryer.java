package org.example.s7.sup;

import org.example.s7.bus.IS7BusDevice;
import org.example.s7.bus.S7Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

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
     * 使用线程池的方式请求plc的数据
     *
     * @param supplier 要查询的s7元地址集合
     * @return 查询结果
     */
    public List<PlcModel> query(Supplier<List<S7Meta>> supplier) {
        List<S7Meta> s7Metas = supplier.get();
        holder.start();
        List<Callable<PlcModel>> callableList = new ArrayList<>();
        for (S7Meta s7Meta : s7Metas) {
            callableList.add(() -> {
                try {
                    String ip = s7Meta.getHost();
                    String address = s7Meta.getAddress();
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
                    plcModel.setPlcMeta(s7Meta);
                    plcModel.setValue(result);
                    return plcModel;
                } catch (Exception e) {
                    LOGGER.error("子任务执行出错!", e);
                    PlcModel plcModel = new PlcModel();
                    plcModel.setPlcMeta(s7Meta);
                    plcModel.setValue(null);
                    return plcModel;
                }
            });
        }
        List<PlcModel> results = new ArrayList<>();
        try {
            List<Future<PlcModel>> futures = es.invokeAll(callableList);
            for (Future<PlcModel> future : futures) {
                PlcModel model = future.get();
                if (model != null)
                    results.add(model);
            }
        } catch (Exception e) {
            LOGGER.error("线程池执行出错!", e);
        }
        holder.finish();
        return results;
    }
}

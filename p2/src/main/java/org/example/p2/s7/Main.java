package org.example.p2.s7;

import org.example.s7.sup.IS7Queryer;
import org.example.s7.sup.PlcModel;
import org.example.s7.sup.S7IpGroupQueryer;
import org.example.s7.sup.S7Meta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fh on 2020/11/10
 */
public class Main {
    private static Logger LOGGER = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args) {
        // 元配置列表
        List<S7Meta> list = new ArrayList<>();
        S7Meta m11 = new S7Meta();
        m11.setHost("192.168.0.190");
        m11.setAddress("DB1.DBD0");
        S7Meta m12 = new S7Meta();
        m12.setHost("192.168.0.190");
        m12.setAddress("DB1.DBD1");

        for (int i = 0; i < 100; i++) {
            list.add(m11);
            list.add(m12);
        }

        // 查询器
        IS7Queryer s7Query = new S7IpGroupQueryer();
        long start = System.currentTimeMillis();
        List<PlcModel> results = s7Query.query(list);
        long end = System.currentTimeMillis();
        s7Query.shutdown();
        long time = end - start;
        double avg = (double) time / list.size();
        LOGGER.info("{}个目标查询完成 {} ms, 平均: {} ms/个", list.size(), (end - start), avg);
        results.forEach(r -> LOGGER.info("address: {}, value: {}", r.getPlcMeta().getAddress(), r.getValue()));
    }
}

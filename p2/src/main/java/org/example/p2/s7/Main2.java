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
 * 2个ip的测试
 */
public class Main2 {
    private static Logger LOGGER = LoggerFactory.getLogger(Main2.class);

    public static void test1() {
        // 元配置列表
        List<S7Meta> list = new ArrayList<>();
        S7Meta m11 = new S7Meta();
        m11.setHost("192.168.0.190");
        m11.setAddress("DB1.DBB0");
        S7Meta m12 = new S7Meta();
        m12.setHost("192.168.0.190");
        m12.setAddress("DB1.DBB1");

        for (int i = 0; i < 100; i++) {
            list.add(m11);
            list.add(m12);
        }

        // 查询器
        IS7Queryer s7Query = new S7IpGroupQueryer();
        long start = System.currentTimeMillis();
        List<PlcModel<S7Meta>> results = s7Query.query(list);
        long end = System.currentTimeMillis();
        s7Query.shutdown();
        long time = end - start;
        double avg = (double) time / list.size();
        LOGGER.info("{}个目标查询完成 {} ms, 平均: {} ms/个", list.size(), (end - start), avg);
        results.forEach(r -> LOGGER.info("ip: {}, address: {}, value: {}", r.getPlcMeta().getHost(), r.getPlcMeta().getAddress(), r.getValue()));
    }

    public static void test2() {
        // 元配置列表
        List<S7Meta> list = new ArrayList<>();
        S7Meta m21 = new S7Meta();
        m21.setHost("192.168.0.191");
        m21.setAddress("DB1.DBW0");
        S7Meta m22 = new S7Meta();
        m22.setHost("192.168.0.191");
        m22.setAddress("DB1.DBW2");

        for (int i = 0; i < 100; i++) {
            list.add(m21);
            list.add(m22);
        }

        // 查询器
        IS7Queryer s7Query = new S7IpGroupQueryer();
        long start = System.currentTimeMillis();
        List<PlcModel<S7Meta>> results = s7Query.query(list);
        long end = System.currentTimeMillis();
        s7Query.shutdown();
        long time = end - start;
        double avg = (double) time / list.size();
        LOGGER.info("{}个目标查询完成 {} ms, 平均: {} ms/个", list.size(), (end - start), avg);
        results.forEach(r -> LOGGER.info("ip: {}, address: {}, value: {}", r.getPlcMeta().getHost(), r.getPlcMeta().getAddress(), r.getValue()));
    }

    public static void testMain() {
        // 元配置列表
        List<S7Meta> list = new ArrayList<>();
        S7Meta m11 = new S7Meta();
        m11.setHost("192.168.0.190");
        m11.setAddress("DB1.DBB0");
        S7Meta m12 = new S7Meta();
        m12.setHost("192.168.0.190");
        m12.setAddress("DB1.DBB1");

        S7Meta m21 = new S7Meta();
        m21.setHost("192.168.0.191");
        m21.setAddress("DB1.DBW0");
        S7Meta m22 = new S7Meta();
        m22.setHost("192.168.0.191");
        m22.setAddress("DB1.DBW2");

        for (int i = 0; i < 100; i++) {
            list.add(m11);
            list.add(m12);
            list.add(m21);
            list.add(m22);
        }

        // 查询器
        IS7Queryer s7Query = new S7IpGroupQueryer();
        long start = System.currentTimeMillis();
        List<PlcModel<S7Meta>> results = s7Query.query(list);
        long end = System.currentTimeMillis();
        s7Query.shutdown();
        long time = end - start;
        double avg = (double) time / list.size();
        LOGGER.info("{}个目标查询完成 {} ms, 平均: {} ms/个", list.size(), (end - start), avg);
        results.forEach(r -> LOGGER.info("ip: {}, address: {}, value: {}", r.getPlcMeta().getHost(), r.getPlcMeta().getAddress(), r.getValue()));
    }

    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args) {
        test1();
        test2();
        testMain();
    }
}

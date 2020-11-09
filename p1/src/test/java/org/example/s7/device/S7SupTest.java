package org.example.s7.device;

import org.example.s7.sup.S7Meta;
import org.example.s7.sup.S7Queryer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fh on 2020/11/9
 */
public class S7SupTest {
    @Test
    public void test() {
        List<String> metaList = new ArrayList<>();
        for (int i = 0; i < 22; i++) {
            for (int j = 0; j < 200; j++) {
                metaList.add(String.format("DB1.DBB%d", i));
            }
        }

        S7Queryer s7Query = new S7Queryer();
        List<S7Meta> list = metaList.stream().map(address -> {
            S7Meta meta = new S7Meta();
            meta.setHost("192.168.0.190");
            meta.setAddress(address);
            return meta;
        }).collect(Collectors.toList());
        long start = System.currentTimeMillis();
        List<S7Queryer.PlcModel> results = s7Query.query(() -> list);
        long end = System.currentTimeMillis();
        long time = end - start;
        double avg = (double) time / metaList.size();
        System.out.printf("%d个目标查询完成 %d ms, 平均: %f ms/个%n", metaList.size(), (end - start), avg);
        results.forEach(r -> {
            System.out.println(String.format("address: %s, value: %s", r.getPlcMeta().getAddress(), r.getValue()));
        });
    }
}

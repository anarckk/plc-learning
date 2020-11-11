package org.example.s7.sup;

import java.util.List;

/**
 * Created by fh on 2020/11/9
 */
public interface IS7Queryer {
    /**
     * 查询元配置集合对应的plc数值
     * 如果查询不到，则value为null
     *
     * @param s7MetaList 元地址集合
     * @param <T> S7Meta或继承S7Meta的子类
     * @return plc数值集合
     */
    <T extends S7Meta> List<PlcModel<T>> query(List<T> s7MetaList);

    /**
     * 关闭线程池
     */
    void shutdown();
}

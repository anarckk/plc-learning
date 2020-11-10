package org.example.s7.sup;

import java.util.List;

/**
 * Created by fh on 2020/11/9
 */
public interface IS7Queryer {
    /**
     * 查询元配置集合对应的plc数值
     *
     * @param s7MetaList 元地址集合
     * @return plc数值集合
     */
    List<PlcModel> query(List<S7Meta> s7MetaList);

    /**
     * 关闭线程池
     */
    void shutdown();
}

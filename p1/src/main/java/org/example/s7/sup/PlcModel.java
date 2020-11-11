package org.example.s7.sup;

/**
 * Created by fh on 2020/11/9
 * plc数据模型，存放元数据和此位置上的数据
 */
public class PlcModel<T extends S7Meta> {
    private T plcMeta;
    private Object value;

    public T getPlcMeta() {
        return plcMeta;
    }

    public void setPlcMeta(T plcMeta) {
        this.plcMeta = plcMeta;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

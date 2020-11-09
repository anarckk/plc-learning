package org.example.s7.sup;

/**
 * Created by fh on 2020/11/9
 */
public class PlcModel {
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

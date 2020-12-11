package org.example.s7.sup;

/**
 * Created by fh on 2020/12/11
 */
public class S7SupUtil {
    /**
     * 从 IS7Queryer 查询出来的值都会被包装成Object类型，其子类型是 Short、Byte、Integer
     *
     * @param o 查询值
     * @return 转成Integer
     */
    public static Integer objToInteger(Object o) {
        Integer r = null;
        if (o instanceof Integer) {
            r = (Integer) o;
        } else if (o instanceof Short) {
            //noinspection PointlessArithmeticExpression
            r = (Short) o + 0;
        } else if (o instanceof Byte) {
            //noinspection PointlessArithmeticExpression
            r = (Byte) o + 0;
        }
        return r;
    }
}

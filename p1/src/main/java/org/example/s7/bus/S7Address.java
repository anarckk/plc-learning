package org.example.s7.bus;

import org.example.s7.device.S7DeviceException;

/**
 * Created by fh on 2020/11/5
 * plc内存地址转换对象
 */
public class S7Address {
    public enum S7Type {
        BIT((short) 1),
        BYTE((short) 8),
        WORD((short) 16),
        DWORD((short) 32);

        private short value;

        S7Type(short value) {
            this.value = value;
        }

        public short getValue() {
            return value;
        }
    }

    /**
     * 1 位地址：DB1.DBX18.0
     * 8 位地址：DB1.DBB19
     * 16 位地址：DB1.DBW0
     * 32 位地址：DB1.DBD6
     */
    private String address;
    /**
     * 数据长度
     * 可能的值： 1、8、16、32
     */
    private S7Type type;
    private int db;
    private int byteOffset;
    private short bitOffset;

    public S7Address(String address) throws S7DeviceException {
        try {
            this.address = address;

            String addressCopy = address.toUpperCase();
            String _db = addressCopy.substring(addressCopy.indexOf("DB") + 2, addressCopy.indexOf("."));
            String _byteOffset = "";
            String _bitOffset = "";
            if (addressCopy.contains("DBX")) {
                _byteOffset = addressCopy.substring(addressCopy.indexOf("DBX") + 3, addressCopy.lastIndexOf("."));
                _bitOffset = addressCopy.substring(addressCopy.lastIndexOf(".") + 1);
                bitOffset = Short.parseShort(_bitOffset);
                this.type = S7Type.BIT;
            } else if (addressCopy.contains("DBB")) {
                _byteOffset = addressCopy.substring(addressCopy.indexOf("DBB") + 3);
                this.type = S7Type.BYTE;
            } else if (addressCopy.contains("DBW")) {
                _byteOffset = addressCopy.substring(addressCopy.indexOf("DBW") + 3);
                this.type = S7Type.WORD;
            } else if (addressCopy.contains("DBD")) {
                _byteOffset = addressCopy.substring(addressCopy.indexOf("DBD") + 3);
                this.type = S7Type.DWORD;
            } else {
                throw new S7DeviceException(String.format("address[%s] 数据地址类型超出处理范围", address));
            }

            db = Integer.parseInt(_db);
            byteOffset = Integer.parseInt(_byteOffset);
        } catch (Exception e) {
            throw new S7DeviceException(String.format("address[%s] 地址解析失败!", address));
        }
    }

    public String getAddress() {
        return address;
    }

    public S7Type getType() {
        return type;
    }

    public int getDB() {
        return db;
    }

    public int getByteOffset() {
        return byteOffset;
    }

    public short getBitOffset() {
        return bitOffset;
    }

    /**
     * 检查是否和 other 在同一个字节上
     */
    public boolean inSameByte(S7Address other) {
        return db == other.db && byteOffset == other.byteOffset;
    }
}

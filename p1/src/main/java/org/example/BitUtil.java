package org.example;

/**
 * Created by fh on 2020/4/2
 * 比特位 debug 工具包
 */
public class BitUtil {
    /**
     * 输出字节的二进制字符串
     *
     * @param b 输入
     * @return 二进制字符串
     */
    public static String parseBinary(byte b) {
        StringBuilder result = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            result.append((b >> i) & 0x01);
        }
        return result.toString();
    }

    public static String parseBinary(char a) {
        byte[] b = new byte[2];
        b[0] = (byte) (a >> 8);
        b[1] = (byte) a;
        return parseBinary(b[0]) + " " + parseBinary(b[1]);
    }

    public static String parseBinary(int a) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (a >> 24);
        bytes[1] = (byte) (a >> 16);
        bytes[2] = (byte) (a >> 8);
        bytes[3] = (byte) a;
        return parseBinary(bytes[0]) + " " + parseBinary(bytes[1]) + " " + parseBinary(bytes[2]) + " " + parseBinary(bytes[3]);
    }

    public static String parseBinary(short s) {
        return parseBinary((char) s);
    }

    public static String parseBinary(long a) {
        StringBuilder sb = new StringBuilder();
        byte temp;
        for (int i = 0; i < 8; i++) {
            temp = (byte) (a >> ((7 - i) * 8));
            sb.append(parseBinary(temp));
            if (i != 7) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static String parseBinary(String s) {
        StringBuilder sb = new StringBuilder();
        char temp;
        for (int i = 0; i < s.length(); i++) {
            temp = s.charAt(i);
            sb.append(parseBinary(temp));
            if (i != s.length() - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }
}

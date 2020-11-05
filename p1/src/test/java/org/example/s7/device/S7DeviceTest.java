package org.example.s7.device;

import org.example.BitUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by fh on 2020/11/4
 */
public class S7DeviceTest {
    private String host = "192.168.0.190";

    @Test
    public void readDBB() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        byte b = device.readDBB(1, 18);
        device.disconnect();
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(0x1, (b & 0x1));
    }

    @Test
    public void readDBB2() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        byte b = device.readDBB(1, 18);
        device.disconnect();
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(0x00, (b & 0x2));
    }

    @Test
    public void readDBB3() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        byte b = device.readDBB(1, 19);
        device.disconnect();
        System.out.println(b);
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(100, b);
    }

    @Test
    public void readDBB4() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        byte b = device.readDBB(1, 20);
        device.disconnect();
        System.out.println(b);
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(101, b);
    }

    @Test
    public void readDBW() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        short b = device.readDBW(1, 0);
        device.disconnect();
        System.out.println(b);
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(b, 257);
    }

    @Test
    public void readDBW1() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        short b = device.readDBW(1, 2);
        device.disconnect();
        System.out.println(b);
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(b, -3);
    }

    @Test
    public void readDBW2() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        short b = device.readDBW(1, 4);
        device.disconnect();
        System.out.println(b);
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(b, -1);
    }

    @Test
    public void readDBD() throws S7DeviceException {
        IS7Device device = new S7Device(host);
        device.connect();
        int b = device.readDBD(1, 6);
        device.disconnect();
        System.out.println(b);
        System.out.println(BitUtil.parseBinary(b));
        Assert.assertEquals(b, -2147483648);
    }
}

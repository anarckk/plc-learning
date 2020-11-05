package org.example.s7.device;

import org.example.s7.bus.IS7BusDevice;
import org.example.s7.bus.S7Address;
import org.example.s7.bus.S7BusDevice;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;

/**
 * Created by fh on 2020/11/5
 */
public class S7BusDeviceTest {
    private IS7BusDevice s7BusDevice;

    @Before
    public void before() throws S7DeviceException {
        IS7Device device = new S7Device("192.168.0.190");
        s7BusDevice = new S7BusDevice(device);
        s7BusDevice.connect();
    }

    @Test
    public void testDBX() throws S7DeviceException {
        byte b180 = s7BusDevice.readDBX(new S7Address("DB1.DBX18.0"));
        byte b181 = s7BusDevice.readDBX(new S7Address("DB1.DBX18.1"));
        System.out.println(b180);
        System.out.println(b181);
    }

    @Test
    public void testDBW() throws S7DeviceException {
        short b180 = s7BusDevice.readDBW(new S7Address("DB1.DBW0"));
        System.out.println(b180);
    }

    @Test
    public void testDBD() throws S7DeviceException {
        int b180 = s7BusDevice.readDBD(new S7Address("DB1.DBD10"));
        System.out.println(b180);
    }

    @Test
    public void testDBB() throws S7DeviceException {
        byte b180 = s7BusDevice.readDBB(new S7Address("DB1.DBB21"));
        System.out.println(b180);
    }

    @Test
    public void test2() {
        System.out.println(Integer.toBinaryString(3000));
    }

    @Test
    public void contrastTest() {
        String[] address = new String[]{
                "DB1.DBB0",
                "DB1.DBB1",
                "DB1.DBB2",
                "DB1.DBB3",
                "DB1.DBB4",
                "DB1.DBB5",
                "DB1.DBB6",
                "DB1.DBB7",
                "DB1.DBB8",
                "DB1.DBB9",
                "DB1.DBB10",
                "DB1.DBB11",
                "DB1.DBB12",
                "DB1.DBB13",
                "DB1.DBB14",
                "DB1.DBB15",
                "DB1.DBB16",
                "DB1.DBB17",
                "DB1.DBB18",
                "DB1.DBB19",
                "DB1.DBB20",
                "DB1.DBB21",
        };
        try {
            Thread current = Thread.currentThread();
            String cname = current.getName();
            for (String s : address) {
                System.out.printf("Thread:[%s][%s] address:[%s], result: [%d] %n", cname, System.currentTimeMillis(), s, s7BusDevice.readDBB(new S7Address(s)));
            }
        } catch (S7DeviceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void threadTest() throws InterruptedException {
        String[] address = new String[]{
                "DB1.DBB0",
                "DB1.DBB1",
                "DB1.DBB2",
                "DB1.DBB3",
                "DB1.DBB4",
                "DB1.DBB5",
                "DB1.DBB6",
                "DB1.DBB7",
                "DB1.DBB8",
                "DB1.DBB9",
        };
        String[] address2 = new String[]{
                "DB1.DBB10",
                "DB1.DBB11",
                "DB1.DBB12",
                "DB1.DBB13",
                "DB1.DBB14",
                "DB1.DBB15",
                "DB1.DBB16",
                "DB1.DBB17",
                "DB1.DBB18",
                "DB1.DBB19",
                "DB1.DBB20",
                "DB1.DBB21",
        };
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch cdl = new CountDownLatch(2);
        Thread t1 = new Thread(() -> {
            try {
                start.await();
                Thread current = Thread.currentThread();
                String cname = current.getName();
                for (String s : address) {
                    System.out.printf("Thread:[%s][%s] address:[%s], result: [%d] %n", cname, System.currentTimeMillis(), s, s7BusDevice.readDBB(new S7Address(s)));
                }
            } catch (S7DeviceException | InterruptedException e) {
                e.printStackTrace();
            }
            cdl.countDown();
        });
        Thread t2 = new Thread(() -> {
            try {
                start.await();
                Thread current = Thread.currentThread();
                String cname = current.getName();
                for (String s : address2) {
                    System.out.printf("Thread:[%s][%s] address:[%s], result: [%d] %n", cname, System.currentTimeMillis(), s, s7BusDevice.readDBB(new S7Address(s)));
                }
            } catch (S7DeviceException | InterruptedException e) {
                e.printStackTrace();
            }
            cdl.countDown();
        });
        t1.start();
        t2.start();
        long startTime = System.currentTimeMillis();
        start.countDown();
        cdl.await();
        long endTime = System.currentTimeMillis();
        System.out.printf("程序结束！耗时: %d ms %n", (endTime - startTime));
    }
}

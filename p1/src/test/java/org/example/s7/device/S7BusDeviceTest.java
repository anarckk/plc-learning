package org.example.s7.device;

import org.example.s7.bus.IS7BusDevice;
import org.example.s7.bus.S7Address;
import org.example.s7.bus.S7BusDevice;
import org.junit.Before;
import org.junit.Test;

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
}

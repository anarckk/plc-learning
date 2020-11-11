package org.example.s7.bus;

import org.example.s7.device.IS7Device;
import org.example.s7.device.S7Device;
import org.example.s7.device.S7DeviceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fh on 2020/11/5
 */
public class S7BusDevice implements IS7BusDevice {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private static class Cache {
        private S7Address address;
        private byte value;

        public Cache(S7Address address, byte value) {
            this.address = address;
            this.value = value;
        }

        public S7Address getAddress() {
            return address;
        }

        public void setAddress(S7Address address) {
            this.address = address;
        }

        public byte getValue() {
            return value;
        }

        public void setValue(byte value) {
            this.value = value;
        }
    }

    private IS7Device s7Device;
    private List<Cache> cacheList = new ArrayList<>();

    public S7BusDevice(IS7Device s7Device) {
        if (s7Device == null) {
            throw new IllegalArgumentException("s7Device can't be null.");
        }
        this.s7Device = s7Device;
    }

    public S7BusDevice(String host) {
        this.s7Device = new S7Device(host);
    }

    @Override
    public void connect() throws S7DeviceException {
        s7Device.connect();
    }

    @Override
    public void disconnect() throws S7DeviceException {
        s7Device.disconnect();
    }

    @Override
    public IS7Device getDevice() {
        return s7Device;
    }

    @Override
    public void clearCache() {
        cacheList.clear();
    }

    @Override
    public byte readDBX(S7Address address) throws S7DeviceException {
        if (!address.getType().equals(S7Address.S7Type.BIT)) {
            throw new S7DeviceException(String.format("地址【%s】不是 DBX", address.getAddress()));
        }
        Cache cache = cacheList.stream()
                .filter(_cache -> _cache.getAddress().inSameByte(address))
                .findFirst()
                .orElse(null);
        byte b;
        if (cache != null) {
            b = cache.getValue();
        } else {
            b = s7Device.readDBB(address.getDB(), address.getByteOffset());
            Cache cache1 = new Cache(address, b);
            cacheList.add(cache1);
        }
        byte r = (byte) ((b >> address.getBitOffset()) & 0x01);
        LOGGER.debug("S7BusDevice readDBX[{}]: {}", address, r);
        return r;
    }

    @Override
    public byte readDBB(S7Address address) throws S7DeviceException {
        if (!address.getType().equals(S7Address.S7Type.BYTE)) {
            throw new S7DeviceException(String.format("地址【%s】不是 DBB", address.getAddress()));
        }
        byte r = s7Device.readDBB(address.getDB(), address.getByteOffset());
        LOGGER.debug("S7BusDevice readDBB[{}]: {}", address, r);
        return r;
    }

    @Override
    public short readDBW(S7Address address) throws S7DeviceException {
        if (!address.getType().equals(S7Address.S7Type.WORD)) {
            throw new S7DeviceException(String.format("地址【%s】不是 DBW", address.getAddress()));
        }
        short r = s7Device.readDBW(address.getDB(), address.getByteOffset());
        LOGGER.debug("S7BusDevice readDBW[{}]: {}", address, r);
        return r;
    }

    @Override
    public int readDBD(S7Address address) throws S7DeviceException {
        if (!address.getType().equals(S7Address.S7Type.DWORD)) {
            throw new S7DeviceException(String.format("地址【%s】不是 DBD", address.getAddress()));
        }
        int r = s7Device.readDBD(address.getDB(), address.getByteOffset());
        LOGGER.debug("S7BusDevice readDBD[{}]: {}", address, r);
        return r;
    }
}

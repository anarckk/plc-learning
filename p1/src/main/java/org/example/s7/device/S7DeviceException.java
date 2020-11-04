package org.example.s7.device;

/**
 * Created by fh on 2020/11/4
 */
public class S7DeviceException extends Exception {
    public S7DeviceException(String message) {
        super(message);
    }

    public S7DeviceException(String message, Throwable cause) {
        super(message, cause);
    }
}

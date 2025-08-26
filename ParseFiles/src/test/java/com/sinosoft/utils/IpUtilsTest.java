package com.sinosoft.utils;

import org.junit.jupiter.api.Test;

import java.net.SocketException;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class IpUtilsTest {

    @Test
    void getIpLocakAddress() throws UnknownHostException {
        System.out.println(IpUtils.getIpLocakAddress());
    }

    @Test
    void getIpAddress() throws SocketException {
        System.out.println(IpUtils.getIpAddress().toString());
        System.out.println(IpUtils.getIpAddress0());
    }

}
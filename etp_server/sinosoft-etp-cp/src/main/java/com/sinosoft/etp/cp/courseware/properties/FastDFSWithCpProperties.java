package com.sinosoft.etp.cp.courseware.properties;

//@Component
public class FastDFSWithCpProperties {

    // @Value("${fdfs.resHost}")
    private String resHost;
    // @Value("${fdfs.storagePort}")
    private String storagePort;

    public String getResHost() {
        return resHost;
    }

    public FastDFSWithCpProperties setResHost(String resHost) {
        this.resHost = resHost;
        return this;
    }

    public String getStoragePort() {
        return storagePort;
    }

    public FastDFSWithCpProperties setStoragePort(String storagePort) {
        this.storagePort = storagePort;
        return this;
    }
}

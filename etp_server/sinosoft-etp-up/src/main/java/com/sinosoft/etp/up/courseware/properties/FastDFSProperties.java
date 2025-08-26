package com.sinosoft.etp.up.courseware.properties;

// @Component
public class FastDFSProperties {

    // @Value("${fdfs.resHost}")
    private String resHost;
    // @Value("${fdfs.storagePort}")
    private String storagePort;

    public String getResHost() {
        return resHost;
    }

    public FastDFSProperties setResHost(String resHost) {
        this.resHost = resHost;
        return this;
    }

    public String getStoragePort() {
        return storagePort;
    }

    public FastDFSProperties setStoragePort(String storagePort) {
        this.storagePort = storagePort;
        return this;
    }
}

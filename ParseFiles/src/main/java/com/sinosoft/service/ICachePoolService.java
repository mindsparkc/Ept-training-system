package com.sinosoft.service;

/**
 * 缓存作业池的维护
 *
 * @author WANGLIJIE
 */
public interface ICachePoolService {
    String PDF_FILE_POOL_KEY = "pdf:file:pool:key";
    String PDF_FILE_PARSE_KEY = "pdf:file:parse:key";

    public void addQueueTask(String task);
    public String getTask();
    public void finishTask(String task);
    public String existsTask(String task);
}

package com.erobbing.ping;

/**
 * Created by zhangzhaolei on 2018/8/21.
 */

public class PingNetEntity {

    private String ip;
    private int pingCount;
    private int pingWtime;
    private StringBuffer resultBuffer;
    private String pingTime;
    private boolean result;

    public PingNetEntity(String ip, int pingCount, int pingWtime, StringBuffer resultBuffer) {
        this.ip = ip;
        this.pingWtime = pingWtime;
        this.pingCount = pingCount;
        this.resultBuffer = resultBuffer;
    }

    public String getPingTime() {
        return pingTime;
    }

    public void setPingTime(String pingTime) {
        this.pingTime = pingTime;
    }

    public StringBuffer getResultBuffer() {
        return resultBuffer;
    }

    public void setResultBuffer(StringBuffer resultBuffer) {
        this.resultBuffer = resultBuffer;
    }

    public int getPingCount() {
        return pingCount;
    }

    public void setPingCount(int pingCount) {
        this.pingCount = pingCount;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getPingWtime() {
        return pingWtime;
    }

    public void setPingWtime(int pingWtime) {
        this.pingWtime = pingWtime;
    }
}

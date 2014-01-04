package net.cmikavac.autowol.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Device implements Serializable {
    private Long mId;
    private String mName;
    private String mMac;
    private String mIp;
    private Integer mPort;
    private Boolean mSSID;
    private Long mQuietHoursFrom;
    private Long mQuietHoursTo;
    private Integer mIdleTime;
    
    public Device() {
        super();
        this.mId = (long)-1;
    }
    
    public Device(String name, String ip, String mac) {
        super();
        this.mName = name;
        this.mMac = mac;
        this.mIp = ip;
    }

    public Device(long id, String name, String ip, String mac) {
        super();
        this.mId = id;
        this.mName = name;
        this.mMac = mac;
        this.mIp = ip;
    }

    public long getId() {
        return mId;
    }

    public void setId(Long id) {
        this.mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getMac() {
        return mMac;
    }

    public String getIp() {
        return mIp;
    }

    public void setIp(String ip) {
        this.mIp = ip;
    }

    public void setMac(String mac) {
        this.mMac = mac;
    }

    public Integer getPort() {
        return mPort;
    }

    public void setPort(Integer port) {
        this.mPort = port;
    }

    public Boolean getSSID() {
        return mSSID;
    }

    public void setSSID(Boolean ssid) {
        this.mSSID = ssid;
    }

    public Long getQuietHoursFrom() {
        return mQuietHoursFrom;
    }

    public void setQuietHoursFrom(Long quietHoursFrom) {
        this.mQuietHoursFrom = quietHoursFrom;
    }

    public Long getQuietHoursTo() {
        return mQuietHoursTo;
    }

    public void setQuietHoursTo(Long quietHoursTo) {
        this.mQuietHoursTo = quietHoursTo;
    }
    
    public Integer getIdleTime() {
        return mIdleTime;
    }

    public void setIdleTime(Integer idleTime) {
        this.mIdleTime = idleTime;
    }
}

package net.cmikavac.autowol.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DeviceModel implements Serializable {
    private Long mId;
    private String mName;
    private String mMac;
    private String mIp;
    private Integer mPort;
    private String mSSID;
    private Long mQuietHoursFrom;
    private Long mQuietHoursTo;
    private Integer mIdleTime;
    
    public DeviceModel() {
        super();
        this.mId = (long)-1;
    }
    
    public DeviceModel(String name, String mac, String ip, Integer port, 
            String ssid, Long quietHoursFrom, Long quietHoursTo, Integer idleTime) {
        super();
        this.mName = name;
        this.mMac = mac;
        this.mIp = ip;
        this.mPort = port;
        this.mSSID = ssid;
        this.mQuietHoursFrom = quietHoursFrom;
        this.mQuietHoursTo = quietHoursTo;
        this.mIdleTime = idleTime;
    }

    public DeviceModel(Long id, String name, String mac, String ip, Integer port,
            String ssid, Long quietHoursFrom, Long quietHoursTo, Integer idleTime) {
        super();
        this.mId = id;
        this.mName = name;
        this.mMac = mac;
        this.mIp = ip;
        this.mPort = port;
        this.mSSID = ssid;
        this.mQuietHoursFrom = quietHoursFrom;
        this.mQuietHoursTo = quietHoursTo;
        this.mIdleTime = idleTime;
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

    public String getSSID() {
        return mSSID;
    }

    public void setSSID(String ssid) {
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

package net.cmikavac.autowol.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DeviceModel implements Serializable {
    private Long mId;
    private String mName;
    private String mMac;
    private String mHost;
    private String mBroadcast;
    private Integer mPort;
    private String mSSID;
    private Long mQuietHoursFrom;
    private Long mQuietHoursTo;
    private Integer mIdleTime;
    private Long mLastDisconnected;
    
    public DeviceModel() {
        super();
        this.mId = (long)-1;
    }
    
    public DeviceModel(String name, String mac, String host, String broadcast, Integer port, String ssid, 
            Long quietHoursFrom, Long quietHoursTo, Integer idleTime) {
        super();
        this.mName = name;
        this.mMac = mac;
        this.mHost = host;
        this.mBroadcast = broadcast;
        this.mPort = port;
        this.mSSID = ssid;
        this.mQuietHoursFrom = quietHoursFrom;
        this.mQuietHoursTo = quietHoursTo;
        this.mIdleTime = idleTime;
    }

    public DeviceModel(Long id, String name, String mac, String host, String broadcast, Integer port, String ssid, 
            Long quietHoursFrom, Long quietHoursTo, Integer idleTime, Long lastDisconnected) {
        super();
        this.mId = id;
        this.mName = name;
        this.mMac = mac;
        this.mHost = host;
        this.mBroadcast = broadcast;
        this.mPort = port;
        this.mSSID = ssid;
        this.mQuietHoursFrom = quietHoursFrom;
        this.mQuietHoursTo = quietHoursTo;
        this.mIdleTime = idleTime;
        this.mLastDisconnected = lastDisconnected;
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

    public void setMac(String mac) {
        this.mMac = mac;
    }

    public String getHost() {
        return mHost;
    }

    public void setHost(String host) {
        this.mHost = host;
    }

    public String getBroadcast() {
        return mBroadcast;
    }

    public void setBroadcast(String broadcast) {
        this.mBroadcast = broadcast;
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
    
    public Long getLastDisconnected() {
        return mLastDisconnected;
    }
    
    public void setLastDisconnected(Long lastDisconnected) {
        this.mLastDisconnected = lastDisconnected;
    }
}

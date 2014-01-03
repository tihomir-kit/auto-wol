package net.cmikavac.autowol.models;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Device implements Serializable {
	private long mId;
	private String mName;
	private String mMac;
	private String mIp;
	private int mPort;
	private Boolean mSSID;
	private Date mQuietHoursStart;
	private Date mQuietHoursEnd;
	private Date mIdleHours;
	
	public Device() {
		super();
		this.mId = -1;
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

	public void setId(Integer id) {
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

	public int getPort() {
		return mPort;
	}

	public void setPort(int port) {
		this.mPort = port;
	}

	public Boolean getSSID() {
		return mSSID;
	}

	public void setSSID(Boolean ssid) {
		this.mSSID = ssid;
	}

	public Date getQuietHoursStart() {
		return mQuietHoursStart;
	}

	public void setQuietHoursStart(Date quietHoursStart) {
		this.mQuietHoursStart = quietHoursStart;
	}

	public Date getQuietHoursEnd() {
		return mQuietHoursEnd;
	}

	public void setQuietHoursEnd(Date quietHoursEnd) {
		this.mQuietHoursEnd = quietHoursEnd;
	}
	
	public Date getIdleHours() {
		return mIdleHours;
	}

	public void setIdleHours(Date idleHours) {
		this.mIdleHours = idleHours;
	}
}

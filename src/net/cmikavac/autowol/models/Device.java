package net.cmikavac.autowol.models;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class Device implements Serializable {
	private String mId;
	private String mName;
	private String mIp;
	private String mMac;
	private Boolean mAutoWOL;
	private Date mLastConnectedFilter;
	private Date mSleepingHoursStart;
	private Date mSleepingHoursEnd;
	
	public Device() {
		super();
	}
	
	public Device(String name, String ip, String mac) {
		super();
		this.mName = name;
		this.mIp = ip;
		this.mMac = mac;
	}
	
	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getIp() {
		return mIp;
	}

	public void setIp(String ip) {
		this.mIp = ip;
	}

	public String getMac() {
		return mMac;
	}

	public void setMac(String mac) {
		this.mMac = mac;
	}
	
	public Boolean getAutoWOL() {
		return mAutoWOL;
	}

	public void setAutoWOL(Boolean autoWOL) {
		this.mAutoWOL = autoWOL;
	}

	public Date getLastConnectedFilter() {
		return mLastConnectedFilter;
	}

	public void setLastConnectedFilter(Date lastConnectedFilter) {
		this.mLastConnectedFilter = lastConnectedFilter;
	}

	public Date getSleepingHoursStart() {
		return mSleepingHoursStart;
	}

	public void setSleepingHoursStart(Date sleepingHoursStart) {
		this.mSleepingHoursStart = sleepingHoursStart;
	}

	public Date getSleepingHoursEnd() {
		return mSleepingHoursEnd;
	}

	public void setSleepingHoursEnd(Date sleepingHoursEnd) {
		this.mSleepingHoursEnd = sleepingHoursEnd;
	}		
}

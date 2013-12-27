package net.cmikavac.autowol.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Device implements Serializable {
	private String mName;
	private String mIp;
	private String mMac;
	
	public Device() {
		super();
		createNewDevice();
	}
	
	public Device(String name, String ip, String mac) {
		super();
		this.mName = name;
		this.mIp = ip;
		this.mMac = mac;
	}
	
	private void createNewDevice() {
		this.mName = "";
		this.mIp = "";
		this.mMac = "";				
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
}

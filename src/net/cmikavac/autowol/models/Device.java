package net.cmikavac.autowol.models;

public class Device {
	private String mName;
	private String mIp;
	private String mMac;
	
	public Device(String name, String ip, String mac) {
		super();
		this.mName = name;
		this.mIp = ip;
		this.mMac = mac;
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

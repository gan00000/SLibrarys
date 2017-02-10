package com.starpy.sdk.entrance.entity;

import java.io.Serializable;

public class EfunMapMemberEntity implements Serializable{

	private static final long serialVersionUID = 1L;

    private double latitude;  //纬度
    private double longitude;  //经度
    private String memberName;  //名称
    private double distance;  //距离
    private int id;
    
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
    
}

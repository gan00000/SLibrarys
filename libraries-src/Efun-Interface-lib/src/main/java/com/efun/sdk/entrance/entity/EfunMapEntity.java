package com.efun.sdk.entrance.entity;

public class EfunMapEntity extends EfunBaseEntity {

	private static final long serialVersionUID = 1L;

    private double longitude; //经度
    private double latitude; //纬度
    private int radius; //距离、最小为500、单位为米
    private int count;  //获取的信息数量、最小为10、最大为100
    private int distance; //距离
    private String userId;
    private String type; //获取信息类型
    
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}

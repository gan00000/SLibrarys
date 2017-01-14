package com.efun.sdk.entrance.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.efun.core.callback.EfunCallBack;

public class EfunEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private EfunCallBack efunCallBack;
	
	private EfunCallBack efunCallBack2;

	private String language;
	
	//以下两个参数为备用
	
//	private String reservedOne;
//	private String reservedTwo;

	/**
	 * @return the efunCallBack
	 */
	public EfunCallBack getEfunCallBack() {
		return efunCallBack;
	}

	/**
	 * @param efunCallBack the efunCallBack to set
	 */
	public void setEfunCallBack(EfunCallBack efunCallBack) {
		this.efunCallBack = efunCallBack;
	}

	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	public EfunCallBack getEfunCallBack2() {
		return efunCallBack2;
	}

	public void setEfunCallBack2(EfunCallBack efunCallBack2) {
		this.efunCallBack2 = efunCallBack2;
	}


/*	public String getReservedOne() {
		return reservedOne;
	}

	public void setReservedOne(String reservedOne) {
		this.reservedOne = reservedOne;
	}

	public String getReservedTwo() {
		return reservedTwo;
	}

	public void setReservedTwo(String reservedTwo) {
		this.reservedTwo = reservedTwo;
	}
	*/
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		Class c = this.getClass();
		StringBuilder sb = new StringBuilder();
		while (c != null && c != EfunEntity.class.getSuperclass()) {
			Field fields[] = c.getDeclaredFields();
			
			for (int i = 0; i < fields.length; i++) {
					try {
						fields[i].setAccessible(true);
						String name = fields[i].getName();
						if (name.equals("serialVersionUID")) {
							continue;
						}
						Object value = fields[i].get(this);
						sb.append(name + "=" + value + ",");
						//Log.d("efun", name + "=" + value);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
			}
			
			c = c.getSuperclass();
		}
		return sb.toString();
	}
	
}

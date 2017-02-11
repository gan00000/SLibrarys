package com.starpy.sdk.entrance.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.core.base.callback.ISCallBack;

public class EfunEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ISCallBack ISCallBack;
	
	private ISCallBack ISCallBack2;

	private String language;
	
	//以下两个参数为备用
	
//	private String reservedOne;
//	private String reservedTwo;

	/**
	 * @return the ISCallBack
	 */
	public ISCallBack getISCallBack() {
		return ISCallBack;
	}

	/**
	 * @param ISCallBack the ISCallBack to set
	 */
	public void setISCallBack(ISCallBack ISCallBack) {
		this.ISCallBack = ISCallBack;
	}

	/**
	 * @return the gameLanguage
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * @param language the gameLanguage to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}

	public ISCallBack getISCallBack2() {
		return ISCallBack2;
	}

	public void setISCallBack2(ISCallBack ISCallBack2) {
		this.ISCallBack2 = ISCallBack2;
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

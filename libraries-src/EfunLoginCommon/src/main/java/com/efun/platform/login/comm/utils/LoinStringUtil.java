package com.efun.platform.login.comm.utils;

public class LoinStringUtil{
	
	public static boolean isAllEmpty(String... mString) {
		for (String string : mString) {
			if (isNotEmpty(string)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isAllNotEmpty(String... mString) {
		if (isAllEmpty(mString)) {
			return false;
		}
		return true;
	}
	
	public static boolean isEmpty(CharSequence cs)
	  {
	    return ((cs == null) || (cs.length() == 0));
	  }

	  public static boolean isNotEmpty(CharSequence cs)
	  {
	    return (!(isEmpty(cs)));
	  }
}

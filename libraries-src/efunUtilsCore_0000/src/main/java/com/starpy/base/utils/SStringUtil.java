package com.starpy.base.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.util.Log;

/**
 * Class Description：String util
 * 
 * @author Joe
 * @date 2013-4-16
 * @version 1.0
 */
public class SStringUtil {

	/**
	 * 去除字符串中的换行、回车、制表符
	 * 
	 * @param inputValue
	 *            输入字符串
	 * @return String
	 */
	public static String removeEnter(String inputValue) {
		if (isEmpty(inputValue))
			return null;
		// \n 匹配一个换行符
		// \r 匹配一个回车符
		// \t 匹配一个制表符
		Pattern p = Pattern.compile("\t|\r|\n");
		Matcher m = p.matcher(inputValue);
		return m.replaceAll("").trim();
	}

	/**
	 * 获取字符串的字符数(英文 1个字符，中文 2个字符)
	 * 
	 * @param inputValue
	 * @return 字符长度
	 */
	public static int lengthOfChar(String inputValue) {
		if (isEmpty(inputValue))
			return 0;
		if (inputValue.matches("[a-zA-Z0-9\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\_\\+\\`\\;\\'\\,\\.\\/\\|\"\\:\\<\\>\\?\\-\\=\\_\\+\\\\]*"))
			return inputValue.length();
		return inputValue.replaceAll("[\u4E00-\u9FFF\\（\\）\\“\\”\\：\\—]", "**").length();
	}

	/**
	 * 验证字符长度是否合法
	 * 
	 * @param inputValue
	 *            输入字符
	 * @param len
	 *            合法长度
	 * @return boolean
	 */
	public static boolean checkParamLen(String inputValue, int len) {
		if (lengthOfChar(inputValue) <= len)
			return true;
		return false;
	}


	/**
	 * 邮箱验证
	 * 
	 * @param email
	 * @return boolean
	 */
	public static boolean email(String email) {
		if (isEmpty(email))
			return false;
		return email.matches("^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$");
	}

	/**
	 * 帐号验证
	 * 
	 * @param loginName
	 * @return boolean
	 */
	public static boolean loginName(String loginName) {
		if (isEmpty(loginName))
			return false;
		return loginName.matches("[\\w_]{1,50}");
	}

	/**
	 * 将空字符串(null)转换为 ""
	 * 
	 * @param inputValue
	 *            输入参数
	 * @return 输出 String
	 */
	public static String valueOf(String inputValue) {
		if (inputValue == null)
			return "";
		return String.valueOf(inputValue);
	}

	/**
	 * 将空字符串(null)转换为 ""
	 * 
	 * @param inputValue
	 *            输入参数
	 * @return 输出 String
	 */
	public static String valueOf(Long inputValue) {
		if (inputValue == null)
			return "";
		return String.valueOf(inputValue);
	}

	/**
	 * toMd5 Method Method Description :MD5加密算法
	 * 
	 * @param inputValue
	 *            要加密的字符
	 * @param isLower
	 *            true为小写,false为大写
	 * @return
	 * @author Joe
	 * @date 2013-4-16
	 */
	public static String toMd5(String inputValue, boolean isLower) {
		if (inputValue == null || "".equals(inputValue))
			return "";
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.update(inputValue.getBytes("UTF8"));
			byte s[] = m.digest();
			String result = "";
			for (int i = 0; i < s.length; i++) {
				result += Integer.toHexString((0x000000ff & s[i]) | 0xffffff00).substring(6);
			}
			if (isLower) {
				return result.toLowerCase();
			}
			return result.toUpperCase();
		} catch (UnsupportedEncodingException e) {
			Log.i("SDK Method toMd5", inputValue + "toMd5 error ,error message:" + e.getMessage());
			return "";
		} catch (NoSuchAlgorithmException e) {
			Log.i("SDK Method toMd5", inputValue + "toMd5 error ,error message:" + e.getMessage());
			return "";
		}
	}

	public static boolean checkServerResponse(String response) {
		if (isNotEmpty(response)) {
			return true;
		}
		return false;
	}

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

	public static boolean isEmpty(CharSequence cs) {
		return ((cs == null) || (cs.length() == 0));
	}

	public static boolean isNotEmpty(CharSequence cs) {
		return (!(isEmpty(cs)));
	}
	
	public static String checkUrl(String url) {
		if (!TextUtils.isEmpty(url) && !url.endsWith("/")) {
			url = url + "/";
		}
		return url;
	}
	
	public static String appenUrl(String url,String urlMethod){
		url = checkUrl(url);
		if (TextUtils.isEmpty(url)) {
			return url;
		}
		return url + urlMethod;
	}
	
	
	public static String map2strData(Map<String, String> dataMap) {
		StringBuilder postData = new StringBuilder();
		String data = "";
		try {
			if (dataMap != null && !dataMap.isEmpty()) {
				for (Map.Entry<String, String> entry : dataMap.entrySet()) {
					String mapValue = entry.getValue();
					if (mapValue == null) {
						mapValue = "";
					}
					postData.append(entry.getKey()).append("=").append(URLEncoder.encode(mapValue,"UTF-8")).append("&");
				}
				data = postData.substring(0, postData.lastIndexOf("&"));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return data;
	}
}

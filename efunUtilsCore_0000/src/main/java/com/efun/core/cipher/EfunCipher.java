package com.efun.core.cipher;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


/**
* <p>Title: EfunCipher</p>
* <p>Description: 加密解密类</p>
* <p>Company: EFun</p> 
* @author GanYuanrong
* @date 2014年2月13日
*/
public class EfunCipher {
	
	private final static String DES = "DES";
	/**
	 * DESKEY 默认加密的key
	 */
	private final static String DESKEY = "DESefun////**";
	private final static String DES_SUFFIX = "$|$$***efun";
	private static final String IV_3DES = "12EFun89";
	
	/**
	 * <p>Description: </p>
	 * @param encryptData 需要加密的数据
	 * @return 加密后的信息后缀加上特定标识DES_SUFFIX
	 * @date 2015年2月5日
	 */
	public static String encryptEfunData(String encryptData){
		return encryptDES(encryptData, DESKEY) + DES_SUFFIX;
	}
	
	/**
	 * <p>Description: 解密的时候先判断是否有特定标识，没有返回，有就去掉特定标识后再解密</p>
	 * @param decryptData
	 * @return 返回解密后的原始数据
	 * @date 2015年2月5日
	 */
	public static String decryptEfunData(String decryptData){
		if (!decryptData.endsWith(DES_SUFFIX)) {
			return decryptData;
		}
		decryptData = decryptData.replace(DES_SUFFIX,"");
		return decryptDES(decryptData, DESKEY);
	}
	
	/**
	* <p>Title: encryptDES</p>
	* <p>Description: 进行DES数据加密</p>
	* @param encryptData 需要加密的数据
	* @return 返回DES加密之后通过base64编码的数据,使用默认加密的key
	*/
	public static String encryptEfunDES(String encryptData){
		return encryptDES(encryptData, DESKEY);
	}
	
	/**
	* <p>Title: decryptDES</p>
	* <p>Description: 解密DES加密之后通过base64编码的数据</p>
	* @param decryptData ES加密之后通过base64编码的数据
	* @return 返回原始数据
	*/
	public static String decryptEfunDES(String decryptData){
		return decryptDES(decryptData, DESKEY);
	}
	
	/**
	* <p>Title: encryptDES</p>
	* <p>Description: 进行DES数据加密</p>
	* @param encryptData 需要加密的数据
	* @param cipherKey 加密key（至少8位）
	* @return  返回DES加密之后通过base64编码的数据
	*/
	public static String encryptDES(String encryptData,String cipherKey) {
		String result = null;
		try {
			SecureRandom random = new SecureRandom();//加密的强随机数生成器
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);//密匙工厂
			KeySpec keySpec = new DESKeySpec(cipherKey.getBytes());//密钥规范
			SecretKey secretKey = keyFactory.generateSecret(keySpec);//生成密钥
			Cipher cipher = Cipher.getInstance(DES);//加密器
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, random);
			byte[] byteResult = cipher.doFinal(encryptData.getBytes());
			String d = binaryToHexString(byteResult);
			Log.d("efun", "byteResult:" + d);
			result = Base64.encodeToString(byteResult, Base64.DEFAULT);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	/**
	* <p>Title: decryptDES</p>
	* <p>Description: 解密DES加密之后通过base64编码的数据</p>
	* @param base64String ES加密之后通过base64编码的数据
	* @param cipherKey 解密key（需要与加密key相同）
	* @return 返回原始数据
	*/
	public static String decryptDES(String base64String, String cipherKey) {
		String result = null;
		try {
			SecureRandom random = new SecureRandom();
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
			KeySpec keySpec = new DESKeySpec(cipherKey.getBytes());
			SecretKey secretKey = keyFactory.generateSecret(keySpec);
			Cipher cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, random);
			byte[] byteResult = cipher.doFinal(Base64.decode(base64String, Base64.DEFAULT));
			result = new String(byteResult);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
/*    *//**
     * 加密
     * 
     * @param content 需要加密的内容
     * @param key  加密密码key
     * @return
     *//*
	public static String encryptByAes128(String content, String efunKey) {
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(efunKey.getBytes("utf-8")));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			byte[] byteContent = content.getBytes("utf-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
			byte[] byteResult = cipher.doFinal(byteContent);
			String result = Base64.encodeToString(byteResult, Base64.DEFAULT);
			return result; // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
    *//**解密
     * @param content  待解密内容
     * @param efunKey 解密密钥
     * @return
     *//*
	public static String decryptByAes128(String content, String efunKey) {
		if (TextUtils.isEmpty(content)) {
			return null;
		}
		try {
			KeyGenerator kgen = KeyGenerator.getInstance("AES");
			kgen.init(128, new SecureRandom(efunKey.getBytes("utf-8")));
			SecretKey secretKey = kgen.generateKey();
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
			Cipher cipher = Cipher.getInstance("AES");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
			byte[] result = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
			return new String(result,"UTF-8"); // 加密
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}*/
	
	
	public static String encrypt3DES(String plainText, String secretKey) throws Exception {
		if (TextUtils.isEmpty(plainText) || TextUtils.isEmpty(secretKey)) {
			return null;
		}
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		Key deskey = keyfactory.generateSecret(spec);
		IvParameterSpec ips = new IvParameterSpec(IV_3DES.getBytes("utf-8"));
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
		byte[] encryptData = cipher.doFinal(plainText.getBytes("utf-8"));
		String base64_ciphertext = Base64.encodeToString(encryptData, Base64.DEFAULT);
		return base64_ciphertext;

	}  

	public static String decrypt3DES(String encryptText, String secretKey) throws Exception {
		if (TextUtils.isEmpty(encryptText) || TextUtils.isEmpty(secretKey)) {
			return null;
		}
		DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes("utf-8"));
		SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
		Key deskey = keyfactory.generateSecret(spec);
		Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
		IvParameterSpec ips = new IvParameterSpec(IV_3DES.getBytes("utf-8"));
		cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
		byte[] decryptData = cipher.doFinal(Base64.decode(encryptText, Base64.DEFAULT));
		return new String(decryptData, "utf-8");

	}  
	 
	
    /** 
     *  
     * @param bytes 
     * @return 将二进制转换为十六进制字符输出 
     */  
    public static String binaryToHexString(byte[] bytes){  
    	String result = "";  
        if (bytes == null) {
        	return result; 
		}
        String hex = "";  
        for(int i=0;i<bytes.length;i++){  
            //字节高4位  
            hex = String.valueOf(hexStr.charAt((bytes[i]&0xF0)>>4));  
            //字节低4位  
            hex += String.valueOf(hexStr.charAt(bytes[i]&0x0F));  
            result +=hex;  
        }  
        return result;  
    }  
    
    /** 
     *  
     * @param hexString 
     * @return 将十六进制转换为字节数组 
     */  
	public static byte[] hexStringToBinary(String hexString) {
		// hexString的长度对2取整，作为bytes的长度
		int len = hexString.length() / 2;
		byte[] bytes = new byte[len];
		byte high = 0;// 字节高四位
		byte low = 0;// 字节低四位
		for (int i = 0; i < len; i++) {
			// 右移四位得到高位
			high = (byte) ((hexStr.indexOf(hexString.charAt(2 * i))) << 4);
			low = (byte) hexStr.indexOf(hexString.charAt(2 * i + 1));
			bytes[i] = (byte) (high | low);// 高地位做或运算
		}
		return bytes;
	}  

    private static String hexStr =  "0123456789ABCDEF";
}

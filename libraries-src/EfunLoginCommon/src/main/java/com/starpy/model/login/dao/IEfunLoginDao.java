/**
 * 
 */
package com.starpy.model.login.dao;

import com.core.base.exception.EfunException;

/**
 * <p>Title: IEfunLoginDao</p>
 * <p>Description: </p>
 * <p>Company: EFun</p> 
 * @author GanYuanrong
 * @date 2013年12月7日
 */
public interface IEfunLoginDao {
	
	public String efunRequestServer() throws EfunException;
	
}

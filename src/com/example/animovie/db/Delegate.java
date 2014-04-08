/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.db;

import java.lang.reflect.Method;

public class Delegate < ParamType > {
	
	private Object _object;
	private String     _method;

	public Delegate(Object object, String method) {
		_object = object;
		_method = method;
	}
	
	public void invoke(ParamType param) {
		try {
			Object[] params = new Object[1];
			params[0] = param;
			Method method = _object.getClass().getDeclaredMethod(_method, param.getClass());
			method.setAccessible(true);
			method.invoke(_object, params);
			method.setAccessible(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
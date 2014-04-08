/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.db;

public class URLParam
{
	public String key = "";
	public String value = "";
	
	URLParam(String key, String value)
	{
		this.key = key;
		this.value = value;
	}
	
	String construct() { return key + "=" + value; }
	
	static String construct(URLParam[] list, boolean question_mark) {
		
		StringBuilder result = new StringBuilder("");
		if (list.length > 0) {
			int start = 0;
			if (question_mark) {
				result.append("?").append(list[0].construct());
				start = 1;
			}
			for(int p = start; p < list.length; ++p) {
				result.append("&").append(list[p].construct());
			}
		}
		return result.toString();
	}
}
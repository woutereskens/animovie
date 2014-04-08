/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import android.os.Bundle;

import com.example.animovie.R;
import com.example.animovie.global.Global_Menu;

public class About extends Global_Menu{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
	}
}

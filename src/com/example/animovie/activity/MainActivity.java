/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import android.os.Bundle;

import com.example.animovie.DatabaseHelper;
import com.example.animovie.R;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.global.Global_Variables;
//import com.example.animovie.DatabaseHelper;

public class MainActivity extends Global_Menu {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		DatabaseHelper database_helper;
		
		database_helper = new DatabaseHelper(this);
		
		Global_Variables.database_helper = database_helper;
	}
}

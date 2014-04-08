/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.global;

import com.example.animovie.DatabaseHelper;
import com.example.animovie.model.Movie;
import com.example.animovie.model.Person;
import com.example.animovie.model.User;

public class Global_Variables {
	public static User loggedInUser;
	public static Movie movie_detail;
	public static Person person_detail;
	public static DatabaseHelper database_helper;
}
/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import com.example.animovie.R;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.global.Global_Variables;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class My_Profile extends Global_Menu {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_profile);
		
		TextView textViewUsername = (TextView) findViewById(R.id.my_profile_username);
		textViewUsername.setText(Global_Variables.loggedInUser.getUsername());
		
		TextView textViewEmail = (TextView) findViewById(R.id.my_profile_email);
		textViewEmail.setText(Global_Variables.loggedInUser.getEmail());
		
		/*List<Movie> movies = Global_Variables.database_helper.getAllMoviesByUser(Global_Variables.loggedInUser.getId());
		
		ArrayList<String> movie_titles = new ArrayList<String>();
		
		for(int i=0; i<movies.size(); i++){
			movie_titles.add(movies.get(i).getTitle() + " (" + movies.get(i).getYear() + ")");
		}
		
		Spinner spinner_movies = (Spinner) findViewById(R.id.my_profile_movies);
		ArrayAdapter<String> adapter_movies = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, movie_titles);
		spinner_movies.setAdapter(adapter_movies);*/
	}
	
	public void onClickLogOut(View v){
		Global_Variables.loggedInUser = null;
		
		Intent i = new Intent(getApplicationContext(), MainActivity.class);
    	startActivity(i);
    	
    	Toast toast = Toast.makeText(getApplicationContext(), "Succesfully logged out", Toast.LENGTH_LONG);
		toast.show();
	}
	
	public void onClickQuiz(View v){
		Intent i = new Intent (getApplicationContext(), Quiz.class);
		startActivity(i);
	}
}

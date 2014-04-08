/* Application: animovie
 * Author: Wouter Eskens
 * API: The Movie Database (TMDb)
 * Database: SQLite 
 * Class: Applied Computer Sciences
 * School: Thomas More Kempen
 * Year: 2013-2014 */

package com.example.animovie.activity;

import java.util.ArrayList;
import java.util.LinkedList;

import com.example.animovie.R;
import com.example.animovie.db.TMDBConnector;
import com.example.animovie.db.TMDBConnector.PersonDetailsResultListener;
import com.example.animovie.global.Global_Menu;
import com.example.animovie.global.Global_Variables;
import com.example.animovie.model.Movie_Actor;
import com.example.animovie.model.Movie_Director;
import com.example.animovie.model.Person;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Person_Detail extends Global_Menu implements PersonDetailsResultListener{  
	
	private Person _person;
	private LinkedList<Movie_Actor> _roles;
	private LinkedList<Movie_Director> _directors;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.person_detail);
		
		Person person = Global_Variables.person_detail;
		
		ImageView person_picture = (ImageView) findViewById(R.id.person_detail_picture);
		TextView person_name = (TextView) findViewById(R.id.person_detail_name);
		TextView person_info = (TextView) findViewById(R.id.person_detail_info);
		
		person_picture.setImageBitmap(person.getPicture());
		person_name.setText(person.getName());
		person_info.setText(person.getInfo());
		
		TMDBConnector.getInstance().retrievePersonDetails(person, this);
	}

	@Override
	public void onPersonDetailsComplete(Person person,
			LinkedList<Movie_Actor> roles, LinkedList<Movie_Director> directors) {
		_person = person;
		_roles = roles;
		_directors = directors;
		
		ImageView person_picture = (ImageView) findViewById(R.id.person_detail_picture);
		TextView person_name = (TextView) findViewById(R.id.person_detail_name);
		TextView person_info = (TextView) findViewById(R.id.person_detail_info);
		
		person_picture.setImageBitmap(person.getPicture());
		person_name.setText(person.getName());
		person_info.setText(person.getInfo());
		
		ArrayList<String> movie_actor_list = new ArrayList<String>();
		
		for (int i = 0; i <= roles.size()-1; i++) {
            Movie_Actor movie_actor = roles.get(i);
            
            if(movie_actor.getMovie() != null){
            	movie_actor_list.add(movie_actor.getMovie().getTitle() + " (" + movie_actor.getMovie().getYear() + ") - " + movie_actor.getRole());
            } else {
            	movie_actor_list.add(movie_actor.getMovieId() + " - " + movie_actor.getRole());
            }
        }
		
		ArrayList<String> movie_director_list = new ArrayList<String>();
		
		for (int i = 0; i <= directors.size()-1; i++) {
            Movie_Director movie_director = directors.get(i);
            
            if(movie_director.getMovie() != null){
            	movie_director_list.add(movie_director.getMovie().getTitle() + " (" + movie_director.getMovie().getYear() + ")");
            } else {
            	movie_director_list.add(Long.toString(movie_director.getMovieId()));
            }
        }
		
		Spinner spinner_movies_directed = (Spinner) findViewById(R.id.person_detail_movies_director);
		ArrayAdapter<String> adapter_movies_director = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, movie_director_list);
		spinner_movies_directed.setAdapter(adapter_movies_director);
		
		Spinner spinner_movies_acted = (Spinner) findViewById(R.id.person_detail_movies_actor);
		ArrayAdapter<String> adapter_movies_actors = new ArrayAdapter <String>(this, android.R.layout.simple_spinner_item, movie_actor_list);
		spinner_movies_acted.setAdapter(adapter_movies_actors);
	}

	@Override
	public void onPersonRoleUpdate(Movie_Actor actor) {
		// TODO Auto-generated method stub
		onPersonDetailsComplete(_person, _roles, _directors);
	}

	@Override
	public void onPersonDirectorUpdate(Movie_Director director) {
		// TODO Auto-generated method stub
		onPersonDetailsComplete(_person, _roles, _directors);
	}
}
